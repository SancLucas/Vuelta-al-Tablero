package com.borboletta.vueltaaltablero.exception;

/* Una regla de negocio impide la operación pedida (por ejemplo, bloquear a un administrador). */
public class OperacionNoPermitidaException extends RuntimeException{

    public OperacionNoPermitidaException(String mensaje){
        super(mensaje);
    }
}