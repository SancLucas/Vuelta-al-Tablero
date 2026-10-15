package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDeRegistro;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.exception.UsuarioYaRegistradoException;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodosLosUsuarios(){
        return usuarioRepository.findAll();
    }

    @Transactional
    public void registrar(DatosDeRegistro datos){
        validarQueNoExista(datos);
        Usuario nuevo = Usuario.registrar(
                datos.nombre(), datos.apellido(), datos.dni(),
                datos.correo(), datos.telefono(), datos.contrasenia());
        usuarioRepository.save(nuevo);
    }

    private void validarQueNoExista(DatosDeRegistro datos){
        if (usuarioRepository.existsByCorreoIgnoreCase(datos.correo())){
            throw new UsuarioYaRegistradoException("correo", "Ya existe una cuenta con ese correo.");
        }
        if (usuarioRepository.existsByDni(datos.dni())) {
            throw new UsuarioYaRegistradoException("dni", "Ya existe una cuenta con ese DNI.");
        }
    }
    
    /**
     * Autentica a un usuario basado en su correo y contraseña.
     * Respeta Single Return, utiliza Tell, Don't Ask y respeta la interfaz actual.
     */
    public Usuario autenticar(String correo, String contrasenia){
        Usuario usuarioAutenticado = null;
        
        // Buscamos el usuario filtrando la lista completa ya que la interfaz no se modifica
        Optional<Usuario> usuarioOpt = usuarioRepository.findAll().stream()
                .filter(u -> u.getCorreo() != null && u.getCorreo().equalsIgnoreCase(correo))
                .findFirst();

        if(usuarioOpt.isPresent()){
            Usuario usuarioEncontrado = usuarioOpt.get();
            if(usuarioEncontrado.verificarContrasenia(contrasenia)){
                usuarioAutenticado = usuarioEncontrado;
            }
        }
        return usuarioAutenticado;
    }
    
    public Usuario buscarPorDni(String dni) {
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