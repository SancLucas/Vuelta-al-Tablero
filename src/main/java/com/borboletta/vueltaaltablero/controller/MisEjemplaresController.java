package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.dto.DetalleDeEjemplar;
import com.borboletta.vueltaaltablero.dto.EdicionEjemplarForm;
import com.borboletta.vueltaaltablero.dto.NuevoEjemplarForm;
import com.borboletta.vueltaaltablero.entity.EstadoEjemplar;
import com.borboletta.vueltaaltablero.service.EjemplarService;
import com.borboletta.vueltaaltablero.service.JuegoService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.borboletta.vueltaaltablero.exception.EjemplarYaAlquiladoException;

/* Cada persona gestiona SU colección. Quién es sale siempre del Principal (la sesión), nunca de
un parámetro: el dueño se verifica en el servicio al buscar el ejemplar por id Y por dueño. */
@Controller
@RequestMapping("/mis-ejemplares")
@RequiredArgsConstructor
public class MisEjemplaresController{

    private static final String VISTA_LISTA = "ejemplares/mis-ejemplares";
    private static final String VISTA_FORMULARIO = "ejemplares/formulario";
    private static final String REDIRECCION_LISTA = "redirect:/mis-ejemplares";

    private final EjemplarService ejemplarService;
    private final JuegoService juegoService;

    /* Las opciones del desplegable "Estado", disponibles para todas las vistas de este controller. */
    @ModelAttribute("estados")
    public EstadoEjemplar[] estados(){
        return EstadoEjemplar.values();
    }

    @GetMapping
    public String misEjemplares(Principal principal, Model model){
        model.addAttribute("ejemplares", ejemplarService.deUsuario(principal.getName()));
        return VISTA_LISTA;
    }

    @GetMapping("/nuevo")
    public String mostrarAlta(Model model){
        model.addAttribute("form", new NuevoEjemplarForm());
        return vistaDeAlta(model);
    }

    @PostMapping
    public String procesarAlta(Principal principal,
                               @Valid @ModelAttribute("form") NuevoEjemplarForm form,
                               BindingResult resultado,
                               Model model,
                               RedirectAttributes redirectAttributes){
        String vista = vistaDeAlta(model);
        if (!resultado.hasErrors()){
            ejemplarService.publicar(principal.getName(), form.aDatosDeNuevoEjemplar());
            redirectAttributes.addFlashAttribute("mensajeExito", "Tu ejemplar quedó publicado.");
            vista = REDIRECCION_LISTA;
        }
        return vista;
    }

    @GetMapping("/{id}/editar")
    public String mostrarEdicion(Principal principal, @PathVariable Long id, Model model){
        DetalleDeEjemplar ejemplar = ejemplarService.obtenerDeUsuario(id, principal.getName());
        model.addAttribute("form", EdicionEjemplarForm.desde(ejemplar));
        return vistaDeEdicion(model, ejemplar);
    }

    @PostMapping("/{id}/editar")
    public String procesarEdicion(Principal principal,
                                  @PathVariable Long id,
                                  @Valid @ModelAttribute("form") EdicionEjemplarForm form,
                                  BindingResult resultado,
                                  Model model,
                                  RedirectAttributes redirectAttributes){
        String vista = REDIRECCION_LISTA;
        if (resultado.hasErrors()){
            vista = vistaDeEdicion(model, ejemplarService.obtenerDeUsuario(id, principal.getName()));
        }else{
            ejemplarService.actualizar(id, principal.getName(), form.aDatosDeEdicion());
            redirectAttributes.addFlashAttribute("mensajeExito", "Los datos del ejemplar se actualizaron.");
        }
        return vista;
    }

    @PostMapping("/{id}/baja")
    public String darDeBaja(Principal principal, @PathVariable Long id,
                            RedirectAttributes redirectAttributes){
        try{
            ejemplarService.darDeBaja(id, principal.getName());
            redirectAttributes.addFlashAttribute("mensajeExito", "El ejemplar se quitó de tu colección.");
        }catch(EjemplarYaAlquiladoException e){
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return REDIRECCION_LISTA;
    }

    /* La plantilla distingue alta de edición con "modoEdicion": se declara siempre, en ambos casos
    (en SpEL, "!modoEdicion" falla si la variable no existe). */
    private String vistaDeAlta(Model model){
        model.addAttribute("modoEdicion", false);
        model.addAttribute("juegos", juegoService.catalogo());
        return VISTA_FORMULARIO;
    }

    private String vistaDeEdicion(Model model, DetalleDeEjemplar ejemplar){
        model.addAttribute("ejemplar", ejemplar);
        model.addAttribute("modoEdicion", true);
        return VISTA_FORMULARIO;
    }
}
