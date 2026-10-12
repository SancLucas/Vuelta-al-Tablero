package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class UsuarioController { 
 
    @Autowired 
    private UsuarioService usuarioService; 
 
    @GetMapping("/usuarios") 
    public String listUsuarios(Model model) { 
        List<Usuario> listaDeUsuarios = usuarioService.obtenerTodosLosUsuarios();         
        model.addAttribute("usuarios", listaDeUsuarios);         
        return "usuarios/list";  
    } 
}
