package com.borboletta.vueltaaltablero.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Getter;
import java.time.Year;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirQue;
import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirTexto;

/* La ficha técnica es PARTE de un Juego: nace, se modifica y vive con él.
Por eso no tiene repositorio propio: se accede siempre a través de Juego. */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // lo exige JPA; nadie más lo usa
public class FichaTecnica{

    private static final int ANIO_MINIMO = 1900; // mismo límite que el CHECK de la base

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int minJugadores;
    private int maxJugadores;
    private int minEdad;
    private String descripcion;
    private int anioPublicacion;

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    private FichaTecnica(int minJugadores, int maxJugadores, int minEdad,
                         String descripcion, int anioPublicacion){
        asignar(minJugadores, maxJugadores, minEdad, descripcion, anioPublicacion);
    }

    public static FichaTecnica crear(int minJugadores, int maxJugadores, int minEdad,
                                     String descripcion, int anioPublicacion){
        return new FichaTecnica(minJugadores, maxJugadores, minEdad, descripcion, anioPublicacion);
    }

    /* Tell, Don't Ask: se le dice a la ficha que se actualice; ella valida sus reglas. */
    public void actualizar(int minJugadores, int maxJugadores, int minEdad,
                           String descripcion, int anioPublicacion){
        asignar(minJugadores, maxJugadores, minEdad, descripcion, anioPublicacion);
    }

    private void asignar(int minJugadores, int maxJugadores, int minEdad,
                         String descripcion, int anioPublicacion){
        exigirQue(minJugadores > 0, "Tiene que poder jugar al menos una persona.");
        exigirQue(maxJugadores >= minJugadores,
                "El máximo de jugadores no puede ser menor que el mínimo.");
        exigirQue(minEdad >= 0, "La edad mínima no puede ser negativa.");
        exigirQue(anioPublicacion >= ANIO_MINIMO && anioPublicacion <= Year.now().getValue(),
                "El año de publicación tiene que estar entre " + ANIO_MINIMO + " y el año actual.");
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.minEdad = minEdad;
        this.descripcion = exigirTexto(descripcion, "La descripción es obligatoria.");
        this.anioPublicacion = anioPublicacion;
    }
}