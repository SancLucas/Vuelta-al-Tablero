package com.borboletta.vueltaaltablero.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/* Guarda la categoría con su etiqueta ("Clásicos", "Party"...) y no con el nombre del enum. */
@Converter
public class CategoriaJuegoConverter implements AttributeConverter<CategoriaJuego, String>{

    @Override
    public String convertToDatabaseColumn(CategoriaJuego categoria){
        return categoria == null ? null : categoria.getEtiqueta();
    }

    @Override
    public CategoriaJuego convertToEntityAttribute(String etiqueta){
        return etiqueta == null ? null : CategoriaJuego.desdeEtiqueta(etiqueta);
    }
}
