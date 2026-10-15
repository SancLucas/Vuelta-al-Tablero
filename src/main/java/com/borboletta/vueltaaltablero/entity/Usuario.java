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

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    private Usuario(String nombre, String apellido, String dni, String correo,
                    String telefono, String contrasenia, boolean rolAdmin){
        this.nombre = exigirTexto(nombre, "Un gusto... ¿dulce o amargo?");
        this.apellido = exigirTexto(apellido, "De los...");
        this.dni = exigirTexto(dni, "No solo somos un número... sino también un documento...");
        this.correo = exigirTexto(correo, "Atte...").toLowerCase(Locale.ROOT);
        this.telefono = exigirTexto(telefono, "\"Número equivocado\" por lo menos...");
        this.contrasenia = exigirTexto(contrasenia, "Debe de estar por algún lado...");
        this.fechaRegistro = LocalDate.now();
        this.rolAdmin = rolAdmin;
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
}