package com.borboletta.vueltaaltablero.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class JuegoTest{

    private Juego catan(){
        return Juego.crear("Catan", CategoriaJuego.ESTRATEGIA, "Devir",
                FichaTecnica.crear(3, 4, 10, "Comerciá y construí", 1995));
    }

    @Test
    void unJuegoNecesitaCategoria(){
        assertThatThrownBy(() -> Juego.crear("Catan", null, "Devir",
                FichaTecnica.crear(3, 4, 10, "Descripción", 1995)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void reactivarReutilizaElJuegoConLosDatosNuevos(){
        Juego juego = catan();
        juego.darDeBaja();

        juego.reactivar("Catan", CategoriaJuego.FAMILIARES, "Otra editorial");

        assertThat(juego.getFechaBaja()).isNull();
        assertThat(juego.getCategoria()).isEqualTo(CategoriaJuego.FAMILIARES);
        assertThat(juego.getEditorial()).isEqualTo("Otra editorial");
    }

    @Test
    void noSePuedeReactivarUnJuegoQueYaEstaEnElCatalogo(){
        Juego juego = catan();

        assertThatThrownBy(() -> juego.reactivar("Catan", CategoriaJuego.ESTRATEGIA, "Devir"))
                .isInstanceOf(IllegalStateException.class);
    }
}
