package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.EstadoEjemplar;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/** Lo que se puede cargar al publicar y también al editar: lo comparten los dos formularios. */
@Getter
@Setter
public abstract class DatosDelEjemplarForm{

    @NotNull(message = "Elegí en qué estado está el ejemplar.")
    private EstadoEjemplar estado;

    private boolean disponibleAlquiler;

    @Digits(integer = 8, fraction = 2, message = "El precio admite hasta 8 enteros y 2 decimales.")
    private BigDecimal precioPorDia;

    /* El precio solo es obligatorio si el ejemplar se ofrece en alquiler. El error queda en la
    propiedad "precioPorDiaValido" y la plantilla lo muestra junto al campo del precio. */
    @AssertTrue(message = "Indicá un precio por día mayor a cero para ofrecerlo en alquiler.")
    public boolean isPrecioPorDiaValido(){
        return !disponibleAlquiler || (precioPorDia != null && precioPorDia.signum() > 0);
    }
}
