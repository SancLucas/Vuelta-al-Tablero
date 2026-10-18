package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.EstadoEjemplar;

public record DatosDeNuevoEjemplar(Long idJuego, EstadoEjemplar estado, boolean disponibleAlquiler){ }
