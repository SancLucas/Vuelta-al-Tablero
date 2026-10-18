package com.borboletta.vueltaaltablero.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Year;
import lombok.Getter;
import lombok.Setter;

/* Un solo formulario para el juego y su ficha técnica, y el mismo para crear y para editar.
Los números son Integer (no int) para que un campo vacío llegue como null y lo atrape @NotNull. */
@Getter
@Setter
public class JuegoForm{

    @NotBlank(message = "Ingresá el título.")
    @Size(max = 45, message = "Máximo 45 caracteres.")
    private String titulo;

    @NotBlank(message = "Ingresá la categoría.")
    @Size(max = 25, message = "Máximo 25 caracteres.")
    private String categoria;

    @NotBlank(message = "Ingresá la editorial.")
    @Size(max = 25, message = "Máximo 25 caracteres.")
    private String editorial;

    @NotNull(message = "Indicá el mínimo de jugadores.")
    @Min(value = 1, message = "El mínimo es 1 jugador.")
    private Integer minJugadores;

    @NotNull(message = "Indicá el máximo de jugadores.")
    @Min(value = 1, message = "El máximo es 1 jugador como mínimo.")
    private Integer maxJugadores;

    @NotNull(message = "Indicá la edad mínima.")
    @Min(value = 0, message = "La edad no puede ser negativa.")
    private Integer minEdad;

    @NotBlank(message = "Escribí una descripción.")
    @Size(max = 200, message = "Máximo 200 caracteres.")
    private String descripcion;

    @NotNull(message = "Indicá el año de publicación.")
    @Min(value = 1900, message = "El año tiene que ser 1900 o posterior.")
    private Integer anioPublicacion;

    @AssertTrue(message = "El máximo de jugadores no puede ser menor que el mínimo.")
    public boolean isRangoDeJugadoresValido(){
        return minJugadores == null || maxJugadores == null || maxJugadores >= minJugadores;
    }

    @AssertTrue(message = "El año de publicación no puede ser futuro.")
    public boolean isAnioNoFuturo(){
        return anioPublicacion == null || anioPublicacion <= Year.now().getValue();
    }

    public static JuegoForm desde(DetalleDeJuego juego){
        JuegoForm form = new JuegoForm();
        form.setTitulo(juego.titulo());
        form.setCategoria(juego.categoria());
        form.setEditorial(juego.editorial());
        form.setMinJugadores(juego.minJugadores());
        form.setMaxJugadores(juego.maxJugadores());
        form.setMinEdad(juego.minEdad());
        form.setDescripcion(juego.descripcion());
        form.setAnioPublicacion(juego.anioPublicacion());
        return form;
    }

    /* Se limpia el título acá porque con él se controla que no haya dos juegos iguales. */
    public DatosDeJuego aDatosDeJuego(){
        return new DatosDeJuego(titulo.trim(), categoria, editorial,
                minJugadores, maxJugadores, minEdad, descripcion, anioPublicacion);
    }
}
