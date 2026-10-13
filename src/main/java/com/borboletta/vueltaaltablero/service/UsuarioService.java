package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService{
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodosLosUsuarios(){
        return usuarioRepository.findAll();
    }
}