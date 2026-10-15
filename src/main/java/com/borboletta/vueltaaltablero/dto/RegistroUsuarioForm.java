package com.borboletta.vueltaaltablero.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroUsuarioForm {

    @NotBlank(message = "Ingresá tu nombre.")
    @Size(max = 45, message = "Máximo 45 caracteres.")
    private String nombre;

    @NotBlank(message = "Ingresá tu apellido.")
    @Size(max = 45, message = "Máximo 45 caracteres.")
    private String apellido;

    @NotBlank(message = "Ingresá tu DNI.")
    @Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener 7 u 8 números, sin puntos.")
    private String dni;

    @NotBlank(message = "Ingresá tu correo.")
    @Email(message = "El correo no es válido.")
    @Size(max = 100, message = "Máximo 100 caracteres.")
    private String correo;

    @NotBlank(message = "Ingresá tu teléfono.")
    @Pattern(regexp = "[0-9+\\- ]{6,15}", message = "Teléfono inválido (6 a 15 caracteres).")
    private String telefono;

    @NotBlank(message = "Ingresá una contraseña.")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres.")
    private String contrasenia;

    @NotBlank(message = "Repetí la contraseña.")
    private String confirmacionContrasenia;

    @AssertTrue(message = "Las contraseñas no coinciden.")
    public boolean isContraseniasCoinciden() {
        return Objects.equals(contrasenia, confirmacionContrasenia);
    }

    public DatosDeRegistro aDatosDeRegistro() {
         return new DatosDeRegistro(nombre, apellido, dni, correo, telefono, contrasenia);
    }
}