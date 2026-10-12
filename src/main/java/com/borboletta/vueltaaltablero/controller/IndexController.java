package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Autowired 
    private UsuarioService usuarioService;
        
    @GetMapping("/") //ruta del usuario. En este caso es solo la barra porque es la raíz
    public String index(Model model){
        return "index"; //indica a thymeleaf que busque la plantilla index, renderice y envie el browser al usuario
    }
}
