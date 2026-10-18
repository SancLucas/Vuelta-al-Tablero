package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDeEdicionUsuario;
import com.borboletta.vueltaaltablero.dto.DatosDePerfil;
import com.borboletta.vueltaaltablero.dto.DatosDeRegistroUsuario;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.exception.UsuarioNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.UsuarioYaRegistradoException;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService{

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodosLosUsuarios(){
        return usuarioRepository.findAllByFechaBajaIsNull();
    }

    /* Si el correo y el DNI son de una cuenta dada de baja, se la reactiva;
    si no, se crea una cuenta nueva. */
    @Transactional
    public void registrar(DatosDeRegistroUsuario datos){
        usuarioRepository
                .findByCorreoIgnoreCaseAndDniAndFechaBajaIsNotNull(datos.correo(), datos.dni())
                .ifPresentOrElse(
                        cuentaDeBaja -> reactivar(cuentaDeBaja, datos),
                        () -> crearCuentaNueva(datos));
    }

    @Transactional(readOnly = true)
    public DatosDePerfil obtenerPerfil(String correo){
        return DatosDePerfil.desde(buscarPorCorreo(correo));
    }

    @Transactional
    public void actualizar(String correo, DatosDeEdicionUsuario datos){
        Usuario usuario = buscarPorCorreo(correo);
        usuario.actualizarDatosPersonales(datos.nombre(), datos.apellido(), datos.telefono());
    }

    @Transactional
    public void darDeBaja(String correo){
        buscarPorCorreo(correo).darDeBaja();
    }

    private void reactivar(Usuario cuentaDeBaja, DatosDeRegistroUsuario datos){
        cuentaDeBaja.reactivar(datos.nombre(), datos.apellido(), datos.telefono(),
                passwordEncoder.encode(datos.contrasenia()));
    }

    private void crearCuentaNueva(DatosDeRegistroUsuario datos){
        validarQueNoExista(datos);
        Usuario nuevo = Usuario.registrar(
                datos.nombre(), datos.apellido(), datos.dni(),
                datos.correo(), datos.telefono(), passwordEncoder.encode(datos.contrasenia()));
        usuarioRepository.save(nuevo);
    }

    private void validarQueNoExista(DatosDeRegistroUsuario datos){
        if (usuarioRepository.existsByCorreoIgnoreCase(datos.correo())){
            throw new UsuarioYaRegistradoException("correo", "Ya existe una cuenta con ese correo.");
        }
        if (usuarioRepository.existsByDni(datos.dni())){
            throw new UsuarioYaRegistradoException("dni", "Ya existe una cuenta con ese DNI.");
        }
    }

    private Usuario buscarPorCorreo(String correo){
        return usuarioRepository.findByCorreoIgnoreCaseAndFechaBajaIsNull(correo)
                .orElseThrow(() -> new UsuarioNoEncontradoException(correo));
    }
}