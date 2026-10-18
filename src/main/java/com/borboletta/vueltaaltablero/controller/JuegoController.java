package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.service.JuegoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/* Catálogo público: cualquiera puede mirarlo, solo con GET. La administración está en JuegoAdminController. */
@Controller
@RequestMapping("/juegos")
@RequiredArgsConstructor
public class JuegoController{

    private static final String VISTA_LISTA = "juegos/lista";
    private static final String VISTA_DETALLE = "juegos/detalle";

    private final JuegoService juegoService;

    @GetMapping
    public String catalogo(Model model){
        model.addAttribute("juegos", juegoService.catalogo());
        return VISTA_LISTA;
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model){
        model.addAttribute("juego", juegoService.obtener(id));
        return VISTA_DETALLE;
    }
}

