package com.borboletta.vueltaaltablero.exception;

import lombok.Getter;

@Getter
public class JuegoYaExisteException extends RuntimeException{
    private final String campo = "titulo";

    private JuegoYaExisteException(String mensaje){
        super(mensaje);
    }

    public static JuegoYaExisteException enElCatalogo(){
        return new JuegoYaExisteException("Ya hay un juego con ese título en el catálogo.");
    }

    /* Al editar un juego no se lo puede renombrar con el título de otro que está dado de baja:
    para recuperar ese, se usa "Agregar juego", que lo reactiva. */
    public static JuegoYaExisteException dadoDeBaja(){
        return new JuegoYaExisteException("Ese título es de un juego dado de baja. "
                + "Para recuperarlo, agregalo de nuevo desde \"Agregar juego\".");
    }
}
