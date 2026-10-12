package com.borboletta.vueltaaltablero.entity;

import com.borboletta.vueltaaltablero.util.Validacion;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity 
@Getter // Al estar a nivel de clase, genera automáticamente el método getter para cada uno de los atributos.
@NoArgsConstructor // Lombok genera un constructor sin argumentos.

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
    
    //Constructor con todos los argumentos, usando los setters para validar
    public Usuario(Long nro, String nombre, String apellido, String dni, String correo, String contrasenia, String telefono, LocalDate fecha_registro, boolean rol_admin){
        this.nro = nro;
        setNombre(nombre);
        setApellido(apellido);
        setDni(dni);
        setCorreo(correo);
        setContrasenia(contrasenia);
        setTelefono(telefono);
        this.fecha_registro = fecha_registro;
        this.rol_admin = rol_admin;
    }

    // Setters //
    
   public void setId(Long nro) { this.nro = nro; }
    
    public void setNombre(String nombre) {
        this.nombre = Validacion.validarCadena(nombre,"Un gusto... ¿dulce o amargo?");
    }
    
    public void setApellido(String apellido){
        this.apellido = Validacion.validarCadena(apellido,"De los...");
    }
    
    public void setDni(String dni){
        this.dni = Validacion.validarCadena(dni,"No solo somos un número... sino también un documento...");
    }
    
    public void setCorreo(String correo){
        this.correo = Validacion.validarCadena(correo,"Atte...");
    }
    
    public void setContrasenia(String contrasenia){
        this.contrasenia = Validacion.validarCadena(contrasenia,"Debe estar por algun lado...");
    }
    
    public void setTelefono(String telefono){
        this.telefono = Validacion.validarCadena(telefono,"\"Numero equivocado\" por lo menos...");
    }
}
