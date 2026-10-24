package com.borboletta.vueltaaltablero.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.borboletta.vueltaaltablero.dto.ResumenDeUsuario;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.exception.CuentaNoEncontradaException;
import com.borboletta.vueltaaltablero.exception.OperacionNoPermitidaException;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceBloqueoTest{

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ApplicationEventPublisher eventos;
    @InjectMocks private UsuarioService servicio;

    private Usuario ana(){
        return Usuario.registrar("Ana", "Gómez", "30111222", "ana@mail.com", "1155550000", "hash");
    }

    @Test
    void bloquearMarcaLaCuentaYAvisaParaCerrarSusSesiones(){
        Usuario ana = ana();
        when(usuarioRepository.findByNroAndFechaBajaIsNull(7L)).thenReturn(Optional.of(ana));

        servicio.bloquear(7L);

        assertThat(ana.getFechaBloqueo()).isNotNull();
        verify(eventos).publishEvent(new UsuarioBloqueadoEvento("ana@mail.com"));
    }

    @Test
    void noBloqueaAUnAdministradorNiAvisaNada(){
        Usuario admin = ana();
        ReflectionTestUtils.setField(admin, "rolAdmin", true);
        when(usuarioRepository.findByNroAndFechaBajaIsNull(1L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> servicio.bloquear(1L)).isInstanceOf(OperacionNoPermitidaException.class);
        verifyNoInteractions(eventos);
    }

    @Test
    void bloquearUnaCuentaQueNoExisteEsUn404YNoCierraLaSesionDeNadie(){
        when(usuarioRepository.findByNroAndFechaBajaIsNull(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.bloquear(99L)).isInstanceOf(CuentaNoEncontradaException.class);
        verifyNoInteractions(eventos);
    }

    @Test
    void desbloquearLevantaElBloqueo(){
        Usuario ana = ana();
        ana.bloquear();
        when(usuarioRepository.findByNroAndFechaBajaIsNull(7L)).thenReturn(Optional.of(ana));

        servicio.desbloquear(7L);

        assertThat(ana.getFechaBloqueo()).isNull();
    }

    @Test
    void elListadoDelAdministradorIncluyeElEstadoDeBloqueo(){
        Usuario ana = ana();
        ana.bloquear();
        when(usuarioRepository.findAllByFechaBajaIsNullOrderByNombreAscApellidoAsc())
                .thenReturn(List.of(ana));

        List<ResumenDeUsuario> usuarios = servicio.obtenerTodosLosUsuarios();

        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).bloqueado()).isTrue();
        assertThat(usuarios.get(0).correo()).isEqualTo("ana@mail.com");
    }
}
