package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDeCritica;
import com.borboletta.vueltaaltablero.dto.DetalleDeCritica;
import com.borboletta.vueltaaltablero.entity.CriticaEjemplar;
import com.borboletta.vueltaaltablero.entity.CriticaEjemplarId;
import com.borboletta.vueltaaltablero.entity.CriticaJuego;
import com.borboletta.vueltaaltablero.entity.CriticaJuegoId;
import com.borboletta.vueltaaltablero.entity.Ejemplar;
import com.borboletta.vueltaaltablero.entity.Juego;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.exception.CriticaNoPermitidaException;
import com.borboletta.vueltaaltablero.exception.EjemplarNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.JuegoNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.UsuarioNoEncontradoException;
import com.borboletta.vueltaaltablero.repository.AlquilerRepository;
import com.borboletta.vueltaaltablero.repository.CriticaEjemplarRepository;
import com.borboletta.vueltaaltablero.repository.CriticaJuegoRepository;
import com.borboletta.vueltaaltablero.repository.EjemplarRepository;
import com.borboletta.vueltaaltablero.repository.JuegoRepository;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* Una persona tiene como máximo una crítica por juego y una por ejemplar: si vuelve a enviarla,
se corrige la anterior en vez de crear otra. El autor sale siempre de la sesión.
Al juego lo puede criticar cualquiera con sesión; al ejemplar, solo quien lo alquiló y devolvió. */
@Service
@RequiredArgsConstructor
public class CriticaService{

    private final CriticaEjemplarRepository criticaEjemplarRepository;
    private final CriticaJuegoRepository criticaJuegoRepository;
    private final AlquilerRepository alquilerRepository;
    private final EjemplarRepository ejemplarRepository;
    private final JuegoRepository juegoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void criticarEjemplar(Long idEjemplar, String correoAutor, DatosDeCritica datos){
        Usuario autor = buscarAutor(correoAutor);
        Ejemplar ejemplar = ejemplarRepository.findVisiblePorId(idEjemplar)
                .orElseThrow(() -> EjemplarNoEncontradoException.enLaPlataforma(idEjemplar));
        if (!alquilerRepository.haDevueltoElEjemplar(autor.getNro(), idEjemplar)){
            throw new CriticaNoPermitidaException(
                    "Solo podés criticar un ejemplar que alquilaste y ya devolviste.");
        }
        criticaEjemplarRepository
                .findById(new CriticaEjemplarId(idEjemplar, autor.getNro()))
                .ifPresentOrElse(
                        critica -> critica.actualizar(datos.comentario(), datos.puntuacion()),
                        () -> criticaEjemplarRepository.save(CriticaEjemplar.escribir(
                                ejemplar, autor, datos.comentario(), datos.puntuacion())));
    }

    @Transactional
    public void criticarJuego(Long idJuego, String correoAutor, DatosDeCritica datos){
        Usuario autor = buscarAutor(correoAutor);
        Juego juego = juegoRepository.findByIdAndFechaBajaIsNull(idJuego)
                .orElseThrow(() -> new JuegoNoEncontradoException(idJuego));
        criticaJuegoRepository
                .findById(new CriticaJuegoId(idJuego, autor.getNro()))
                .ifPresentOrElse(
                        critica -> critica.actualizar(datos.comentario(), datos.puntuacion()),
                        () -> criticaJuegoRepository.save(CriticaJuego.escribir(
                                juego, autor, datos.comentario(), datos.puntuacion())));
    }

    @Transactional(readOnly = true)
    public List<DetalleDeCritica> deEjemplar(Long idEjemplar){
        return criticaEjemplarRepository.findDeEjemplar(idEjemplar).stream()
                .map(critica -> DetalleDeCritica.desde(critica.getAutor(), critica.getOpinion()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DetalleDeCritica> deJuego(Long idJuego){
        return criticaJuegoRepository.findDeJuego(idJuego).stream()
                .map(critica -> DetalleDeCritica.desde(critica.getAutor(), critica.getOpinion()))
                .toList();
    }

    private Usuario buscarAutor(String correo){
        return usuarioRepository.findByCorreoIgnoreCaseAndFechaBajaIsNull(correo)
                .orElseThrow(() -> new UsuarioNoEncontradoException(correo));
    }
}