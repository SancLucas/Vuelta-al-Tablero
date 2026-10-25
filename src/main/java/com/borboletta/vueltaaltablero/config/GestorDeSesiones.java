package com.borboletta.vueltaaltablero.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/* Cierra las sesiones abiertas de una cuenta (por ejemplo, al darla de baja).
Está aparte para que el día de mañana también la use el administrador. */
@Component
@RequiredArgsConstructor
public class GestorDeSesiones{

    private final SessionRegistry sessionRegistry;

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    public void cerrarSesionesDe(String correo){
        sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> esDe(principal, correo))
                .flatMap(principal -> sessionRegistry.getAllSessions(principal, false).stream())
                .forEach(SessionInformation::expireNow);
    }

    private boolean esDe(Object principal, String correo){
        return principal instanceof UserDetails usuario
                && usuario.getUsername().equalsIgnoreCase(correo);
    }
}
