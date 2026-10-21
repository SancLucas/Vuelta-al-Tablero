package com.borboletta.vueltaaltablero.exception;

/* Regla que depende de la base (otro alquiler abierto), por eso no la valida la entidad. */
public class EjemplarYaAlquiladoException extends RuntimeException{

    public EjemplarYaAlquiladoException(){
        super("Este ejemplar ya está alquilado: hay que esperar a que lo devuelvan.");
    }

    /* Para otros casos en que el alquiler abierto impide algo (por ejemplo, quitar el ejemplar). */
    public EjemplarYaAlquiladoException(String mensaje){
        super(mensaje);
    }
}