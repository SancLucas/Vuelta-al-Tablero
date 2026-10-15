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

    public static Usuario registrar(String nombre, String apellido, String dni, String correo,
                                    String telefono, String contrasenia){
        return new Usuario(nombre, apellido, dni, correo, telefono, contrasenia, false);
    }

    public static Usuario registrarAdministrador(String nombre, String apellido, String dni, String correo,
                                                 String telefono, String contrasenia){
        return new Usuario(nombre, apellido, dni, correo, telefono, contrasenia, true);
    }
    
    /**
     * Principio Tell, Don't Ask: El objeto Usuario valida su propia contraseña
     * en lugar de que el servicio extraiga el contrasenia con un getter.
     */
    public boolean verificarContrasenia(String contraseniaIngresado){
        boolean esValida = false;
        if(this.contrasenia != null && this.contrasenia.equals(contraseniaIngresado)){
            esValida = true;
        }
        return esValida;
    }
}