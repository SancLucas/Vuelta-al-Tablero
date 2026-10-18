package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.service.EjemplarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/* Lista pública: los ejemplares que hoy se pueden alquilar. La gestión de los propios está en MisEjemplaresController. */
@Controller
@RequiredArgsConstructor
public class EjemplarController{

    private static final String VISTA_LISTA = "ejemplares/lista";

    private final EjemplarService ejemplarService;

    @GetMapping("/ejemplares")
    public String disponibles(Model model){
        model.addAttribute("ejemplares", ejemplarService.disponiblesParaAlquilar());
        return VISTA_LISTA;
    }
}
