package com.borboletta.vueltaaltablero.exception;

/* Quien no alquiló y devolvió el juego o el ejemplar no puede criticarlo. */
public class CriticaNoPermitidaException extends RuntimeException{

    public CriticaNoPermitidaException(String mensaje){
        super(mensaje);
    }
}