package com.borboletta.vueltaaltablero.exception;

import lombok.Getter;

@Getter
public class JuegoYaExisteException extends RuntimeException{
    private final String campo = "titulo";

    public JuegoYaExisteException(){
        super("Ya hay un juego con ese título en el catálogo.");
    }
}
