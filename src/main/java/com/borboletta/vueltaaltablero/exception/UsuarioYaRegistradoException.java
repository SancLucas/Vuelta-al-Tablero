package com.borboletta.vueltaaltablero.exception;

import lombok.Getter;

@Getter
public class UsuarioYaRegistradoException extends RuntimeException{
    private final String campo;

    public UsuarioYaRegistradoException(String campo, String mensaje){
        super(mensaje);
        this.campo = campo; //usamos un campo genérico para así reutilizar el metodo con dni y correo.
    }
}
