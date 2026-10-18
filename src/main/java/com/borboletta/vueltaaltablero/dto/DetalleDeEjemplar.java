package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.Ejemplar;
import com.borboletta.vueltaaltablero.entity.EstadoEjemplar;

/* "propietario" es solo el nombre de pila: la lista pública no expone más datos de las personas. */
public record DetalleDeEjemplar(Long id, Long idJuego, String tituloJuego, EstadoEjemplar estado,
                                boolean disponibleAlquiler, String propietario){

    public static DetalleDeEjemplar desde(Ejemplar ejemplar){
        return new DetalleDeEjemplar(ejemplar.getId(), ejemplar.getJuego().getId(),
                ejemplar.getJuego().getTitulo(), ejemplar.getEstado(),
                ejemplar.isDisponibleAlquiler(), ejemplar.getPropietario().getNombre());
    }
}
