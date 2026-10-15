package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.dto.RegistroUsuarioForm;
import com.borboletta.vueltaaltablero.dto.IngresoUsuarioForm;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.exception.UsuarioYaRegistradoException;
import com.borboletta.vueltaaltablero.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UsuarioController{

    private static final String VISTA_LISTADO = "usuarios/list";
    private static final String VISTA_REGISTRO = "usuarios/registro";
    private static final String REDIRECCION_LISTADO = "redirect:/usuarios";

    private final UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public String listUsuarios(Model model){
        List<Usuario> listaDeUsuarios = usuarioService.obtenerTodosLosUsuarios();
        model.addAttribute("usuarios", listaDeUsuarios);
        return VISTA_LISTADO;
    }

    @GetMapping("/registro")
    public String mostrarFormulario(Model model){
        model.addAttribute("registroForm", new RegistroUsuarioForm());
        return VISTA_REGISTRO;
    }

    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("registroForm")
                                   RegistroUsuarioForm form,
                                   BindingResult resultado,
                                   RedirectAttributes redirectAttributes){
        String vista = VISTA_REGISTRO;
        if (!resultado.hasErrors()) {
            vista = registrar(form, resultado, redirectAttributes);
        }
        return vista;
    }

    private String registrar(RegistroUsuarioForm form, BindingResult resultado,
                             RedirectAttributes redirectAttributes){
        String vista = VISTA_REGISTRO;
        try{
            usuarioService.registrar(form.aDatosDeRegistro());
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Usuario registrado correctamente!");
            vista = REDIRECCION_LISTADO;
        }catch (UsuarioYaRegistradoException e){
            resultado.rejectValue(e.getCampo(), "duplicado", e.getMessage());
        }
        return vista;
    }
    
    @GetMapping("/usuario/ingreso")
    public String mostrarFormularioIngreso(Model model) {
        model.addAttribute("ingresoForm", new IngresoUsuarioForm());
        return "usuarios/ingreso"; 
    }
    
    @PostMapping("/usuario/ingreso")
    public String procesarIngreso(@ModelAttribute IngresoUsuarioForm ingresoForm, HttpSession session, Model model) {
        String vistaDestino;
        Usuario usuario = usuarioService.autenticar(ingresoForm.getCorreo(), ingresoForm.getContrasenia());

        if (usuario == null) {
            model.addAttribute("error", "Credenciales inválidas o usuario no registrado.");
            vistaDestino = "usuarios/ingreso";
        } else {
            session.setAttribute("usuarioIngresado", usuario);
            vistaDestino = "redirect:/usuario/perfil";
        }
        return vistaDestino;
    }
    
    @GetMapping("/usuario/perfil")
    public String verPerfil(HttpSession session, Model model) {
        String vistaDestino;
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioIngresado");

        if (usuarioSesion == null) {
            vistaDestino = "redirect:/usuario/ingreso";
        } else {
            model.addAttribute("usuario", usuarioSesion);
            vistaDestino = "usuarios/perfil"; 
        }
        return vistaDestino;
    }

    @GetMapping("/salir")
    public String cerrarSesion(HttpSession session) {
        String vistaDestino = "redirect:/";
        session.invalidate();
        return vistaDestino;
    }
}