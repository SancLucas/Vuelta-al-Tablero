package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.dto.CriticaForm;
import com.borboletta.vueltaaltablero.entity.CategoriaJuego;
import com.borboletta.vueltaaltablero.service.CriticaService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/* Catálogo público: cualquiera puede mirarlo, solo con GET. Dejar una crítica pide sesión (el POST
no es público). La administración está en JuegoAdminController. */
@Controller
@RequestMapping("/juegos")
@RequiredArgsConstructor
public class JuegoController{

    private static final String VISTA_LISTA = "juegos/lista";
    private static final String VISTA_DETALLE = "juegos/detalle";

    private final JuegoService juegoService;
    private final EjemplarService ejemplarService;
    private final CriticaService criticaService;

    /* /juegos muestra todos; /juegos?categoria=ESTRATEGIA filtra. Una categoría inventada no rompe: no filtra. */
    @GetMapping
    public String catalogo(@RequestParam(required = false) String categoria, Model model){
        CategoriaJuego filtro = CategoriaJuego.buscarPorCodigo(categoria).orElse(null);
        model.addAttribute("juegos", juegoService.catalogo(filtro));
        model.addAttribute("categorias", CategoriaJuego.values());
        model.addAttribute("categoriaActual", filtro);
        return VISTA_LISTA;
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Principal principal, Model model){
        return vistaDeDetalle(id, principal, model);
    }

    /* Al juego lo puede criticar cualquier persona con sesión. Quién es sale del Principal. Si ya
    tenía una crítica, esta la reemplaza (el servicio hace el "actualizar o crear"). */
    @PostMapping("/{id}/critica")
    public String criticar(@PathVariable Long id, Principal principal,
                           @Valid @ModelAttribute("criticaForm") CriticaForm form,
                           BindingResult resultado, Model model,
                           RedirectAttributes redirectAttributes){
        String vista = vistaDeDetalle(id, principal, model);
        if (!resultado.hasErrors()){
            try{
                criticaService.criticarJuego(id, principal.getName(), form.aDatosDeCritica());
                redirectAttributes.addFlashAttribute("mensajeExito", "Tu crítica quedó guardada.");
                vista = "redirect:/juegos/" + id;
            }catch(IllegalArgumentException e){
                resultado.reject("criticaNoValida", e.getMessage());
            }
        }
        return vista;
    }

    /* Arma lo que necesita la página. Si llega un "criticaForm" ya cargado (con errores de un POST)
    se respeta; si no, se muestra el de la crítica propia, o uno vacío. */
    private String vistaDeDetalle(Long id, Principal principal, Model model){
        model.addAttribute("juego", juegoService.obtener(id));
        model.addAttribute("ejemplares", ejemplarService.disponiblesDeJuego(id));
        model.addAttribute("resumenCriticas", criticaService.deJuego(id));
        if (!model.containsAttribute("criticaForm")){
            CriticaForm propia = new CriticaForm();
            if (principal != null){
                propia = criticaService.miCriticaDeJuego(id, principal.getName())
                        .map(CriticaForm::desde)
                        .orElse(propia);
            }
            model.addAttribute("criticaForm", propia);
        }
        return VISTA_DETALLE;
    }
}