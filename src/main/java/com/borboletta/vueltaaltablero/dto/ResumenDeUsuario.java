package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.Usuario;
import java.time.LocalDate;

/** Lo que el administrador ve de cada cuenta en el listado: nunca la contraseña. */
public record ResumenDeUsuario(Long nro, String nombre, String apellido, String correo,
                               String dni, boolean administrador, LocalDate fechaBloqueo){

    public static ResumenDeUsuario desde(Usuario usuario){
        return new ResumenDeUsuario(usuario.getNro(), usuario.getNombre(), usuario.getApellido(),
                usuario.getCorreo(), usuario.getDni(), usuario.isRolAdmin(),
                usuario.getFechaBloqueo());
    }

    public boolean bloqueado(){
        return fechaBloqueo != null;
    }
}
