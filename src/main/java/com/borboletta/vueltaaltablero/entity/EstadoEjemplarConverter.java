package com.borboletta.vueltaaltablero.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Guarda el estado con su etiqueta ("Nuevo", "Dañado"...) y no con el nombre del enum,
 * para respetar los valores que ya acepta la base (CHECK del DDL).
 */
@Converter
public class EstadoEjemplarConverter implements AttributeConverter<EstadoEjemplar, String>{

    @Override
    public String convertToDatabaseColumn(EstadoEjemplar estado){
        return estado == null ? null : estado.getEtiqueta();
    }

    @Override
    public EstadoEjemplar convertToEntityAttribute(String etiqueta){
        return etiqueta == null ? null : EstadoEjemplar.desdeEtiqueta(etiqueta);
    }
}
