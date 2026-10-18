package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.config.GestorDeSesiones;
import com.borboletta.vueltaaltablero.dto.DatosDePerfil;
import com.borboletta.vueltaaltablero.dto.EdicionUsuarioForm;
import com.borboletta.vueltaaltablero.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PerfilController{

    private static final String VISTA_PERFIL = "usuarios/perfil";
    private static final String VISTA_FORMULARIO = "usuarios/formulario";
    private static final String REDIRECCION_PERFIL = "redirect:/usuario/perfil";
    private static final String REDIRECCION_INICIO = "redirect:/?cuentaEliminada";

    private final UsuarioService usuarioService;
    private final GestorDeSesiones gestorDeSesiones;

    /* Spring Security garantiza que acá solo llega alguien con sesión iniciada,
    y el Principal es quien ingresó: no hay forma de pedir los datos de otro. */
    @GetMapping("/usuario/perfil")
    public String verPerfil(Principal principal, Model model){
        model.addAttribute("perfil", usuarioService.obtenerPerfil(principal.getName()));
        return VISTA_PERFIL;
    }

    @GetMapping("/usuario/editar")
    public String mostrarEdicion(Principal principal, Model model){
        DatosDePerfil perfil = usuarioService.obtenerPerfil(principal.getName());
        model.addAttribute("form", EdicionUsuarioForm.desde(perfil));
        return vistaDeEdicion(model, perfil);
    }

    @PostMapping("/usuario/editar")
    public String procesarEdicion(Principal principal,
                                  @Valid @ModelAttribute("form") EdicionUsuarioForm form,
                                  BindingResult resultado,
                                  Model model,
                                  RedirectAttributes redirectAttributes){
        String vista = REDIRECCION_PERFIL;
        if (resultado.hasErrors()){
            vista = vistaDeEdicion(model, usuarioService.obtenerPerfil(principal.getName()));
        }else{
            usuarioService.actualizar(principal.getName(), form.aDatosDeEdicion());
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Tus datos se actualizaron correctamente.");
        }
        return vista;
    }

    @PostMapping("/usuario/eliminar")
    public String eliminarCuenta(Authentication autenticacion,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        String correo = autenticacion.getName();
        usuarioService.darDeBaja(correo);
        new SecurityContextLogoutHandler().logout(request, response, autenticacion);   // esta sesión
        gestorDeSesiones.cerrarSesionesDe(correo);                                     // las demás
        return REDIRECCION_INICIO;
    }

    private String vistaDeEdicion(Model model, DatosDePerfil perfil){
        model.addAttribute("perfil", perfil);
        model.addAttribute("modoEdicion", true);
        return VISTA_FORMULARIO;
    }
}