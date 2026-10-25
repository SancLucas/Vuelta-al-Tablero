package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.service.AlquilerService;
import java.security.Principal;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/* Los alquileres de cada persona, vistos desde sus dos lados: lo que alquiló (como inquilino) y
los alquileres de sus ejemplares (como dueño). Quién es sale siempre del Principal (la sesión). */
@Controller
@RequiredArgsConstructor
public class MisAlquileresController{

    private static final String VISTA_COMO_INQUILINO = "alquileres/mis-alquileres";
    private static final String VISTA_DE_MIS_EJEMPLARES = "alquileres/de-mis-ejemplares";
    private static final String REDIRECCION_DE_MIS_EJEMPLARES = "redirect:/mis-ejemplares/alquileres";

    private final AlquilerService alquilerService;

///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    @GetMapping("/mis-alquileres")
    public String comoInquilino(Principal principal, Model model){
        model.addAttribute("alquileres", alquilerService.comoInquilino(principal.getName()));
        return VISTA_COMO_INQUILINO;
    }

    @GetMapping("/mis-ejemplares/alquileres")
    public String deMisEjemplares(Principal principal, Model model){
        model.addAttribute("alquileres", alquilerService.deMisEjemplares(principal.getName()));
        return VISTA_DE_MIS_EJEMPLARES;
    }

    /* Solo el dueño del ejemplar puede registrar la devolución: el servicio busca el
    alquiler por id Y por dueño, así que el de un ejemplar ajeno responde 404. */
    @PostMapping("/mis-ejemplares/alquileres/{id}/devolucion")
    public String registrarDevolucion(Principal principal, @PathVariable Long id,
                                      RedirectAttributes redirectAttributes){
        try{
            alquilerService.registrarDevolucion(id, principal.getName());
            redirectAttributes.addFlashAttribute("mensajeExito", "La devolución quedó registrada.");
        }catch(IllegalArgumentException e){
            // Reglas del alquiler (ya devuelto, o todavía no empezó): se muestran en la lista.
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return REDIRECCION_DE_MIS_EJEMPLARES;
    }

    @PostMapping("/mis-ejemplares/alquileres/{id}/cancelacion")
    public String cancelar(Principal principal, @PathVariable Long id,
                           RedirectAttributes redirectAttributes){
        try{
            alquilerService.cancelar(id, principal.getName());
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "El alquiler se canceló. Avisale a quien lo había pedido.");
        }catch(IllegalArgumentException e){
            // Regla del alquiler (ya empezó): se muestra en la lista.
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return REDIRECCION_DE_MIS_EJEMPLARES;
    }
}