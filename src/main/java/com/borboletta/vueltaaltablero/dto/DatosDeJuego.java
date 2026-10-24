package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.CategoriaJuego;

/** Lo que el servicio necesita para crear o actualizar un juego junto con su ficha técnica. */
public record DatosDeJuego(String titulo, CategoriaJuego categoria, String editorial,
                           int minJugadores, int maxJugadores, int minEdad,
                           String descripcion, int anioPublicacion){ }
