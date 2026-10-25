package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDeEdicionDeEjemplar;
import com.borboletta.vueltaaltablero.dto.DatosDeNuevoEjemplar;
import com.borboletta.vueltaaltablero.dto.DetalleDeEjemplar;

import com.borboletta.vueltaaltablero.entity.Ejemplar;
import com.borboletta.vueltaaltablero.entity.Juego;
import com.borboletta.vueltaaltablero.entity.Usuario;

import com.borboletta.vueltaaltablero.exception.EjemplarNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.EjemplarYaAlquiladoException;
import com.borboletta.vueltaaltablero.exception.JuegoNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.UsuarioNoEncontradoException;

import com.borboletta.vueltaaltablero.repository.AlquilerRepository;
import com.borboletta.vueltaaltablero.repository.EjemplarRepository;
import com.borboletta.vueltaaltablero.repository.JuegoRepository;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;

import com.borboletta.vueltaaltablero.paginacion.Paginas;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EjemplarService{

    private final EjemplarRepository ejemplarRepository;
    private final JuegoRepository juegoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlquilerRepository alquilerRepository;

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    /* La lista pública ya excluye por consulta a los alquilados: por eso "alquilado" es false. */
    @Transactional(readOnly = true)
    public Page<DetalleDeEjemplar> disponiblesDeJuego(Long idJuego, Pageable pedido){
        return Paginas.consultar(pedido, pagina -> ejemplarRepository.findDisponiblesDeJuego(idJuego, pagina))
                .map(ejemplar -> DetalleDeEjemplar.desde(ejemplar, false));
    }

    /* Los del dueño sí pueden estar alquilados: una sola consulta trae cuáles, sin una por ejemplar. */
    @Transactional(readOnly = true)
    public List<DetalleDeEjemplar> deUsuario(String correo){
        Set<Long> alquilados = alquilerRepository.findIdsDeEjemplaresEnCursoDe(correo);
        return ejemplarRepository.findActivosDe(correo).stream()
                .map(ejemplar -> DetalleDeEjemplar.desde(
                        ejemplar, alquilados.contains(ejemplar.getId())))
                .toList();
    }

    /* Para la página pública de un ejemplar: la ve cualquiera, esté disponible, alquilado o no. */
    @Transactional(readOnly = true)
    public DetalleDeEjemplar obtener(Long id){
        Ejemplar ejemplar = ejemplarRepository.findVisiblePorId(id)
                .orElseThrow(() -> EjemplarNoEncontradoException.enLaPlataforma(id));
        return DetalleDeEjemplar.desde(ejemplar, estaAlquilado(id));
    }

    /* ¿El ejemplar es de esta persona? La página pública lo usa para no ofrecerle alquilar lo suyo. */
    @Transactional(readOnly = true)
    public boolean esDe(Long id, String correo){
        return ejemplarRepository.findActivoDe(id, correo).isPresent();
    }

    @Transactional(readOnly = true)
    public DetalleDeEjemplar obtenerDeUsuario(Long id, String correo){
        return DetalleDeEjemplar.desde(buscarDeUsuario(id, correo), estaAlquilado(id));
    }

    @Transactional
    public void publicar(String correoPropietario, DatosDeNuevoEjemplar datos){
        Usuario propietario = usuarioRepository
                .findByCorreoIgnoreCaseAndFechaBajaIsNull(correoPropietario)
                .orElseThrow(() -> new UsuarioNoEncontradoException(correoPropietario));
        Juego juego = juegoRepository.findByIdAndFechaBajaIsNull(datos.idJuego())
                .orElseThrow(() -> new JuegoNoEncontradoException(datos.idJuego()));
        ejemplarRepository.save(Ejemplar.publicar(juego, propietario, datos.estado(),
                datos.disponibleAlquiler(), datos.precioPorDia()));
    }

    @Transactional
    public void actualizar(Long id, String correo, DatosDeEdicionDeEjemplar datos){
        buscarDeUsuario(id, correo).actualizar(
                datos.estado(), datos.disponibleAlquiler(), datos.precioPorDia());
    }

        /* Un ejemplar con un alquiler abierto no se puede quitar: dejaría
    el alquiler huérfano. Primero hay que registrar su devolución. */
    @Transactional
    public void darDeBaja(Long id, String correo){
        Ejemplar ejemplar = buscarDeUsuario(id, correo);
        if (estaAlquilado(id)){
            throw new EjemplarYaAlquiladoException(
                    "No podés quitar este ejemplar mientras está alquilado. "
                            + "Primero registrá su devolución.");
        }
        ejemplar.darDeBaja();
    }

    private boolean estaAlquilado(Long idEjemplar){
        return alquilerRepository.existsByEjemplarIdAndFechaDevolucionIsNull(idEjemplar);
    }

    private Ejemplar buscarDeUsuario(Long id, String correo){
        return ejemplarRepository.findActivoDe(id, correo)
                .orElseThrow(() -> new EjemplarNoEncontradoException(id));
    }
}