package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.Alquiler;
import com.borboletta.vueltaaltablero.entity.Ejemplar;
import java.math.BigDecimal;
import java.time.LocalDate;

/* inquilino y propietario son solo el nombre de pila, igual que en DetalleDeEjemplar.
"telefonoPropietario" solo viene mientras el alquiler está en curso (para coordinar la entrega
y la devolución) y es null después. */
public record DetalleDeAlquiler(Long id, Long idEjemplar, Long idJuego, String tituloJuego,
                                String inquilino, String propietario, String telefonoPropietario,
                                BigDecimal precio, LocalDate fechaEntrega,
                                LocalDate fechaDevolucionPrevista, long dias,
                                LocalDate fechaDevolucion, boolean enCurso){

    public static DetalleDeAlquiler desde(Alquiler alquiler){
        Ejemplar ejemplar = alquiler.getEjemplar();
        String telefono = alquiler.estaEnCurso() ? ejemplar.getPropietario().getTelefono() : null;
        return new DetalleDeAlquiler(alquiler.getId(), ejemplar.getId(),
                ejemplar.getJuego().getId(), ejemplar.getJuego().getTitulo(),
                alquiler.getInquilino().getNombre(), ejemplar.getPropietario().getNombre(),
                telefono, alquiler.getPrecio(), alquiler.getFechaEntrega(),
                alquiler.getFechaDevolucionPrevista(), alquiler.dias(),
                alquiler.getFechaDevolucion(), alquiler.estaEnCurso());
    }
}