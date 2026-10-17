package com.borboletta.vueltaaltablero.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.function.BiPredicate;
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

    public static Usuario registrarAdministrador(String nombre, String apellido, String dni,
                                                String correo, String telefono, String contra){
        return new Usuario(nombre, apellido, dni, correo, telefono, contra, true);
    }

    /**
     * Tell, Don't Ask: el propio Usuario decide qué entrega a la capa de seguridad.
     * Es la única vía por la que sale la contraseña (ya codificada); no hay getter.
     * La comparación de contraseñas la hace Spring Security, no la entidad.
     */
    public Credenciales credenciales(){
        return new Credenciales(correo, contrasenia, rolAdmin);
    }

    public record Credenciales(String correo, String contraseniaCodificada, boolean administrador){ }
    
    /** Tell, Don't Ask: se le dice al usuario que se actualice; él valida sus reglas. */
    public void actualizarDatosPersonales(String nombre, String apellido, String telefono){
        asignarDatosPersonales(nombre, apellido, telefono);
    }
    
        /** Tell, Don't Ask: se le dice al usuario que se dé de baja. */
    public void darDeBaja(){
        this.fechaBaja = LocalDate.now();
    }

    /**
     * Reactivación por registro: la persona vuelve a su misma cuenta con datos nuevos.
     * Nunca devuelve el rol de administrador: el alta pública no crea administradores.
     */
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