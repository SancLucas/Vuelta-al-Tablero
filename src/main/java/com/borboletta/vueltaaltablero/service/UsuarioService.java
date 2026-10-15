package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDeRegistro;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.exception.UsuarioYaRegistradoException;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public void registrar(DatosDeRegistro datos) {
        validarQueNoExista(datos);
        Usuario nuevo = Usuario.registrar(
                datos.nombre(), datos.apellido(), datos.dni(),
                datos.correo(), datos.telefono(), datos.contrasenia());
        usuarioRepository.save(nuevo);
    }

    private void validarQueNoExista(DatosDeRegistro datos) {
        if (usuarioRepository.existsByCorreoIgnoreCase(datos.correo())) {
            throw new UsuarioYaRegistradoException("correo", "Ya existe una cuenta con ese correo.");
        }
        if (usuarioRepository.existsByDni(datos.dni())) {
            throw new UsuarioYaRegistradoException("dni", "Ya existe una cuenta con ese DNI.");
        }
    }
}