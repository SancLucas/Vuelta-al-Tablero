package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.Ejemplar;
import com.borboletta.vueltaaltablero.entity.EstadoEjemplar;
import java.math.BigDecimal;

/* "propietario" es solo el nombre de pila: la lista pública no expone más datos de las personas.
"precioPorDia" es null cuando el ejemplar no se ofrece en alquiler.
"alquilado" dice si hoy tiene un alquiler abierto (sin devolución registrada). */
public record DetalleDeEjemplar(Long id, Long idJuego, String tituloJuego, EstadoEjemplar estado,
                                boolean disponibleAlquiler, BigDecimal precioPorDia,
                                boolean alquilado, String propietario){

    public static DetalleDeEjemplar desde(Ejemplar ejemplar, boolean alquilado){
        return new DetalleDeEjemplar(ejemplar.getId(), ejemplar.getJuego().getId(),
                ejemplar.getJuego().getTitulo(), ejemplar.getEstado(),
                ejemplar.isDisponibleAlquiler(), ejemplar.getPrecioPorDia(), alquilado,
                ejemplar.getPropietario().getNombre());
    }
}
