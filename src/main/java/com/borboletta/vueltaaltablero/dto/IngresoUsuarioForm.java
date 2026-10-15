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
public class IngresoUsuarioForm{

    @NotBlank(message = "Ingresá tu correo.")
    @Email(message = "El correo no es válido.")
    @Size(max = 100, message = "Máximo 100 caracteres.")
    private String correo;

    @NotBlank(message = "Ingresá una contraseña.")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres.")
    private String contrasenia;

    public DatosDeIngreso aDatosDeIngreso(){
         return new DatosDeIngreso(correo, contrasenia);
    }
}
