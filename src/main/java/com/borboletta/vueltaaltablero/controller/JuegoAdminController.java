package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.dto.DetalleDeJuego;
import com.borboletta.vueltaaltablero.dto.JuegoForm;
import com.borboletta.vueltaaltablero.entity.CategoriaJuego;
import com.borboletta.vueltaaltablero.exception.JuegoYaExisteException;
import com.borboletta.vueltaaltablero.service.JuegoService;
import jakarta.validation.Valid;
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

/* Todo lo que cuelga de /admin/** exige rol ADMIN (ver SecurityConfig): acá no hace falta
volver a chequearlo, y es imposible que se olvide en un método nuevo. */
@Controller
@RequestMapping("/admin/juegos")
@RequiredArgsConstructor
public class JuegoAdminController{

    private static final String VISTA_FORMULARIO = "juegos/formulario";
    private static final String REDIRECCION_CATALOGO = "redirect:/juegos";

    private final JuegoService juegoService;

    /* Las opciones del desplegable "Categoría", disponibles para las vistas de este controller. */
    @ModelAttribute("categorias")
    public CategoriaJuego[] categorias(){
        return CategoriaJuego.values();
    }

    @GetMapping("/nuevo")
    public String mostrarAlta(Model model){
        model.addAttribute("form", new JuegoForm());
        return vistaDeAlta(model);
    }

    @PostMapping
    public String procesarAlta(@Valid @ModelAttribute("form") JuegoForm form,
                               BindingResult resultado,
                               Model model,
                               RedirectAttributes redirectAttributes){
        String vista = vistaDeAlta(model);
        if (!resultado.hasErrors()){
            vista = crear(form, resultado, vista, redirectAttributes);
        }
        return vista;
    }

    private String crear(JuegoForm form, BindingResult resultado,
                         String vistaSiFalla, RedirectAttributes redirectAttributes){
        String vista = vistaSiFalla;
        try{
            juegoService.crear(form.aDatosDeJuego());
            redirectAttributes.addFlashAttribute("mensajeExito", "El juego se agregó al catálogo.");
            vista = REDIRECCION_CATALOGO;
        }catch (JuegoYaExisteException e){
            resultado.rejectValue(e.getCampo(), "duplicado", e.getMessage());
        }
        return vista;
    }

    @GetMapping("/{id}/editar")
    public String mostrarEdicion(@PathVariable Long id, Model model){
        DetalleDeJuego juego = juegoService.obtener(id);
        model.addAttribute("form", JuegoForm.desde(juego));
        return vistaDeEdicion(model, id);
    }

    @PostMapping("/{id}/editar")
    public String procesarEdicion(@PathVariable Long id,
                                  @Valid @ModelAttribute("form") JuegoForm form,
                                  BindingResult resultado,
                                  Model model,
                                  RedirectAttributes redirectAttributes){
        String vista = vistaDeEdicion(model, id);
        if (!resultado.hasErrors()){
            vista = actualizar(id, form, resultado, vista, redirectAttributes);
        }
        return vista;
    }

    private String actualizar(Long id, JuegoForm form, BindingResult resultado,
                              String vistaSiFalla, RedirectAttributes redirectAttributes){
        String vista = vistaSiFalla;
        try{
            juegoService.actualizar(id, form.aDatosDeJuego());
            redirectAttributes.addFlashAttribute("mensajeExito", "Los datos del juego se actualizaron.");
            vista = REDIRECCION_CATALOGO;
        }catch (JuegoYaExisteException e){
            resultado.rejectValue(e.getCampo(), "duplicado", e.getMessage());
        }
        return vista;
    }

    @PostMapping("/{id}/baja")
    public String darDeBaja(@PathVariable Long id, RedirectAttributes redirectAttributes){
        juegoService.darDeBaja(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "El juego se quitó del catálogo.");
        return REDIRECCION_CATALOGO;
    }

    /* La plantilla distingue alta de edición por "modoEdicion" (se declara siempre, en ambos casos). */
    private String vistaDeAlta(Model model){
        model.addAttribute("modoEdicion", false);
        return VISTA_FORMULARIO;
    }

    /* En edición, "idJuego" sirve para armar la URL a la que se envía el formulario. */
    private String vistaDeEdicion(Model model, Long id){
        model.addAttribute("modoEdicion", true);
        model.addAttribute("idJuego", id);
        return VISTA_FORMULARIO;
    }
}
