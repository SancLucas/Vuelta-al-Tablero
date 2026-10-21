package com.borboletta.vueltaaltablero.entity;

import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirQue;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Lo que alguien opina de un juego o de un ejemplar. No tiene tabla propia: sus dos campos
 * viven en las columnas comentario y puntuacion de critica_juego y critica_ejemplar.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED) // lo exige JPA
public class Opinion{

    public static final int PUNTUACION_MINIMA = 1;   // chk_critica_*_puntos del DDL
    public static final int PUNTUACION_MAXIMA = 5;
    public static final int LARGO_MAXIMO_COMENTARIO = 200;

    private String comentario;   // opcional: la columna admite NULL
    private int puntuacion;

    private Opinion(String comentario, int puntuacion){
        exigirQue(puntuacion >= PUNTUACION_MINIMA && puntuacion <= PUNTUACION_MAXIMA,
                "La puntuación va de " + PUNTUACION_MINIMA + " a " + PUNTUACION_MAXIMA + ".");
        this.comentario = normalizar(comentario);
        this.puntuacion = puntuacion;
    }

    public static Opinion de(String comentario, int puntuacion){
        return new Opinion(comentario, puntuacion);
    }

    /* Un comentario en blanco se guarda como "sin comentario" (NULL). */
    private static String normalizar(String comentario){
        String limpio = (comentario == null || comentario.isBlank()) ? null : comentario.trim();
        exigirQue(limpio == null || limpio.length() <= LARGO_MAXIMO_COMENTARIO,
                "El comentario admite hasta " + LARGO_MAXIMO_COMENTARIO + " caracteres.");
        return limpio;
    }
}