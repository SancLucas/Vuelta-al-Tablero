package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.EstadoEjemplar;
import java.math.BigDecimal;

/** Solo lo que se puede modificar de un ejemplar: ni el juego ni el dueño cambian. */
public record DatosDeEdicionDeEjemplar(EstadoEjemplar estado, boolean disponibleAlquiler,
                                       BigDecimal precioPorDia){ }
