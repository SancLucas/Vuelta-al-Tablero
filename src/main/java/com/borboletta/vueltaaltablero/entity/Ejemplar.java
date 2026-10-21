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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
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

    // Solo tiene valor si disponibleAlquiler es true (lo exige chk_ejemplar_precio de la tabla).
    private BigDecimal precioPorDia;

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
                     boolean disponibleAlquiler, BigDecimal precioPorDia){
        exigirQue(juego != null, "Un ejemplar es la copia de un juego.");
        exigirQue(propietario != null, "Un ejemplar tiene que tener propietario.");
        this.juego = juego;
        this.propietario = propietario;
        asignarCondicion(estado, disponibleAlquiler, precioPorDia);
    }

    public static Ejemplar publicar(Juego juego, Usuario propietario, EstadoEjemplar estado,
                                    boolean disponibleAlquiler, BigDecimal precioPorDia){
        return new Ejemplar(juego, propietario, estado, disponibleAlquiler, precioPorDia);
    }

    public void actualizar(EstadoEjemplar estado, boolean disponibleAlquiler,
                           BigDecimal precioPorDia){
        asignarCondicion(estado, disponibleAlquiler, precioPorDia);
    }

    public void darDeBaja(){
        if (this.fechaBaja == null){
            this.fechaBaja = LocalDate.now();
        }
    }

    /**
     * Tell, Don't Ask: se le dice al ejemplar que se alquile a alguien y él decide si puede.
     * Acá viven las reglas que dependen solo de él. Que no tenga ya otro alquiler abierto lo
     * controla el servicio, porque el ejemplar no conoce sus alquileres.
     */
    public Alquiler alquilarA(Usuario inquilino, LocalDate fechaEntrega,
                              LocalDate fechaDevolucionPrevista){
        exigirQue(this.fechaBaja == null, "El ejemplar ya no está en la colección de su dueño.");
        exigirQue(this.disponibleAlquiler, "El dueño no ofrece este ejemplar en alquiler.");
        exigirQue(inquilino != null, "Un alquiler necesita a quien alquila.");
        exigirQue(!Objects.equals(propietario.getNro(), inquilino.getNro()),
                "Nadie puede alquilar su propio ejemplar.");
        return Alquiler.registrar(this, inquilino, fechaEntrega, fechaDevolucionPrevista);
    }

    /* Sin "public" a propósito: solo Alquiler (mismo paquete) pregunta cuánto cuesta, y lo hace
    después de que alquilarA comprobó que el ejemplar está disponible, o sea, que tiene precio. */
    BigDecimal costoDeAlquilarPor(long dias){
        return precioPorDia.multiply(BigDecimal.valueOf(dias));
    }

    private void asignarCondicion(EstadoEjemplar estado, boolean disponibleAlquiler,
                                  BigDecimal precioPorDia){
        exigirQue(estado != null, "Indicá en qué estado está el ejemplar.");
        exigirQue(!disponibleAlquiler || (precioPorDia != null && precioPorDia.signum() > 0),
                "Para ofrecer el ejemplar en alquiler indicá un precio por día mayor a cero.");
        this.estado = estado;
        this.disponibleAlquiler = disponibleAlquiler;
        // Si no se ofrece en alquiler no tiene precio: el CHECK de la tabla lo exige.
        this.precioPorDia = disponibleAlquiler
                ? precioPorDia.setScale(2, RoundingMode.HALF_UP)
                : null;
    }
}