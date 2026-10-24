package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.dto.CriticaForm;
import com.borboletta.vueltaaltablero.dto.DetalleDeAlquiler;
import com.borboletta.vueltaaltablero.dto.NuevoAlquilerForm;
import com.borboletta.vueltaaltablero.exception.CriticaNoPermitidaException;
import com.borboletta.vueltaaltablero.exception.EjemplarYaAlquiladoException;
import com.borboletta.vueltaaltablero.service.AlquilerService;
import com.borboletta.vueltaaltablero.service.CriticaService;
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

/* Páginas públicas de ejemplares (la lista y el detalle de cada uno), el pedido de alquiler y la
crítica del ejemplar. La gestión de los propios está en MisEjemplaresController. */
@Controller
@RequiredArgsConstructor
public class EjemplarController{

    private static final String VISTA_DETALLE = "ejemplares/detalle";
    private static final String MENSAJE_ALQUILER_REGISTRADO =
            "Tu alquiler quedó registrado. Coordiná la entrega con el dueño con el teléfono de abajo.";

    private final EjemplarService ejemplarService;
    private final AlquilerService alquilerService;
    private final CriticaService criticaService;

    @GetMapping("/ejemplares/{id}")
    public String detalle(@PathVariable Long id, Principal principal, Model model){
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

    /* Al ejemplar solo lo critica quien lo alquiló y devolvió: el servicio lo verifica. Si ya
    tenía una crítica, esta la reemplaza. */
    @PostMapping("/ejemplares/{id}/critica")
    public String criticar(@PathVariable Long id, Principal principal,
                           @Valid @ModelAttribute("criticaForm") CriticaForm form,
                           BindingResult resultado, Model model,
                           RedirectAttributes redirectAttributes){
        String vista = vistaDeDetalle(id, principal, model);
        if (!resultado.hasErrors()){
            try{
                criticaService.criticarEjemplar(id, principal.getName(), form.aDatosDeCritica());
                redirectAttributes.addFlashAttribute("mensajeExito", "Tu crítica quedó guardada.");
                vista = "redirect:/ejemplares/" + id;
            }catch(IllegalArgumentException | CriticaNoPermitidaException e){
                resultado.reject("criticaNoValida", e.getMessage());
            }
        }
        return vista;
    }

    /* Arma lo que necesita la página: el ejemplar, sus críticas y lo que depende de quién mira
    (si es suyo, su alquiler abierto, si puede criticar). Sin sesión esos valores quedan en "no"
    (la plantilla no tolera que falten). Los dos formularios que lleguen ya cargados (con errores
    de un POST) se respetan; el otro se arma de cero. */
    private String vistaDeDetalle(Long id, Principal principal, Model model){
        model.addAttribute("ejemplar", ejemplarService.obtener(id));
        model.addAttribute("resumenCriticas", criticaService.deEjemplar(id));
        boolean esPropio = false;
        boolean puedeCriticar = false;
        DetalleDeAlquiler miAlquiler = null;
        CriticaForm miCritica = new CriticaForm();
        if (principal != null){
            String correo = principal.getName();
            esPropio = ejemplarService.esDe(id, correo);
            miAlquiler = alquilerService.enCursoDe(id, correo).orElse(null);
            puedeCriticar = criticaService.puedeCriticarEjemplar(id, correo);
            if (puedeCriticar){
                miCritica = criticaService.miCriticaDeEjemplar(id, correo)
                        .map(CriticaForm::desde)
                        .orElse(miCritica);
            }
        }
        model.addAttribute("esPropio", esPropio);
        model.addAttribute("miAlquiler", miAlquiler);
        model.addAttribute("puedeCriticar", puedeCriticar);
        if (!model.containsAttribute("form")){
            model.addAttribute("form", new NuevoAlquilerForm());
        }
        if (!model.containsAttribute("criticaForm")){
            model.addAttribute("criticaForm", miCritica);
        }
        return VISTA_DETALLE;
    }
}