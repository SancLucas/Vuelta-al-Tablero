package com.borboletta.vueltaaltablero.exception;

/**
 * Base de los "no existe lo que pediste" del catálogo. Un único manejador global
 * (ManejadorDeExcepciones) las traduce a una página 404.
 * UsuarioNoEncontradoException NO hereda de acá: significa otra cosa (ver ese manejador).
 */
public abstract class RecursoNoEncontradoException extends RuntimeException{

    protected RecursoNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
