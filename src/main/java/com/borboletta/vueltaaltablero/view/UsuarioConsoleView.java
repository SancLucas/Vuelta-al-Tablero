package com.borboletta.vueltaaltablero.view;

import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.util.Constants;
import java.util.List;
import java.util.Scanner;

public class UsuarioConsoleView {
    
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println(Constants.MSG_MENU_HEADER);
        System.out.println("1. Crear Usuario");
        System.out.println("2. Listar Usuarios");
        System.out.println("3. Actualizar Usuario");
        System.out.println("4. Eliminar Usuario");
        System.out.println("5. Salir");
    }

    public String readOption() {
        System.out.print("Seleccione una opción: ");
        return scanner.nextLine();
    }

    public String readNombre() {
        System.out.print("Ingrese nombre: ");
        return scanner.nextLine();
    }
    
    public String readApellido() {
        System.out.print("Ingrese apellido: ");
        return scanner.nextLine();
    }

    public String readDni() {
        System.out.print("Ingrese dni: ");
        return scanner.nextLine();
    }

    public String readCorreo() {
        System.out.print("Ingrese correo: ");
        return scanner.nextLine();
    }
    
    public String readContrasenia() {
        System.out.print("Ingrese contrasenia: ");
        return scanner.nextLine();
    }
    
    public String readTelefono() {
        System.out.print("Ingrese telefono: ");
        return scanner.nextLine();
    }

    public Long readId(String prompt) {
        System.out.print(prompt);
        return Long.parseLong(scanner.nextLine());
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayUsuarios(List<Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            displayMessage("No hay usuarios registrados.");
        } else {
            for (Usuario u : usuarios) {
                displayMessage(u.toString());
            }
        }
    }

    public void displayGoodbye() {
        System.out.println("Cerrando aplicación...");
    }
}
