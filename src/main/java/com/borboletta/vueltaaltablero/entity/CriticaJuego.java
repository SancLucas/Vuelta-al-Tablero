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

/** La opinión de una persona sobre el juego en sí (reglas, diversión...), sin importar la copia. */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // lo exige JPA; nadie más lo usa
public class CriticaJuego{

    @EmbeddedId
    private CriticaJuegoId id;

    @MapsId("idJuego")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_juego")
    private Juego juego;

    @MapsId("nroUsuario")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nro_usuario")
    private Usuario autor;

    @Embedded
    private Opinion opinion;

    private CriticaJuego(Juego juego, Usuario autor, Opinion opinion){
        exigirQue(juego != null, "Una crítica es sobre un juego.");
        exigirQue(autor != null, "Una crítica necesita a su autor.");
        this.id = new CriticaJuegoId(juego.getId(), autor.getNro());
        this.juego = juego;
        this.autor = autor;
        this.opinion = opinion;
    }

    public static CriticaJuego escribir(Juego juego, Usuario autor,
                                        String comentario, int puntuacion){
        return new CriticaJuego(juego, autor, Opinion.de(comentario, puntuacion));
    }

    /** Tell, Don't Ask: el autor corrige su opinión y la crítica valida la nueva. */
    public void actualizar(String comentario, int puntuacion){
        this.opinion = Opinion.de(comentario, puntuacion);
    }
}