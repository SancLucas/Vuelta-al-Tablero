package com.borboletta.vueltaaltablero.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;
import java.time.Year;

@ControllerAdvice
public class GlobalControllerAdvice{

    @ModelAttribute("currentYear")
    public int getCurrentYear(){
        return Year.now().getValue();
    }

    /* Lo usa el header para decidir qué opciones del menú mostrar. */
    @ModelAttribute("sesionIniciada")
    public boolean haySesionIniciada(Principal principal){
        return principal != null;
    }
}