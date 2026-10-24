package com.borboletta.vueltaaltablero.exception;

/**
 * El administrador pidió una cuenta ajena que no existe. Se traduce a una página 404.
 * No es UsuarioNoEncontradoException: esa significa "la cuenta de quien tiene la sesión
 * ya no existe" y cierra la sesión. Acá no corresponde cerrarle la sesión al administrador.
 */
public class CuentaNoEncontradaException extends RecursoNoEncontradoException{

    public CuentaNoEncontradaException(Long nro){
        super("No existe la cuenta " + nro + ".");
    }
}
