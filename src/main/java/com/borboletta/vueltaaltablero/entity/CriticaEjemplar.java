package com.borboletta.vueltaaltablero.entity;

import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirQue;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** La opinión de una persona sobre una copia física concreta (su estado, su trato...). */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // lo exige JPA; nadie más lo usa
public class CriticaEjemplar{

    @EmbeddedId
    private CriticaEjemplarId id;

    /* @MapsId: las dos columnas de la clave son a la vez las claves foráneas, y JPA completa
    el id solo a partir de estas dos relaciones. */
    @MapsId("idEjemplar")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ejemplar")
    private Ejemplar ejemplar;

    @MapsId("nroUsuario")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nro_usuario")
    private Usuario autor;

    @Embedded
    private Opinion opinion;

    private CriticaEjemplar(Ejemplar ejemplar, Usuario autor, Opinion opinion){
        exigirQue(ejemplar != null, "Una crítica es sobre un ejemplar.");
        exigirQue(autor != null, "Una crítica necesita a su autor.");
        this.id = new CriticaEjemplarId(ejemplar.getId(), autor.getNro());
        this.ejemplar = ejemplar;
        this.autor = autor;
        this.opinion = opinion;
    }

    public static CriticaEjemplar escribir(Ejemplar ejemplar, Usuario autor,
                                           String comentario, int puntuacion){
        return new CriticaEjemplar(ejemplar, autor, Opinion.de(comentario, puntuacion));
    }

    /** Tell, Don't Ask: el autor corrige su opinión y la crítica valida la nueva. */
    public void actualizar(String comentario, int puntuacion){
        this.opinion = Opinion.de(comentario, puntuacion);
    }
}