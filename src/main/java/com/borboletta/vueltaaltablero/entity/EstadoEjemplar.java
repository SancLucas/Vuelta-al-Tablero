package com.borboletta.vueltaaltablero.entity;

import static com.borboletta.vueltaaltablero.entity.EstadoEjemplar.values;
import java.util.Arrays;

/** Estados permitidos por el CHECK chk_ejemplar_estado de la tabla ejemplar. */
public enum EstadoEjemplar{
    NUEVO("Nuevo"),
    EXCELENTE("Excelente"),
    BUENO("Bueno"),
    DANIADO("Dañado"),
    INCOMPLETO("Incompleto");

    private final String etiqueta;

    EstadoEjemplar(String etiqueta){
        this.etiqueta = etiqueta;
    }

    /** Texto que se muestra al usuario y que se guarda en la base. */
    public String getEtiqueta(){
        return etiqueta;
    }

    public static EstadoEjemplar desdeEtiqueta(String etiqueta){
        return Arrays.stream(values())
                .filter(estado -> estado.etiqueta.equalsIgnoreCase(etiqueta))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Estado de ejemplar desconocido: " + etiqueta));
    }
}