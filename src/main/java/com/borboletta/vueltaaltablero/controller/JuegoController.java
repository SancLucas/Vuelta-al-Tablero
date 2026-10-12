package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.entity.Juego;
import com.borboletta.vueltaaltablero.service.JuegoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JuegoController{

    @Autowired
    private JuegoService juegoService;

    @GetMapping("/juegos")
    public String listJuegos(Model model){
        List<Juego> listaDeJuegos = juegoService.obtenerTodosLosJuegos();     
        model.addAttribute("juegos", listaDeJuegos);
        return "juegos/list";
    }
}
