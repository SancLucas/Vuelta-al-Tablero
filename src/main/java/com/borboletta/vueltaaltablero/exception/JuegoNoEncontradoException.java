package com.borboletta.vueltaaltablero.exception;

public class JuegoNoEncontradoException extends RecursoNoEncontradoException{

    public JuegoNoEncontradoException(Long id){
        super("No existe un juego con el id " + id + " en el catálogo.");
    }
}
