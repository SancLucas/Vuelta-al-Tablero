package com.borboletta.vueltaaltablero.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class AutenticacionServiceTest{

    @Mock private UsuarioRepository usuarioRepository;
    @InjectMocks private AutenticacionService servicio;

    private Usuario ana(){
        return Usuario.registrar("Ana", "Gómez", "30111222", "ana@mail.com", "1155550000", "hash");
    }

    @Test
    void unaCuentaNormalPuedeIngresar(){
        when(usuarioRepository.findByCorreoIgnoreCaseAndFechaBajaIsNull("ana@mail.com"))
                .thenReturn(Optional.of(ana()));

        UserDetails detalles = servicio.loadUserByUsername("ana@mail.com");

        assertThat(detalles.isAccountNonLocked()).isTrue();
        assertThat(detalles.getPassword()).isEqualTo("hash");
    }

    @Test
    void unaCuentaBloqueadaLeLlegaALaSeguridadComoBloqueada(){
        Usuario ana = ana();
        ana.bloquear();
        when(usuarioRepository.findByCorreoIgnoreCaseAndFechaBajaIsNull("ana@mail.com"))
                .thenReturn(Optional.of(ana));

        UserDetails detalles = servicio.loadUserByUsername("ana@mail.com");

        assertThat(detalles.isAccountNonLocked()).isFalse();
    }

    @Test
    void rechazaCuentasInexistentesODadasDeBaja(){
        when(usuarioRepository.findByCorreoIgnoreCaseAndFechaBajaIsNull("ana@mail.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.loadUserByUsername("ana@mail.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
