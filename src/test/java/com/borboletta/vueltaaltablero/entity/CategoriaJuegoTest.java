package com.borboletta.vueltaaltablero.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CategoriaJuegoTest{

    @Test
    void buscaPorCodigoSinDistinguirMayusculas(){
        assertThat(CategoriaJuego.buscarPorCodigo("estrategia")).contains(CategoriaJuego.ESTRATEGIA);
        assertThat(CategoriaJuego.buscarPorCodigo("PARTY")).contains(CategoriaJuego.PARTY);
    }

    @Test
    void unCodigoInventadoONuloNoFiltra(){
        assertThat(CategoriaJuego.buscarPorCodigo("inventada")).isEmpty();
        assertThat(CategoriaJuego.buscarPorCodigo(null)).isEmpty();
    }

    @Test
    void laCategoriaSeGuardaConSuEtiquetaYSeLeeDeVuelta(){
        CategoriaJuegoConverter conversor = new CategoriaJuegoConverter();

        assertThat(conversor.convertToDatabaseColumn(CategoriaJuego.CLASICOS)).isEqualTo("Clásicos");
        assertThat(conversor.convertToEntityAttribute("Clásicos")).isEqualTo(CategoriaJuego.CLASICOS);
        assertThat(conversor.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    void ningunaEtiquetaSuperaLaColumnaDeLaBase(){
        for (CategoriaJuego categoria : CategoriaJuego.values()){
            assertThat(categoria.getEtiqueta().length()).isLessThanOrEqualTo(25);   // VARCHAR(25)
        }
    }
}
