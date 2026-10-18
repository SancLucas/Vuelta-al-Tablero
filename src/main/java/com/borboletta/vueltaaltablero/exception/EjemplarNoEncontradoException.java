package com.borboletta.vueltaaltablero.exception;

/* Se usa también cuando el ejemplar existe pero es de otra persona: así no se revela que existe. */
public class EjemplarNoEncontradoException extends RecursoNoEncontradoException{

    public EjemplarNoEncontradoException(Long id){
        super("No existe un ejemplar con el id " + id + " en tu colección.");
    }
}
