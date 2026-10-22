package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.Opinion;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** Sirve para escribir y para corregir una crítica, tanto de un juego como de un ejemplar. */
@Getter
@Setter
public class CriticaForm{

    // Integer (y no int) para distinguir "no eligió nada" de un valor: el @NotNull lo detecta.
    @NotNull(message = "Elegí una puntuación.")
    @Min(value = Opinion.PUNTUACION_MINIMA, message = "La puntuación va de "
            + Opinion.PUNTUACION_MINIMA + " a " + Opinion.PUNTUACION_MAXIMA + ".")
    @Max(value = Opinion.PUNTUACION_MAXIMA, message = "La puntuación va de "
            + Opinion.PUNTUACION_MINIMA + " a " + Opinion.PUNTUACION_MAXIMA + ".")
    private Integer puntuacion;

    @Size(max = Opinion.LARGO_MAXIMO_COMENTARIO,
            message = "El comentario admite hasta " + Opinion.LARGO_MAXIMO_COMENTARIO + " caracteres.")
    private String comentario;

    /* Para precargar el formulario con la crítica que la persona ya dejó. */
    public static CriticaForm desde(DetalleDeCritica critica){
        CriticaForm form = new CriticaForm();
        form.setPuntuacion(critica.puntuacion());
        form.setComentario(critica.comentario());
        return form;
    }

    public DatosDeCritica aDatosDeCritica(){
        return new DatosDeCritica(comentario, puntuacion);
    }
}