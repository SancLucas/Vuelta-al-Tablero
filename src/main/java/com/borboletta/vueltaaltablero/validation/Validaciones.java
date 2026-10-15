package com.borboletta.vueltaaltablero.validation;

public final class Validaciones{

    private Validaciones() { }

    public static String exigirTexto(String valor, String mensaje){
        if(valor == null || valor.isBlank()){
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }
}
