package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.entity.Juego;
import com.borboletta.vueltaaltablero.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuegoService{
    @Autowired
    private JuegoRepository juegoRepository;

    public List<Juego> obtenerTodosLosJuegos(){
        return juegoRepository.findAll();
    }
}