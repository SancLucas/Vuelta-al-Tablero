package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.entity.Ejemplar;
import com.borboletta.vueltaaltablero.repository.EjemplarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EjemplarService{
    @Autowired
    private EjemplarRepository ejemplarRepository;

    public List<Ejemplar> obtenerTodosLosEjemplares(){
        return ejemplarRepository.findAll();
    }
}