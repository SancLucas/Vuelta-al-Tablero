package com.borboletta.vueltaaltablero.dto;

import java.time.LocalDate;

/** Lo que elige quien alquila. El ejemplar sale de la URL y quién alquila, de la sesión. */
public record DatosDeNuevoAlquiler(LocalDate fechaEntrega, LocalDate fechaDevolucion){ }
