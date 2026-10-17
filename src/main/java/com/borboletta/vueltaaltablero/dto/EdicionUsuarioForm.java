package com.borboletta.vueltaaltablero.dto;

public class EdicionUsuarioForm extends DatosPersonalesForm{

    public static EdicionUsuarioForm desde(DatosDePerfil perfil){
        EdicionUsuarioForm form = new EdicionUsuarioForm();
        form.setNombre(perfil.nombre());
        form.setApellido(perfil.apellido());
        form.setTelefono(perfil.telefono());
        return form;
    }

    public DatosDeEdicion aDatosDeEdicion(){
        return new DatosDeEdicion(getNombre(), getApellido(), getTelefono());
    }
}