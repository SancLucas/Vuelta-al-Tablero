package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.dto.DatosDeEdicionUsuario;
import com.borboletta.vueltaaltablero.dto.DatosDePerfil;
import com.borboletta.vueltaaltablero.dto.DatosDeRegistroUsuario;
import com.borboletta.vueltaaltablero.dto.ResumenDeUsuario;

import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;

import com.borboletta.vueltaaltablero.exception.CuentaNoEncontradaException;
import com.borboletta.vueltaaltablero.exception.UsuarioNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.UsuarioYaRegistradoException;

import com.borboletta.vueltaaltablero.paginacion.Paginas;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService{

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventos;

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    /* Para el listado del administrador (incluye a las cuentas bloqueadas, para poder desbloquearlas). */
    @Transactional(readOnly = true)
    public Page<ResumenDeUsuario> obtenerTodosLosUsuarios(Pageable pedido){
        return Paginas.consultar(pedido, usuarioRepository::findAllByFechaBajaIsNullOrderByNombreAscApellidoAsc)
                .map(ResumenDeUsuario::desde);
    }

    /* Bloquea la cuenta y avisa (evento) para que se le cierren las sesiones abiertas. */
    @Transactional
    public void bloquear(Long nro){
        Usuario usuario = buscarPorNro(nro);
        usuario.bloquear();
        eventos.publishEvent(new UsuarioBloqueadoEvento(usuario.getCorreo()));
    }

    @Transactional
    public void desbloquear(Long nro){
        buscarPorNro(nro).desbloquear();
    }

        /* Si el correo y el DNI son de una cuenta dada de baja,
    se la reactiva; si no, se crea una cuenta nueva. */
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

        /* La cuenta que pide el administrador es AJENA: si no existe es un 404, y no hay que
    cerrarle la sesión a quien navega (por eso no es UsuarioNoEncontradoException). */
    private Usuario buscarPorNro(Long nro){
        return usuarioRepository.findByNroAndFechaBajaIsNull(nro)
                .orElseThrow(() -> new CuentaNoEncontradaException(nro));
    }

    private Usuario buscarPorCorreo(String correo){
        return usuarioRepository.findByCorreoIgnoreCaseAndFechaBajaIsNull(correo)
                .orElseThrow(() -> new UsuarioNoEncontradoException(correo));
    }
}