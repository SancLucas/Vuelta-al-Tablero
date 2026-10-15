package com.borboletta.vueltaaltablero.exception;

import lombok.Getter;

@Getter
public class UsuarioYaRegistradoException extends RuntimeException {
    private final String campo;

    public UsuarioYaRegistradoException(String campo, String mensaje) { 
        super(mensaje);
        this.campo = campo; //usamos campo genérico porque podemos reutilizar con dni y con correo
    }
}
