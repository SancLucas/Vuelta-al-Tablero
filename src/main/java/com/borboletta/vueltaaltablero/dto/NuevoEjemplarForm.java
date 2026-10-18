package com.borboletta.vueltaaltablero.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Al publicar se elige además de qué juego es la copia. */
@Getter
@Setter
public class NuevoEjemplarForm extends DatosDelEjemplarForm{

    @NotNull(message = "Elegí el juego del que tenés una copia.")
    private Long idJuego;

    public DatosDeNuevoEjemplar aDatosDeNuevoEjemplar(){
        return new DatosDeNuevoEjemplar(idJuego, getEstado(), isDisponibleAlquiler());
    }
}
