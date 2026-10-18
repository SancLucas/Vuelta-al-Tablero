package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDeJuego;
import com.borboletta.vueltaaltablero.dto.DetalleDeJuego;
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

    @Transactional(readOnly = true)
    public List<DetalleDeJuego> catalogo(){
        return juegoRepository.findAllByFechaBajaIsNullOrderByTituloAsc().stream()
                .map(DetalleDeJuego::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public DetalleDeJuego obtener(Long id){
        return DetalleDeJuego.desde(buscar(id));
    }

    @Transactional
    public void crear(DatosDeJuego datos){
        rechazarSiElTituloEstaOcupado(
                juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNull(datos.titulo()));
        FichaTecnica ficha = FichaTecnica.crear(datos.minJugadores(), datos.maxJugadores(),
                datos.minEdad(), datos.descripcion(), datos.anioPublicacion());
        juegoRepository.save(
                Juego.crear(datos.titulo(), datos.categoria(), datos.editorial(), ficha));
    }

    /* No hay save(): el juego que trae el repositorio queda administrado por JPA y, al terminar
    la transacción, Hibernate detecta los cambios (también los de la ficha) y hace el UPDATE. */
    @Transactional
    public void actualizar(Long id, DatosDeJuego datos){
        Juego juego = buscar(id);
        rechazarSiElTituloEstaOcupado(
                juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNullAndIdNot(datos.titulo(), id));
        juego.actualizarDatos(datos.titulo(), datos.categoria(), datos.editorial());
        juego.actualizarFicha(datos.minJugadores(), datos.maxJugadores(), datos.minEdad(),
                datos.descripcion(), datos.anioPublicacion());
    }

    /* Baja lógica: una sola actualización. Sus ejemplares dejan de mostrarse porque las
    consultas de ejemplares solo traen los de juegos que siguen en el catálogo. */
    @Transactional
    public void darDeBaja(Long id){
        buscar(id).darDeBaja();
    }

    private void rechazarSiElTituloEstaOcupado(boolean ocupado){
        if (ocupado){
            throw new JuegoYaExisteException();
        }
    }

    private Juego buscar(Long id){
        return juegoRepository.findByIdAndFechaBajaIsNull(id)
                .orElseThrow(() -> new JuegoNoEncontradoException(id));
    }
}
