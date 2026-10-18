package com.borboletta.vueltaaltablero.entity;

import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirQue;
import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirTexto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // lo exige JPA; nadie más lo usa
public class Juego{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String categoria;
    private String editorial;

    /* La ficha es parte del juego: se guarda con él (cascade) y se carga solo cuando hace falta (LAZY). */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "id_ficha_tecnica", nullable = false)
    private FichaTecnica fichaTecnica;

    private LocalDate fechaBaja;     // null = el juego está en el catálogo

    private Juego(String titulo, String categoria, String editorial, FichaTecnica fichaTecnica){
        exigirQue(fichaTecnica != null, "Un juego necesita su ficha técnica.");
        asignarDatosBasicos(titulo, categoria, editorial);
        this.fichaTecnica = fichaTecnica;
    }

    public static Juego crear(String titulo, String categoria, String editorial,
                              FichaTecnica fichaTecnica){
        return new Juego(titulo, categoria, editorial, fichaTecnica);
    }

    public void actualizarDatos(String titulo, String categoria, String editorial){
        asignarDatosBasicos(titulo, categoria, editorial);
    }

    /** El servicio no "le pide la ficha al juego para tocarla": le dice al juego que la actualice. */
    public void actualizarFicha(int minJugadores, int maxJugadores, int minEdad,
                                String descripcion, int anioPublicacion){
        fichaTecnica.actualizar(minJugadores, maxJugadores, minEdad, descripcion, anioPublicacion);
    }

    public void darDeBaja(){
        if (this.fechaBaja == null){
            this.fechaBaja = LocalDate.now();
        }
    }

    private void asignarDatosBasicos(String titulo, String categoria, String editorial){
        this.titulo = exigirTexto(titulo, "El juego necesita un título.");
        this.categoria = exigirTexto(categoria, "El juego necesita una categoría.");
        this.editorial = exigirTexto(editorial, "El juego necesita una editorial.");
    }
}
