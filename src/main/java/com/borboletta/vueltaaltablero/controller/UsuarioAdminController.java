package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.exception.OperacionNoPermitidaException;
import com.borboletta.vueltaaltablero.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/* Todo lo que cuelga de /admin/** exige rol ADMIN (ver SecurityConfig). */
@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioAdminController{

    private static final String VISTA_LISTADO = "usuarios/list";
    private static final String REDIRECCION_LISTADO = "redirect:/admin/usuarios";
    private static final int USUARIOS_POR_PAGINA = 12;

    private final UsuarioService usuarioService;

    @GetMapping
    public String listado(@PageableDefault(size = USUARIOS_POR_PAGINA) Pageable pageable, Model model){
        model.addAttribute("paginaUsuarios", usuarioService.obtenerTodosLosUsuarios(pageable));
        return VISTA_LISTADO;
    }

    @PostMapping("/{nro}/bloquear")
    public String bloquear(@PathVariable Long nro,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "" + USUARIOS_POR_PAGINA) int size,
                           RedirectAttributes redirectAttributes){
        try{
            usuarioService.bloquear(nro);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "La cuenta quedó bloqueada: ya no puede ingresar a la plataforma.");
        }catch (OperacionNoPermitidaException e){
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return volverAlListado(page, size, redirectAttributes);
    }

    @PostMapping("/{nro}/desbloquear")
    public String desbloquear(@PathVariable Long nro,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "" + USUARIOS_POR_PAGINA) int size,
                              RedirectAttributes redirectAttributes){
        usuarioService.desbloquear(nro);
        redirectAttributes.addFlashAttribute("mensajeExito", "La cuenta quedó desbloqueada.");
        return volverAlListado(page, size, redirectAttributes);
    }

    /* Después de bloquear o desbloquear se vuelve a la MISMA página de la lista, no a la primera. */
    private String volverAlListado(int page, int size, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return REDIRECCION_LISTADO;
    }
}
