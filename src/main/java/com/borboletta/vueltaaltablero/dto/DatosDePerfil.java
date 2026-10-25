package com.borboletta.vueltaaltablero.dto;

import com.borboletta.vueltaaltablero.entity.Usuario;
import java.time.LocalDate;

public record DatosDePerfil(String nombre, String apellido, String dni, String correo,
                            String telefono, LocalDate fechaRegistro, String rol){

    private static final String ROL_ADMINISTRADOR = "Administrador";
    private static final String ROL_USUARIO = "Usuario";

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    public static DatosDePerfil desde(Usuario usuario){
        String rol = usuario.isRolAdmin() ? ROL_ADMINISTRADOR : ROL_USUARIO;
        return new DatosDePerfil(usuario.getNombre(), usuario.getApellido(), usuario.getDni(),
                                 usuario.getCorreo(), usuario.getTelefono(),
                                 usuario.getFechaRegistro(), rol);
    }
}
