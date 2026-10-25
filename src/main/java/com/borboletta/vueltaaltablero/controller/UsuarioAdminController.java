package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.service.UsuarioService;
import com.borboletta.vueltaaltablero.exception.OperacionNoPermitidaException;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/* Todo lo que cuelga de /admin/** exige rol ADMIN (ver SecurityConfig). */
@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioAdminController{

    private static final String VISTA_LISTADO = "usuarios/list";
    private static final String REDIRECCION_LISTADO = "redirect:/admin/usuarios";

    private final UsuarioService usuarioService;

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    @GetMapping
    public String listado(Model model){
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return VISTA_LISTADO;
    }

    @PostMapping("/{nro}/bloquear")
    public String bloquear(@PathVariable Long nro, RedirectAttributes redirectAttributes){
        try{
            usuarioService.bloquear(nro);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "La cuenta quedó bloqueada: ya no puede ingresar a la plataforma.");
        }catch (OperacionNoPermitidaException e){
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return REDIRECCION_LISTADO;
    }

    @PostMapping("/{nro}/desbloquear")
    public String desbloquear(@PathVariable Long nro, RedirectAttributes redirectAttributes){
        usuarioService.desbloquear(nro);
        redirectAttributes.addFlashAttribute("mensajeExito", "La cuenta quedó desbloqueada.");
        return REDIRECCION_LISTADO;
    }
}