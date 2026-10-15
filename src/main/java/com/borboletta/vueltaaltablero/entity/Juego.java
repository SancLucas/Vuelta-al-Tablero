package com.borboletta.vueltaaltablero.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity 
@Getter // Al estar a nivel de clase, genera automáticamente el método getter para cada uno de los atributos.
@NoArgsConstructor // Lombok genera un constructor sin argumentos.
@AllArgsConstructor // Lombok genera un constructor con un argumento para cada atributo.

public class Juego{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String categoria;
    private String editorial;
    private Long id_ficha_tecnica;
    
///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
}
    