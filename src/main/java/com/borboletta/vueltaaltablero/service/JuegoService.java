package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDeJuego;
import com.borboletta.vueltaaltablero.dto.DetalleDeJuego;
import com.borboletta.vueltaaltablero.entity.CategoriaJuego;
import com.borboletta.vueltaaltablero.entity.FichaTecnica;
import com.borboletta.vueltaaltablero.entity.Juego;
import com.borboletta.vueltaaltablero.exception.JuegoNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.JuegoYaExisteException;
import com.borboletta.vueltaaltablero.repository.JuegoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* Juego y FichaTecnica se gestionan juntos: la ficha no tiene vida propia. */
@Service
@RequiredArgsConstructor
public class JuegoService{

    private final JuegoRepository juegoRepository;

    /** Todo el catálogo ("Todos los juegos"). */
    @Transactional(readOnly = true)
    public List<DetalleDeJuego> catalogo(){
        return catalogo(null);
    }

    /** El catálogo de una categoría; con null, sin filtrar. */
    @Transactional(readOnly = true)
    public List<DetalleDeJuego> catalogo(CategoriaJuego categoria){
        List<Juego> juegos = categoria == null
                ? juegoRepository.findAllByFechaBajaIsNullOrderByTituloAsc()
                : juegoRepository.findAllByCategoriaAndFechaBajaIsNullOrderByTituloAsc(categoria);
        return juegos.stream()
                .map(DetalleDeJuego::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public DetalleDeJuego obtener(Long id){
        return DetalleDeJuego.desde(buscar(id));
    }

    /* Si el título es de un juego que estaba dado de baja, se lo reactiva con los datos nuevos
    en lugar de crear otra fila: la base no se llena de duplicados. Si no, se crea uno nuevo. */
    @Transactional
    public void crear(DatosDeJuego datos){
        juegoRepository.findByTituloIgnoreCaseAndFechaBajaIsNotNull(datos.titulo())
                .ifPresentOrElse(
                        juegoDeBaja -> reponerEnElCatalogo(juegoDeBaja, datos),
                        () -> crearNuevo(datos));
    }

    /* No hay save(): el juego que trae el repositorio queda administrado por JPA y, al terminar
    la transacción, Hibernate detecta los cambios (también los de la ficha) y hace el UPDATE. */
    @Transactional
    public void actualizar(Long id, DatosDeJuego datos){
        Juego juego = buscar(id);
        validarTituloLibre(datos.titulo(), id);
        juego.actualizarDatos(datos.titulo(), datos.categoria(), datos.editorial());
        actualizarFicha(juego, datos);
    }

    /* Baja lógica: una sola actualización. Sus ejemplares dejan de mostrarse porque las
    consultas de ejemplares solo traen los de juegos que siguen en el catálogo. */
    @Transactional
    public void darDeBaja(Long id){
        buscar(id).darDeBaja();
    }

    private void reponerEnElCatalogo(Juego juegoDeBaja, DatosDeJuego datos){
        juegoDeBaja.reactivar(datos.titulo(), datos.categoria(), datos.editorial());
        actualizarFicha(juegoDeBaja, datos);
    }

    private void crearNuevo(DatosDeJuego datos){
        if (juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNull(datos.titulo())){
            throw JuegoYaExisteException.enElCatalogo();
        }
        FichaTecnica ficha = FichaTecnica.crear(datos.minJugadores(), datos.maxJugadores(),
                datos.minEdad(), datos.descripcion(), datos.anioPublicacion());
        juegoRepository.save(
                Juego.crear(datos.titulo(), datos.categoria(), datos.editorial(), ficha));
    }

    private void actualizarFicha(Juego juego, DatosDeJuego datos){
        juego.actualizarFicha(datos.minJugadores(), datos.maxJugadores(), datos.minEdad(),
                datos.descripcion(), datos.anioPublicacion());
    }

    private void validarTituloLibre(String titulo, Long idPropio){
        if (juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNullAndIdNot(titulo, idPropio)){
            throw JuegoYaExisteException.enElCatalogo();
        }
        if (juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNotNullAndIdNot(titulo, idPropio)){
            throw JuegoYaExisteException.dadoDeBaja();
        }
    }

    private Juego buscar(Long id){
        return juegoRepository.findByIdAndFechaBajaIsNull(id)
                .orElseThrow(() -> new JuegoNoEncontradoException(id));
    }
}
