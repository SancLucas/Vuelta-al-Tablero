package com.borboletta.vueltaaltablero.exception;

public class UsuarioNoEncontradoException extends RuntimeException{

    public UsuarioNoEncontradoException(String correo){
        super("No existe un usuario con el correo " + correo + ".");
    }
}
