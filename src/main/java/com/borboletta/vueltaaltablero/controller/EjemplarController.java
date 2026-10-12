package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.entity.Ejemplar;
import com.borboletta.vueltaaltablero.service.EjemplarService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EjemplarController{

    @Autowired
    private EjemplarService ejemplarService;

    @GetMapping("/ejemplares")
    public String listEjemplares(Model model){
        List<Ejemplar> listaDeEjemplares = ejemplarService.obtenerTodosLosEjemplares();     
        model.addAttribute("ejemplares", listaDeEjemplares);
        return "ejemplares/list";
    }
}
