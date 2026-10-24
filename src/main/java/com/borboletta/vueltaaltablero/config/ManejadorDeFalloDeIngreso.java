package com.borboletta.vueltaaltablero.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Qué hacer cuando falla un ingreso: una cuenta bloqueada vuelve al formulario con su propio
 * aviso (?bloqueado); cualquier otro motivo, con el mensaje genérico (?error), que no revela
 * si el correo existe.
 */
public class ManejadorDeFalloDeIngreso implements AuthenticationFailureHandler{

    private final String rutaIngreso;

    public ManejadorDeFalloDeIngreso(String rutaIngreso){
        this.rutaIngreso = rutaIngreso;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException excepcion) throws IOException{
        String motivo = excepcion instanceof LockedException ? "bloqueado" : "error";
        response.sendRedirect(request.getContextPath() + rutaIngreso + "?" + motivo);
    }
}
