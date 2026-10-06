package com.borboletta.vueltaaltablero.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/") //ruta del usuario. En este caso es solo la barra porque es la raíz
    public String index(){
        return "index"; //indica a thymeleaf que busque la plantilla indez, renderice y envie el browser al usuario
    }
}
