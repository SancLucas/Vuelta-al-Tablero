package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.Alquiler;
import com.borboletta.vueltaaltablero.entity.Ejemplar;
import java.math.BigDecimal;
import java.time.LocalDate;

/* inquilino y propietario son solo el nombre de pila, igual que en DetalleDeEjemplar.
Los teléfonos de las dos partes solo vienen mientras el alquiler está abierto (para coordinar la
entrega y la devolución) y son null después.
"empezado" es false para un alquiler pedido para una fecha futura (todavía "reservado"). */
public record DetalleDeAlquiler(Long id, Long idEjemplar, Long idJuego, String tituloJuego,
                                String inquilino, String propietario,
                                String telefonoPropietario, String telefonoInquilino,
                                BigDecimal precio, LocalDate fechaEntrega,
                                LocalDate fechaDevolucionPrevista, long dias,
                                LocalDate fechaDevolucion, boolean enCurso, boolean empezado){

    public static DetalleDeAlquiler desde(Alquiler alquiler){
        Ejemplar ejemplar = alquiler.getEjemplar();
        boolean abierto = alquiler.estaEnCurso();
        String telefonoPropietario = abierto ? ejemplar.getPropietario().getTelefono() : null;
        String telefonoInquilino = abierto ? alquiler.getInquilino().getTelefono() : null;
        return new DetalleDeAlquiler(alquiler.getId(), ejemplar.getId(),
                ejemplar.getJuego().getId(), ejemplar.getJuego().getTitulo(),
                alquiler.getInquilino().getNombre(), ejemplar.getPropietario().getNombre(),
                telefonoPropietario, telefonoInquilino, alquiler.getPrecio(),
                alquiler.getFechaEntrega(), alquiler.getFechaDevolucionPrevista(), alquiler.dias(),
                alquiler.getFechaDevolucion(), abierto, alquiler.yaEmpezo());
    }
}