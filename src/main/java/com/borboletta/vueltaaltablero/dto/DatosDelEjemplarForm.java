package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.EstadoEjemplar;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Lo que se puede cargar al publicar y también al editar: lo comparten los dos formularios. */
@Getter
@Setter
public abstract class DatosDelEjemplarForm{

    @NotNull(message = "Elegí en qué estado está el ejemplar.")
    private EstadoEjemplar estado;

    private boolean disponibleAlquiler;
}
