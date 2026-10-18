package com.borboletta.vueltaaltablero.dto;

/** Lo que el servicio necesita para crear o actualizar un juego junto con su ficha técnica. */
public record DatosDeJuego(String titulo, String categoria, String editorial,
                           int minJugadores, int maxJugadores, int minEdad,
                           String descripcion, int anioPublicacion){ }
