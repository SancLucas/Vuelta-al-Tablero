package com.borboletta.vueltaaltablero.service;

    /* Aviso de que una cuenta acaba de bloquearse. El servicio solo lo publica:
quién reacciona (hoy, cerrar sus sesiones abiertas) no es asunto suyo. */
public record UsuarioBloqueadoEvento(String correo){ }