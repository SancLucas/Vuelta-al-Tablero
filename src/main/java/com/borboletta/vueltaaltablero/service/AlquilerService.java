package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDeNuevoAlquiler;
import com.borboletta.vueltaaltablero.dto.DetalleDeAlquiler;
import com.borboletta.vueltaaltablero.entity.Alquiler;
import com.borboletta.vueltaaltablero.entity.Ejemplar;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.exception.AlquilerNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.EjemplarNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.EjemplarYaAlquiladoException;
import com.borboletta.vueltaaltablero.exception.UsuarioNoEncontradoException;
import com.borboletta.vueltaaltablero.repository.AlquilerRepository;
import com.borboletta.vueltaaltablero.repository.EjemplarRepository;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* Quién alquila o devuelve llega siempre como correo de la sesión, nunca de un formulario. */
@Service
@RequiredArgsConstructor
public class AlquilerService{

    private final AlquilerRepository alquilerRepository;
    private final EjemplarRepository ejemplarRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void alquilar(Long idEjemplar, String correoInquilino, DatosDeNuevoAlquiler datos){
        Usuario inquilino = usuarioRepository
                .findByCorreoIgnoreCaseAndFechaBajaIsNull(correoInquilino)
                .orElseThrow(() -> new UsuarioNoEncontradoException(correoInquilino));
        Ejemplar ejemplar = ejemplarRepository.findVisiblePorId(idEjemplar)
                .orElseThrow(() -> EjemplarNoEncontradoException.enLaPlataforma(idEjemplar));

        // Tell, Don't Ask: el ejemplar decide si puede alquilarse y devuelve el alquiler.
        Alquiler alquiler = ejemplar.alquilarA(
                inquilino, datos.fechaEntrega(), datos.fechaDevolucion());

        // Lo único que el ejemplar no sabe: si ya tiene otro alquiler abierto.
        if (alquilerRepository.existsByEjemplarIdAndFechaDevolucionIsNull(idEjemplar)){
            throw new EjemplarYaAlquiladoException();
        }
        alquilerRepository.save(alquiler);
    }

    /* Solo el propietario del ejemplar registra la devolución (es quien lo recibe de vuelta). */
    @Transactional
    public void registrarDevolucion(Long idAlquiler, String correoPropietario){
        alquilerRepository.findDelPropietario(idAlquiler, correoPropietario)
                .orElseThrow(() -> new AlquilerNoEncontradoException(idAlquiler))
                .registrarDevolucion();
    }

    @Transactional(readOnly = true)
    public List<DetalleDeAlquiler> comoInquilino(String correo){
        return alquilerRepository.findDeInquilino(correo).stream()
                .map(DetalleDeAlquiler::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DetalleDeAlquiler> deMisEjemplares(String correo){
        return alquilerRepository.findDeEjemplaresDe(correo).stream()
                .map(DetalleDeAlquiler::desde)
                .toList();
    }
    
        /* El alquiler abierto de esta persona sobre este ejemplar, si lo hay. Trae el teléfono del
    dueño (solo mientras está abierto) para que coordinen la entrega. */
    @Transactional(readOnly = true)
    public Optional<DetalleDeAlquiler> enCursoDe(Long idEjemplar, String correoInquilino){
        return alquilerRepository.findEnCursoDeInquilino(idEjemplar, correoInquilino)
                .map(DetalleDeAlquiler::desde);
    }
    
        /* Cancelar un alquiler que todavía no empezó lo elimina: nunca llegó a ocurrir, así que no
    deja historial ni cuenta para criticar. Solo lo cancela el dueño del ejemplar (se busca por
    id Y por dueño: el alquiler de un ejemplar ajeno responde 404). */
    @Transactional
    public void cancelar(Long idAlquiler, String correoPropietario){
        Alquiler alquiler = alquilerRepository.findDelPropietario(idAlquiler, correoPropietario)
                .orElseThrow(() -> new AlquilerNoEncontradoException(idAlquiler));
        alquiler.exigirQuePuedaCancelarse();
        alquilerRepository.delete(alquiler);
    }
}