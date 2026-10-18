package com.borboletta.vueltaaltablero.dto;

/* Lo que el servicio necesita para registrar. No conoce nada de la web.
Se usa para que el controlador no tenga que tomar campo por campo */
public record DatosDeRegistroUsuario(String nombre, String apellido, String dni, String correo, String telefono, String contrasenia){ }