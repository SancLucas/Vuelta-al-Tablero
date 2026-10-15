package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.exception.UsuarioNoEncontradoException;
import com.borboletta.vueltaaltablero.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PerfilController{

    private static final String VISTA_PERFIL = "usuarios/perfil";
    private static final String REDIRECCION_INGRESO = "redirect:/usuario/ingreso";

    private final UsuarioService usuarioService;

    /* Spring Security garantiza que acá solo llega alguien con sesión iniciada,
    y el Principal es quien ingresó: no hay forma de pedir el perfil de otro. */
    @GetMapping("/usuario/perfil")
    public String verPerfil(Principal principal, HttpSession session, Model model){
        String vista = VISTA_PERFIL;
        try{
            model.addAttribute("perfil", usuarioService.obtenerPerfil(principal.getName()));
        }catch (UsuarioNoEncontradoException e){
            session.invalidate(); // la cuenta ya no existe: la sesión quedó huérfana
            vista = REDIRECCION_INGRESO;
        }
        return vista;
    }
}