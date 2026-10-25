package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.exception.RecursoNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.UsuarioNoEncontradoException;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/* Manejo de errores para toda la aplicación, en un solo lugar. */
@ControllerAdvice
public class ManejadorDeExcepciones{

    private static final String VISTA_NO_ENCONTRADO = "error/404";
    private static final String REDIRECCION_INGRESO = "redirect:/usuario/ingreso";

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    /* Juego o ejemplar que no existe (o que no es tuyo): página 404. */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String recursoNoEncontrado(RecursoNoEncontradoException e, Model model){
        model.addAttribute("mensaje", e.getMessage());
        return VISTA_NO_ENCONTRADO;
    }

    /* Esta excepción siempre es sobre la cuenta de QUIEN TIENE LA SESIÓN: ya no existe o fue dada de
    baja y la sesión quedó huérfana. Antes vivía en PerfilController; ahora que otro controller
    también puede toparse con esto, se movió acá, que es lo que habíamos anticipado. */
    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public String cuentaInexistente(HttpSession session){
        session.invalidate();
        return REDIRECCION_INGRESO;
    }
}
