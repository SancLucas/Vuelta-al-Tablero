package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.borboletta.vueltaaltablero.exception.UsuarioNotFoundException;
import com.borboletta.vueltaaltablero.util.Constants;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService{
    @Autowired
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario createUsuario(String nombre, String apellido, String dni, String correo, String contrasenia, String telefono, LocalDate fecha_reg, boolean rol) {
        Usuario result = null;
        Usuario newUsuario = new Usuario(null, nombre, apellido, dni, correo, contrasenia, telefono, fecha_reg, rol);
        result = usuarioRepository.save(newUsuario);
        return result; // Single return
    }

    public Usuario getUsuario(Long id) {
        Usuario result;
        // Se usa Optional para evitar retornar null y hacer explícito que el usuario puede no existir.
        // Esto obliga al llamador a manejar la ausencia de valor de forma controlada.
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isPresent()) {
            result = usuarioOpt.get();
        } else {
            throw new UsuarioNotFoundException(Constants.MSG_USER_NOT_FOUND);
        }

        return result; // Single return
    }

    public List<Usuario> getAllUsuarios() {
        List<Usuario> result = usuarioRepository.findAll();
        return result; // Single return
    }

    public Usuario updateUsuario(Long id, String newNombre, String newApellido, String newDni, String newCorreo, String newContrasenia, String newTelefono) {
        Usuario result = null;
        Usuario existingUsuario = getUsuario(id);

        existingUsuario.setNombre(newNombre);
        existingUsuario.setApellido(newApellido);
        existingUsuario.setDni(newDni);
        existingUsuario.setCorreo(newCorreo);
        existingUsuario.setContrasenia(newContrasenia);
        existingUsuario.setTelefono(newTelefono);
        result = usuarioRepository.update(existingUsuario);

        return result; // Single return
    }

    public boolean deleteUsuario(Long id) {
        boolean result = false;
        if (usuarioRepository.deleteById(id)) {
            result = true;
        } else {
            throw new UsuarioNotFoundException(Constants.MSG_USER_NOT_FOUND);
        }
        return result; // Single return
    }
}
