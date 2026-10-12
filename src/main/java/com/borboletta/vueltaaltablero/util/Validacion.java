package com.borboletta.vueltaaltablero.util;

public class Validacion {

    private Validacion() {
    }

    public static String validarCadena(String cadena, String mensaje) {
        if(cadena == null || cadena.trim().isEmpty()){
            throw new IllegalArgumentException(mensaje);
        }
        return cadena;
    }

    public static double validarMayorACero(double n) {
        if(n<=0)
            throw new RuntimeException("El valor "+ n+ " debe ser mayor a cero");
        return n;
    }

    public static double validarEntre(double n, double min, double max) {
        if(n<min||n>max)
            throw new RuntimeException("El valor "+ n+ " debe estar entre "+ min+" y "+ max);
        return n;
    }
}
