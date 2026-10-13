package com.borboletta.vueltaaltablero.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity 
@Getter // Al estar a nivel de clase, genera automáticamente el método getter para cada uno de los atributos.
@NoArgsConstructor // Lombok genera un constructor sin argumentos.
@AllArgsConstructor // Lombok genera un constructor con un argumento para cada atributo.

public class Usuario{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nro;
    private String nombre;
    private String apellido;
    private String dni;
    private String correo;
    private String contrasenia;
    private String telefono;
    private LocalDate fecha_registro;
    private boolean rol_admin;
    
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
    
    // Setters //
    public void setNombre(String nombre){
        if(nombre == null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException("Un gusto... ¿dulce o amargo?");
        }
        this.nombre = nombre;
    }
    
    public void setApellido(String apellido){
        if(apellido == null || apellido.trim().isEmpty()){
            throw new IllegalArgumentException("De los...");
        }
        this.apellido = apellido;
    }
    
    public void setDni(String dni){
        if(dni == null || dni.trim().isEmpty()){
            throw new IllegalArgumentException("No solo somos un número..."
                                                + " sino también un documento...");
        }
        this.dni = dni;
    }
    
    public void setCorreo(String correo){
        if(correo == null || correo.trim().isEmpty()){
            throw new IllegalArgumentException("Atte...");
        }
        this.correo = correo;
    }
    
    public void setContrasenia(String contrasenia){
        if(contrasenia == null || contrasenia.trim().isEmpty()){
            throw new IllegalArgumentException("Debe de estar por algún lado...");
        }
        this.contrasenia = contrasenia;
    }
    
    public void setTelefono(String telefono){
        if(telefono == null || telefono.trim().isEmpty()){
            throw new IllegalArgumentException("\"Número equivocado\" por lo menos...");
        }
        this.telefono = telefono;
    }
}
