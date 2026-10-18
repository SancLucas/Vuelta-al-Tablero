package com.borboletta.vueltaaltablero.dto;

public class EdicionEjemplarForm extends DatosDelEjemplarForm{

    public static EdicionEjemplarForm desde(DetalleDeEjemplar ejemplar){
        EdicionEjemplarForm form = new EdicionEjemplarForm();
        form.setEstado(ejemplar.estado());
        form.setDisponibleAlquiler(ejemplar.disponibleAlquiler());
        return form;
    }

    public DatosDeEdicionDeEjemplar aDatosDeEdicion(){
        return new DatosDeEdicionDeEjemplar(getEstado(), isDisponibleAlquiler());
    }
}
