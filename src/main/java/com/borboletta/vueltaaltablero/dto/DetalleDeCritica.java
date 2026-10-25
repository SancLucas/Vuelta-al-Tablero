package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.Opinion;
import com.borboletta.vueltaaltablero.entity.Usuario;

/* "autor" es solo el nombre de pila: las listas públicas no exponen más datos de las personas. */
public record DetalleDeCritica(String autor, int puntuacion, String comentario){

    public static DetalleDeCritica desde(Usuario autor, Opinion opinion){
        return new DetalleDeCritica(autor.getNombre(), opinion.getPuntuacion(),
                opinion.getComentario());
    }
}
