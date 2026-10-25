package com.borboletta.vueltaaltablero.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Getter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* Clave de CriticaEjemplar: una persona puede criticar una vez cada ejemplar. */
@Embeddable
@Getter
@EqualsAndHashCode // JPA exige equals/hashCode en una clave compuesta
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CriticaEjemplarId implements Serializable{

    private static final long serialVersionUID = 1L;

    @Column(name = "id_ejemplar")
    private Long idEjemplar;

    @Column(name = "nro_usuario")
    private Long nroUsuario;

    public CriticaEjemplarId(Long idEjemplar, Long nroUsuario){
        this.idEjemplar = idEjemplar;
        this.nroUsuario = nroUsuario;
    }
}
