package com.borboletta.vueltaaltablero.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import java.util.Locale;
import lombok.AccessLevel;
import java.time.LocalDate;
import lombok.NoArgsConstructor;
import java.util.function.BiPredicate;
import com.borboletta.vueltaaltablero.exception.OperacionNoPermitidaException;

import static com.borboletta.vueltaaltablero.validation.Validaciones.exigirTexto;

@Entity
@Getter // Al estar a nivel de clase, genera automáticamente el método getter para cada uno de los atributos.
@NoArgsConstructor // Lombok genera un constructor sin argumentos, exigido por JPA

public class Usuario{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long nro;
        private String nombre;
        private String apellido;
        private String dni;
        private String correo;

        @Getter(AccessLevel.NONE)
        private String contrasenia;

        private String telefono;
        private LocalDate fechaRegistro;
        private boolean rolAdmin;
        private LocalDate fechaBaja;     // null = cuenta activa
        private LocalDate fechaBloqueo;  // null = cuenta no bloqueada

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    private Usuario(String nombre, String apellido, String dni, String correo,
                    String telefono, String contrasenia, boolean rolAdmin){
        asignarDatosPersonales(nombre, apellido, telefono);
        this.dni = exigirTexto(dni, "No solo somos un número... sino también un documento...");
        this.correo = exigirTexto(correo, "Atte...").toLowerCase(Locale.ROOT);
        this.contrasenia = exigirTexto(contrasenia, "Debe de estar por algún lado...");
        this.fechaRegistro = LocalDate.now();
        this.rolAdmin = rolAdmin;
    }

    private void asignarDatosPersonales(String nombre, String apellido, String telefono){
        this.nombre = exigirTexto(nombre, "Un gusto... ¿dulce o amargo?");
        this.apellido = exigirTexto(apellido, "De los...");
        this.telefono = exigirTexto(telefono, "\"Número equivocado\" por lo menos...");
    }

    public static Usuario registrar(String nombre, String apellido, String dni,
                                    String correo, String telefono, String contra){
        return new Usuario(nombre, apellido, dni, correo, telefono, contra, false);
    }

    /* Tell, Don't Ask: el propio Usuario decide qué entrega a la capa de seguridad.
    Es la única vía por la que sale la contraseña (ya codificada); no hay getter.
    La comparación de contraseñas la hace Spring Security, no la entidad. */
    public Credenciales credenciales(){
        return new Credenciales(correo, contrasenia, rolAdmin, fechaBloqueo != null);
    }

    public record Credenciales(String correo, String contraseniaCodificada,
                               boolean administrador, boolean bloqueado){ }
    
    /* Tell, Don't Ask: se le dice al usuario que se actualice; él valida sus reglas. */
    public void actualizarDatosPersonales(String nombre, String apellido, String telefono){
        asignarDatosPersonales(nombre, apellido, telefono);
    }
    
    /* Tell, Don't Ask: se le dice al usuario que se dé de baja. */
    public void darDeBaja(){
        this.fechaBaja = LocalDate.now();
    }

    /* Tell, Don't Ask: el usuario decide si puede ser bloqueado. A un administrador no se lo bloquea
    (primero habría que quitarle el rol): así nadie deja al sistema sin administración. */
    public void bloquear(){
        if (this.rolAdmin){
            throw new OperacionNoPermitidaException("Un administrador no puede ser bloqueado.");
        }
        if (this.fechaBloqueo == null){
            this.fechaBloqueo = LocalDate.now();
        }
    }

    public void desbloquear(){
        this.fechaBloqueo = null;
    }

    /* Reactivación por registro: la persona vuelve a su misma cuenta con datos nuevos.
    Nunca devuelve el rol de administrador: el alta pública no crea administradores.
    Tampoco levanta un bloqueo: quien estaba bloqueado sigue bloqueado (no se evade volviendo a registrarse). */
    public void reactivar(String nombre, String apellido, String telefono,
                          String contraseniaCodificada){
        if (this.fechaBaja == null){
            throw new IllegalStateException("La cuenta ya está activa.");
        }
        asignarDatosPersonales(nombre, apellido, telefono);
        this.contrasenia = exigirTexto(contraseniaCodificada, "Debe de estar por algún lado...");
        this.rolAdmin = false;
        this.fechaBaja = null;
    }
}
