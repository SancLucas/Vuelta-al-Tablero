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
public class RegistroUsuarioForm extends DatosPersonalesForm{

    @NotBlank(message = "Ingresá tu DNI.")
    @Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener 7 u 8 números, sin puntos.")
    private String dni;

    @NotBlank(message = "Ingresá tu correo.")
    @Email(message = "El correo no es válido.")
    @Size(max = 100, message = "Máximo 100 caracteres.")
    private String correo;

    @NotBlank(message = "Ingresá una contraseña.")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres.")
    private String contrasenia;

    @NotBlank(message = "Repetí la contraseña.")
    private String confirmacionContrasenia;

    @AssertTrue(message = "Las contraseñas no coinciden.")
    public boolean isContraseniasCoinciden(){
        return Objects.equals(contrasenia, confirmacionContrasenia);
    }

    public DatosDeRegistro aDatosDeRegistro(){
        return new DatosDeRegistro(getNombre(), getApellido(), dni, correo,
                                   getTelefono(), contrasenia);
    }
}