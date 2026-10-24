package com.borboletta.vueltaaltablero.config;

import com.borboletta.vueltaaltablero.service.UsuarioBloqueadoEvento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/* Cuando se bloquea una cuenta, se le cierran las sesiones que tuviera abiertas. Se ejecuta
DESPUÉS de confirmar el bloqueo en la base. UsuarioService no sabe nada de sesiones. */
@Component
@RequiredArgsConstructor
public class CierreDeSesionesAlBloquear{

    private final GestorDeSesiones gestorDeSesiones;

    @TransactionalEventListener
    public void alBloquearse(UsuarioBloqueadoEvento evento){
        gestorDeSesiones.cerrarSesionesDe(evento.correo());
    }
}
