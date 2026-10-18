package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDeEdicionDeEjemplar;
import com.borboletta.vueltaaltablero.dto.DatosDeNuevoEjemplar;
import com.borboletta.vueltaaltablero.dto.DetalleDeEjemplar;
import com.borboletta.vueltaaltablero.entity.Ejemplar;
import com.borboletta.vueltaaltablero.entity.Juego;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.exception.EjemplarNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.JuegoNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.UsuarioNoEncontradoException;
import com.borboletta.vueltaaltablero.repository.EjemplarRepository;
import com.borboletta.vueltaaltablero.repository.JuegoRepository;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EjemplarService{

    private final EjemplarRepository ejemplarRepository;
    private final JuegoRepository juegoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<DetalleDeEjemplar> disponiblesParaAlquilar(){
        return ejemplarRepository.findDisponiblesParaAlquilar().stream()
                .map(DetalleDeEjemplar::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DetalleDeEjemplar> deUsuario(String correo){
        return ejemplarRepository.findActivosDe(correo).stream()
                .map(DetalleDeEjemplar::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public DetalleDeEjemplar obtenerDeUsuario(Long id, String correo){
        return DetalleDeEjemplar.desde(buscarDeUsuario(id, correo));
    }

    @Transactional
    public void publicar(String correoPropietario, DatosDeNuevoEjemplar datos){
        Usuario propietario = usuarioRepository
                .findByCorreoIgnoreCaseAndFechaBajaIsNull(correoPropietario)
                .orElseThrow(() -> new UsuarioNoEncontradoException(correoPropietario));
        Juego juego = juegoRepository.findByIdAndFechaBajaIsNull(datos.idJuego())
                .orElseThrow(() -> new JuegoNoEncontradoException(datos.idJuego()));
        ejemplarRepository.save(Ejemplar.publicar(
                juego, propietario, datos.estado(), datos.disponibleAlquiler()));
    }

    @Transactional
    public void actualizar(Long id, String correo, DatosDeEdicionDeEjemplar datos){
        buscarDeUsuario(id, correo).actualizar(datos.estado(), datos.disponibleAlquiler());
    }

    @Transactional
    public void darDeBaja(Long id, String correo){
        buscarDeUsuario(id, correo).darDeBaja();
    }

    private Ejemplar buscarDeUsuario(Long id, String correo){
        return ejemplarRepository.findActivoDe(id, correo)
                .orElseThrow(() -> new EjemplarNoEncontradoException(id));
    }
}
