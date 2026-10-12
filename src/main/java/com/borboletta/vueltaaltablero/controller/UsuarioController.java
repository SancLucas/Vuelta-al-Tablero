package com.borboletta.vueltaaltablero.controller;

import com.borboletta.vueltaaltablero.exception.UsuarioNotFoundException;
import com.borboletta.vueltaaltablero.view.UsuarioConsoleView;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.service.UsuarioService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController{

    @Autowired
    private UsuarioService usuarioService;
    private final UsuarioConsoleView view;

    public UsuarioController(UsuarioService usuarioService, UsuarioConsoleView view) {
        this.usuarioService = usuarioService;
        this.view = view;
    }
    

    public void start() {
        boolean running = true;
        while (running) {
            view.displayMenu();
            String option = view.readOption();
            switch (option) {
                case "1": handleCreate(); break;
                case "2": handleFindAll(); break;
                case "3": handleUpdate(); break;
                case "4": handleDelete(); break;
                case "5": running = false; view.displayGoodbye(); break;
                default: view.displayMessage("Opción no válida.");
                }
         }
     }

    private void handleCreate() {
        try {
            String nombre = view.readNombre();
            String apellido = view.readApellido();
            String dni = view.readDni();
            String correo = view.readCorreo();
            String contrasenia = view.readContrasenia();
            String telefono = view.readTelefono();
            Usuario usuario = usuarioService.createUsuario(nombre, apellido, dni, correo, contrasenia, telefono, LocalDate.now(), false);
            view.displayMessage("Éxito: Usuario creado con ID " + usuario.getNro());
        } catch (IllegalArgumentException e) {
            view.displayMessage(e.getMessage());
        }
    }

    private void handleFindAll() {
        view.displayMessage("\n--- Lista de Usuarios ---");
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        view.displayUsuarios(usuarios);
    }
    
    private void handleUpdate() {
        try {
            Long idToUpdate = view.readId("Ingrese ID del usuario a actualizar: ");
            String newNombre = view.readNombre();
            String newApellido = view.readApellido();
            String newDni = view.readDni();
            String newCorreo = view.readCorreo();
            String newContrasenia = view.readContrasenia();
            String newTelefono = view.readTelefono();
            usuarioService.updateUsuario(idToUpdate, newNombre, newApellido, newDni, newCorreo, newContrasenia, newTelefono);
            view.displayMessage("Éxito: Usuario actualizado.");
        } catch (UsuarioNotFoundException | IllegalArgumentException e) {
            view.displayMessage(e.getMessage());
        }
    }

    private void handleDelete() {
        try {
            Long idToDelete = view.readId("Ingrese ID del usuario a eliminar: ");
            usuarioService.deleteUsuario(idToDelete);
            view.displayMessage("Éxito: Usuario eliminado.");
        } catch (UsuarioNotFoundException e) {
            view.displayMessage(e.getMessage());
        }
    }

    /*
    @GetMapping("/usuarios")
    public String listUsuarios(Model model){
        List<Usuario> listaDeUsuarios = usuarioService.getAllUsuarios();     
        model.addAttribute("usuarios", listaDeUsuarios);
        return "usuarios/list";
    }*/
}
