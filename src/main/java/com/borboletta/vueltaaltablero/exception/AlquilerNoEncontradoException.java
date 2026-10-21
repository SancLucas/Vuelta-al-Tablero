package com.borboletta.vueltaaltablero.exception;

/* También se usa cuando el alquiler existe pero es de un ejemplar ajeno: así no se revela. */
public class AlquilerNoEncontradoException extends RecursoNoEncontradoException{

    public AlquilerNoEncontradoException(Long id){
        super("No existe un alquiler con el id " + id + " entre los préstamos de tus ejemplares.");
    }
}