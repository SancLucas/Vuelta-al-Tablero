package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.CategoriaJuego;
import com.borboletta.vueltaaltablero.entity.FichaTecnica;
import com.borboletta.vueltaaltablero.entity.Juego;

/** Lo que las vistas pueden ver de un juego: las plantillas nunca tocan la entidad. */
public record DetalleDeJuego(Long id, String titulo, CategoriaJuego categoria, String editorial,
                             int minJugadores, int maxJugadores, int minEdad,
                             String descripcion, int anioPublicacion){

    public static DetalleDeJuego desde(Juego juego){
        FichaTecnica ficha = juego.getFichaTecnica();
        return new DetalleDeJuego(juego.getId(), juego.getTitulo(), juego.getCategoria(),
                juego.getEditorial(), ficha.getMinJugadores(), ficha.getMaxJugadores(),
                ficha.getMinEdad(), ficha.getDescripcion(), ficha.getAnioPublicacion());
    }

    /** Texto listo para mostrar: "2 a 4 jugadores", "2 jugadores" o "1 jugador". */
    public String jugadores(){
        String texto = minJugadores + " a " + maxJugadores + " jugadores";
        if (minJugadores == maxJugadores){
            texto = minJugadores == 1 ? "1 jugador" : minJugadores + " jugadores";
        }
        return texto;
    }
}
