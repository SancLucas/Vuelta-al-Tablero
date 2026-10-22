package com.borboletta.vueltaaltablero.dto;

import java.util.List;

/** Las críticas de un juego o de un ejemplar, con su puntuación promedio ya calculada. */
public record ResumenDeCriticas(List<DetalleDeCritica> criticas, double promedio){

    public static ResumenDeCriticas de(List<DetalleDeCritica> criticas){
        double promedio = criticas.stream()
                .mapToInt(DetalleDeCritica::puntuacion)
                .average()
                .orElse(0);
        return new ResumenDeCriticas(criticas, promedio);
    }

    public int cantidad(){
        return criticas.size();
    }

    public boolean hayCriticas(){
        return !criticas.isEmpty();
    }
}