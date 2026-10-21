package com.borboletta.vueltaaltablero.entity;

import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirQue;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** El préstamo de un Ejemplar a un usuario, con sus fechas y el total que se acordó al pedirlo. */
@Entity
@Table(name = "alquila") // la tabla se llama distinto que la clase, por eso se indica
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // lo exige JPA; nadie más lo usa
public class Alquiler{

    public static final int DIAS_MINIMOS = 1;           // chk_alquila_duracion del DDL
    public static final int DIAS_MAXIMOS = 7;
    public static final int ANTELACION_MAXIMA_DIAS = 7; // cuántos días antes se puede pedir

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* updatable = false: un alquiler no cambia de persona ni de ejemplar una vez registrado. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nro_usuario", nullable = false, updatable = false)
    private Usuario inquilino;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ejemplar", nullable = false, updatable = false)
    private Ejemplar ejemplar;

    // Total del alquiler (precio por día del ejemplar x días), fijado al pedirlo: si el dueño
    // cambia después su precio, este alquiler no se modifica.
    private BigDecimal precio;
    private LocalDate fechaEntrega;
    private LocalDate fechaDevolucionPrevista;   // la que eligió quien alquila
    private LocalDate fechaDevolucion;           // la real; null = todavía no lo devolvió

    private Alquiler(Ejemplar ejemplar, Usuario inquilino, LocalDate fechaEntrega,
                     LocalDate fechaDevolucionPrevista){
        exigirQue(ejemplar != null, "Un alquiler es de un ejemplar.");
        exigirQue(inquilino != null, "Un alquiler necesita a quien alquila.");
        exigirQue(fechaEntrega != null, "Indicá la fecha de entrega.");
        exigirQue(fechaDevolucionPrevista != null, "Indicá la fecha de devolución.");
        exigirQue(!fechaEntrega.isBefore(LocalDate.now()),
                "La fecha de entrega no puede ser anterior a hoy.");
        exigirQue(!fechaEntrega.isAfter(LocalDate.now().plusDays(ANTELACION_MAXIMA_DIAS)),
                "Solo se puede pedir un ejemplar con hasta " + ANTELACION_MAXIMA_DIAS
                        + " días de anticipación.");
        long dias = ChronoUnit.DAYS.between(fechaEntrega, fechaDevolucionPrevista);
        exigirQue(dias >= DIAS_MINIMOS && dias <= DIAS_MAXIMOS,
                "El alquiler dura entre " + DIAS_MINIMOS + " y " + DIAS_MAXIMOS + " días.");
        this.ejemplar = ejemplar;
        this.inquilino = inquilino;
        this.fechaEntrega = fechaEntrega;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.precio = ejemplar.costoDeAlquilarPor(dias);   // el ejemplar sabe cuánto cuesta
    }

    /* Sin "public" a propósito: solo Ejemplar.alquilarA (mismo paquete) puede crear un alquiler,
    así nadie se salta las reglas del ejemplar. */
    static Alquiler registrar(Ejemplar ejemplar, Usuario inquilino, LocalDate fechaEntrega,
                              LocalDate fechaDevolucionPrevista){
        return new Alquiler(ejemplar, inquilino, fechaEntrega, fechaDevolucionPrevista);
    }

    /**
     * Tell, Don't Ask: el propietario le dice al alquiler que ya recibió el ejemplar de vuelta.
     * La fecha real es la de hoy.
     */
    public void registrarDevolucion(){
        LocalDate hoy = LocalDate.now();
        exigirQue(this.fechaDevolucion == null, "Este alquiler ya fue devuelto.");
        exigirQue(!hoy.isBefore(this.fechaEntrega),
                "Todavía no llegó la fecha de entrega: no se puede registrar la devolución.");
        this.fechaDevolucion = hoy;
    }

    public boolean estaEnCurso(){
        return this.fechaDevolucion == null;
    }

    public long dias(){
        return ChronoUnit.DAYS.between(this.fechaEntrega, this.fechaDevolucionPrevista);
    }
}