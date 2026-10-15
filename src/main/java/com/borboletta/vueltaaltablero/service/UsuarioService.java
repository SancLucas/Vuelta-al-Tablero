package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDePerfil;
import com.borboletta.vueltaaltablero.dto.DatosDeRegistro;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.exception.UsuarioNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.UsuarioYaRegistradoException;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodosLosUsuarios(){
        return usuarioRepository.findAll();
    }

    @Transactional
    public void registrar(DatosDeRegistro datos){
        validarQueNoExista(datos);
        Usuario nuevo = Usuario.registrar(
                datos.nombre(), datos.apellido(), datos.dni(),
                datos.correo(), datos.telefono(), passwordEncoder.encode(datos.contrasenia()));
        usuarioRepository.save(nuevo);
    }

    @Transactional(readOnly = true)
    public DatosDePerfil obtenerPerfil(String correo){
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new UsuarioNoEncontradoException(correo));
        return DatosDePerfil.desde(usuario);
    }

    private void validarQueNoExista(DatosDeRegistro datos){
        if (usuarioRepository.existsByCorreoIgnoreCase(datos.correo())){
            throw new UsuarioYaRegistradoException("correo", "Ya existe una cuenta con ese correo.");
        }
        if (usuarioRepository.existsByDni(datos.dni())) {
            throw new UsuarioYaRegistradoException("dni", "Ya existe una cuenta con ese DNI.");
        }
    }

    public Usuario buscarPorDni(String dni){
    Usuario usuarioEncontrado = null;

    Optional<Usuario> usuarioOpt = usuarioRepository.findAll().stream()
            .filter(u -> u.getDni() != null && u.getDni().equals(dni))
            .findFirst();

    if (usuarioOpt.isPresent()) {
        usuarioEncontrado = usuarioOpt.get();
    }
    
    return usuarioEncontrado; // Único punto de retorno
    }
}