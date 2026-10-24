package com.borboletta.vueltaaltablero.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;

class ManejadorDeFalloDeIngresoTest{

    private final ManejadorDeFalloDeIngreso manejador = new ManejadorDeFalloDeIngreso("/usuario/ingreso");

    @Test
    void unaCuentaBloqueadaVuelveAlIngresoConSuPropioAviso() throws IOException{
        MockHttpServletResponse respuesta = new MockHttpServletResponse();

        manejador.onAuthenticationFailure(new MockHttpServletRequest(), respuesta,
                new LockedException("bloqueada"));

        assertThat(respuesta.getRedirectedUrl()).isEqualTo("/usuario/ingreso?bloqueado");
    }

    @Test
    void cualquierOtroFalloUsaElMensajeGenerico() throws IOException{
        MockHttpServletResponse respuesta = new MockHttpServletResponse();

        manejador.onAuthenticationFailure(new MockHttpServletRequest(), respuesta,
                new BadCredentialsException("mal"));

        assertThat(respuesta.getRedirectedUrl()).isEqualTo("/usuario/ingreso?error");
    }
}
