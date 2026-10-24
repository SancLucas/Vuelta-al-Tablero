package com.borboletta.vueltaaltablero.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.borboletta.vueltaaltablero.exception.OperacionNoPermitidaException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class UsuarioBloqueoTest{

    private Usuario ana(){
        return Usuario.registrar("Ana", "Gómez", "30111222", "ana@mail.com", "1155550000", "hash");
    }

    @Test
    void bloquearMarcaLaFechaYSeLeEntregaALaSeguridad(){
        Usuario ana = ana();

        ana.bloquear();

        assertThat(ana.getFechaBloqueo()).isNotNull();
        assertThat(ana.credenciales().bloqueado()).isTrue();
    }

    @Test
    void desbloquearLevantaElBloqueo(){
        Usuario ana = ana();
        ana.bloquear();

        ana.desbloquear();

        assertThat(ana.getFechaBloqueo()).isNull();
        assertThat(ana.credenciales().bloqueado()).isFalse();
    }

    @Test
    void unAdministradorNoPuedeSerBloqueado(){
        Usuario admin = ana();
        ReflectionTestUtils.setField(admin, "rolAdmin", true);

        assertThatThrownBy(admin::bloquear).isInstanceOf(OperacionNoPermitidaException.class);
        assertThat(admin.getFechaBloqueo()).isNull();
    }

    @Test
    void volverARegistrarseNoLevantaElBloqueo(){
        Usuario ana = ana();
        ana.bloquear();
        ana.darDeBaja();

        ana.reactivar("Ana", "Gómez", "1155550000", "hash-nuevo");

        assertThat(ana.getFechaBaja()).isNull();
        assertThat(ana.credenciales().bloqueado()).isTrue();
    }
}
