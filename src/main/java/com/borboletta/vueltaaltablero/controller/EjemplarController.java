package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.dto.DetalleDeAlquiler;
import com.borboletta.vueltaaltablero.dto.NuevoAlquilerForm;
import com.borboletta.vueltaaltablero.exception.EjemplarYaAlquiladoException;
import com.borboletta.vueltaaltablero.service.AlquilerService;
import com.borboletta.vueltaaltablero.service.EjemplarService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/* Páginas públicas de ejemplares (la lista y el detalle de cada uno) y el pedido de alquiler.
La gestión de los propios está en MisEjemplaresController. */
@Controller
@RequiredArgsConstructor
public class EjemplarController{

    private static final String VISTA_LISTA = "ejemplares/lista";
    private static final String VISTA_DETALLE = "ejemplares/detalle";
    private static final String MENSAJE_ALQUILER_REGISTRADO =
            "Tu alquiler quedó registrado. Coordiná la entrega con el dueño con el teléfono de abajo.";

    private final EjemplarService ejemplarService;
    private final AlquilerService alquilerService;

    @GetMapping("/ejemplares")
    public String disponibles(Model model){
        model.addAttribute("ejemplares", ejemplarService.disponiblesParaAlquilar());
        return VISTA_LISTA;
    }

    @GetMapping("/ejemplares/{id}")
    public String detalle(@PathVariable Long id, Principal principal, Model model){
        model.addAttribute("form", new NuevoAlquilerForm());
        return vistaDeDetalle(id, principal, model);
    }

    /* Quién alquila sale de la sesión (principal), nunca del formulario. Sin sesión, Spring
    Security ya redirige al ingreso porque este POST no es público. */
    @PostMapping("/ejemplares/{id}/alquilar")
    public String alquilar(@PathVariable Long id, Principal principal,
                           @Valid @ModelAttribute("form") NuevoAlquilerForm form,
                           BindingResult resultado, Model model,
                           RedirectAttributes redirectAttributes){
        String vista = vistaDeDetalle(id, principal, model);
        if (!resultado.hasErrors()){
            try{
                alquilerService.alquilar(id, principal.getName(), form.aDatosDeNuevoAlquiler());
                redirectAttributes.addFlashAttribute("mensajeExito", MENSAJE_ALQUILER_REGISTRADO);
                vista = "redirect:/ejemplares/" + id;
            }catch(IllegalArgumentException | EjemplarYaAlquiladoException e){
                // Reglas del alquiler (fechas, ejemplar propio u ocupado): se muestran en el formulario.
                resultado.reject("alquilerNoPermitido", e.getMessage());
            }
        }
        return vista;
    }

    /* Arma lo que necesita la página: el ejemplar, si es del visitante y su alquiler abierto
    sobre él. Sin sesión esos dos últimos quedan en "no" (la plantilla no tolera que falten). */
    private String vistaDeDetalle(Long id, Principal principal, Model model){
        model.addAttribute("ejemplar", ejemplarService.obtener(id));
        boolean esPropio = false;
        DetalleDeAlquiler miAlquiler = null;
        if (principal != null){
            esPropio = ejemplarService.esDe(id, principal.getName());
            miAlquiler = alquilerService.enCursoDe(id, principal.getName()).orElse(null);
        }
        model.addAttribute("esPropio", esPropio);
        model.addAttribute("miAlquiler", miAlquiler);
        return VISTA_DETALLE;
    }
}