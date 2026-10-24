package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.config.Roles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;
import java.time.Year;
import org.springframework.security.core.Authentication;

@ControllerAdvice
public class GlobalControllerAdvice{

    private final String correoContacto;

    /* El correo de la administración se configura en application.properties (vuelta.contacto.correo). */
    public GlobalControllerAdvice(@Value("${vuelta.contacto.correo}") String correoContacto){
        this.correoContacto = correoContacto;
    }

    /* Lo usan las vistas para los enlaces "escribinos" (pedir un juego, cuenta bloqueada). */
    @ModelAttribute("correoContacto")
    public String correoDeContacto(){
        return correoContacto;
    }

    @ModelAttribute("currentYear")
    public int getCurrentYear(){
        return Year.now().getValue();
    }

    /* Lo usa el header para decidir qué opciones del menú mostrar. */
    @ModelAttribute("sesionIniciada")
    public boolean haySesionIniciada(Principal principal){
        return principal != null;
    }
    
        /* Lo usan las vistas del catálogo para mostrar (o no) los botones de administración.
    Es solo una comodidad visual: la protección real está en SecurityConfig (/admin/**). */
    @ModelAttribute("esAdmin")
    public boolean esAdministrador(Authentication autenticacion){
        return autenticacion != null && autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> Roles.AUTORIDAD_ADMINISTRADOR.equals(autoridad.getAuthority()));
    }
}