package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.EstadoEjemplar;
import java.math.BigDecimal;

public record DatosDeNuevoEjemplar(Long idJuego, EstadoEjemplar estado,
                                   boolean disponibleAlquiler, BigDecimal precioPorDia){ }