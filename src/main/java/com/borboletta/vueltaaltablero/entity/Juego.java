package com.borboletta.vueltaaltablero.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import lombok.Getter;
import lombok.AccessLevel;
import java.time.LocalDate;
import lombok.NoArgsConstructor;

import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirQue;
import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirTexto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // lo exige JPA; nadie más lo usa
public class Juego{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;

    @Convert(converter = CategoriaJuegoConverter.class)
    private CategoriaJuego categoria;

    private String editorial;

    /* La ficha es parte del juego: se guarda con él (cascade) y se carga solo cuando hace falta (LAZY). */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "id_ficha_tecnica", nullable = false)
    private FichaTecnica fichaTecnica;

    private LocalDate fechaBaja;     // null = el juego está en el catálogo

    private Juego(String titulo, CategoriaJuego categoria, String editorial,
                  FichaTecnica fichaTecnica){
        exigirQue(fichaTecnica != null, "Un juego necesita su ficha técnica.");
        asignarDatosBasicos(titulo, categoria, editorial);
        this.fichaTecnica = fichaTecnica;
    }

    public static Juego crear(String titulo, CategoriaJuego categoria, String editorial,
                              FichaTecnica fichaTecnica){
        return new Juego(titulo, categoria, editorial, fichaTecnica);
    }

    public void actualizarDatos(String titulo, CategoriaJuego categoria, String editorial){
        asignarDatosBasicos(titulo, categoria, editorial);
    }

    /* El servicio no "le pide la ficha al juego para tocarla": le dice al juego que la actualice. */
    public void actualizarFicha(int minJugadores, int maxJugadores, int minEdad,
                                String descripcion, int anioPublicacion){
        fichaTecnica.actualizar(minJugadores, maxJugadores, minEdad, descripcion, anioPublicacion);
    }

    public void darDeBaja(){
        if (this.fechaBaja == null){
            this.fechaBaja = LocalDate.now();
        }
    }

    /* El juego vuelve al catálogo con los datos nuevos. Se reutiliza la MISMA fila: dar de baja y
    volver a agregar un juego no duplica registros en la base (y sus ejemplares vuelven solos). */
    public void reactivar(String titulo, CategoriaJuego categoria, String editorial){
        if (this.fechaBaja == null){
            throw new IllegalStateException("El juego ya está en el catálogo.");
        }
        asignarDatosBasicos(titulo, categoria, editorial);
        this.fechaBaja = null;
    }

    private void asignarDatosBasicos(String titulo, CategoriaJuego categoria, String editorial){
        exigirQue(categoria != null, "El juego necesita una categoría.");
        this.titulo = exigirTexto(titulo, "El juego necesita un título.");
        this.categoria = categoria;
        this.editorial = exigirTexto(editorial, "El juego necesita una editorial.");
    }
}