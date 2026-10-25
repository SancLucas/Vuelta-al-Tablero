package com.borboletta.vueltaaltablero.entity;

import java.util.Arrays;
import java.util.Optional;

/* Categorías de juego. Tienen que coincidir con el CHECK chk_juego_categoria de la base
(ver migracion_v3.sql): si se agrega una acá, hay que agregarla también ahí.
"Todos los juegos" NO es una categoría: es el catálogo sin filtro. */
public enum CategoriaJuego{
    CLASICOS("Clásicos"),
    MODERNOS("Modernos"),
    ESTRATEGIA("Estrategia"),
    COOPERATIVOS("Cooperativos"),
    FAMILIARES("Familiares"),
    CARTAS("Cartas"),
    PARTY("Party"),
    OTROS("Otros");

    private final String etiqueta;

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    CategoriaJuego(String etiqueta){
        this.etiqueta = etiqueta;
    }

    /** Texto que se muestra al usuario y que se guarda en la base. */
    public String getEtiqueta(){
        return etiqueta;
    }

    public static CategoriaJuego desdeEtiqueta(String etiqueta){
        return Arrays.stream(values())
                .filter(categoria -> categoria.etiqueta.equalsIgnoreCase(etiqueta))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Categoría de juego desconocida: " + etiqueta));
    }

    /* Para el filtro del catálogo (?categoria=ESTRATEGIA).
    Un valor inventado en la URL no rompe nada: simplemente no filtra. */
    public static Optional<CategoriaJuego> buscarPorCodigo(String codigo){
        return Arrays.stream(values())
                .filter(categoria -> categoria.name().equalsIgnoreCase(codigo))
                .findFirst();
    }
}