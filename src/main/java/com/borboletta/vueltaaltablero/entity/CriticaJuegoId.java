package com.borboletta.vueltaaltablero.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.AccessLevel;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* Clave de CriticaJuego: una persona puede criticar una vez cada juego. */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CriticaJuegoId implements Serializable{

    private static final long serialVersionUID = 1L;

    @Column(name = "id_juego")
    private Long idJuego;

    @Column(name = "nro_usuario")
    private Long nroUsuario;

    public CriticaJuegoId(Long idJuego, Long nroUsuario){
        this.idJuego = idJuego;
        this.nroUsuario = nroUsuario;
    }
}
