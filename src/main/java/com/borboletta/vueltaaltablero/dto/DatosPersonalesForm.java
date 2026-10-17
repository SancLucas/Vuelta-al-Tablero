package com.borboletta.vueltaaltablero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DatosPersonalesForm{

    @NotBlank(message = "Ingresá tu nombre.")
    @Size(max = 45, message = "Máximo 45 caracteres.")
    private String nombre;

    @NotBlank(message = "Ingresá tu apellido.")
    @Size(max = 45, message = "Máximo 45 caracteres.")
    private String apellido;

    @NotBlank(message = "Ingresá tu teléfono.")
    @Pattern(regexp = "[0-9+\\- ]{6,15}", message = "Teléfono inválido (6 a 15 caracteres).")
    private String telefono;
}
