package com.borboletta.vueltaaltablero.entity;

import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirQue;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** La copia física de un Juego que un usuario (o la administración) pone en la plataforma. */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // lo exige JPA; nadie más lo usa
public class Ejemplar{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EstadoEjemplarConverter.class)
    private EstadoEjemplar estado;

    private boolean disponibleAlquiler;

    /* updatable = false: id_juego y nro_usuario forman parte de la clave primaria de la tabla,
    así que un ejemplar nunca cambia de juego ni de dueño. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_juego", nullable = false, updatable = false)
    private Juego juego;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nro_usuario", nullable = false, updatable = false)
    private Usuario propietario;

    private LocalDate fechaBaja;     // null = el ejemplar sigue en la colección de su dueño

    private Ejemplar(Juego juego, Usuario propietario, EstadoEjemplar estado,
                     boolean disponibleAlquiler){
        exigirQue(juego != null, "Un ejemplar es la copia de un juego.");
        exigirQue(propietario != null, "Un ejemplar tiene que tener propietario.");
        this.juego = juego;
        this.propietario = propietario;
        asignarCondicion(estado, disponibleAlquiler);
    }

    public static Ejemplar publicar(Juego juego, Usuario propietario, EstadoEjemplar estado,
                                    boolean disponibleAlquiler){
        return new Ejemplar(juego, propietario, estado, disponibleAlquiler);
    }

    public void actualizar(EstadoEjemplar estado, boolean disponibleAlquiler){
        asignarCondicion(estado, disponibleAlquiler);
    }

    public void darDeBaja(){
        if (this.fechaBaja == null){
            this.fechaBaja = LocalDate.now();
        }
    }

    private void asignarCondicion(EstadoEjemplar estado, boolean disponibleAlquiler){
        exigirQue(estado != null, "Indicá en qué estado está el ejemplar.");
        this.estado = estado;
        this.disponibleAlquiler = disponibleAlquiler;
    }
}