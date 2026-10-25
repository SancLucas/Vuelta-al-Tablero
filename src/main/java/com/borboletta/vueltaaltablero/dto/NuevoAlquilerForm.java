package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.Alquiler;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/* Lo que se completa al alquilar un ejemplar. Acá solo se exige que haya fechas: que la
duración y la anticipación sean válidas lo decide Alquiler (una sola fuente de verdad).
Los métodos de abajo solo le pasan esos límites a la plantilla para acotar los campos de fecha. */
@Getter
@Setter
public class NuevoAlquilerForm{

    @NotNull(message = "Elegí la fecha de entrega.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)   // el <input type="date"> envía aaaa-mm-dd
    private LocalDate fechaEntrega;

    @NotNull(message = "Elegí la fecha de devolución.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaDevolucion;

    public LocalDate entregaMinima(){
        return LocalDate.now();
    }

    public LocalDate entregaMaxima(){
        return LocalDate.now().plusDays(Alquiler.ANTELACION_MAXIMA_DIAS);
    }

    public int diasMinimos(){
        return Alquiler.DIAS_MINIMOS;
    }

    public int diasMaximos(){
        return Alquiler.DIAS_MAXIMOS;
    }

    public DatosDeNuevoAlquiler aDatosDeNuevoAlquiler(){
        return new DatosDeNuevoAlquiler(fechaEntrega, fechaDevolucion);
    }
}
