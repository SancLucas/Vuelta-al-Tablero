package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.entity.FichaTecnica;
import com.borboletta.vueltaaltablero.repository.FichaTecnicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichaTecnicaService{
    @Autowired
    private FichaTecnicaRepository fichaTecnicaRepository;

    public List<FichaTecnica> obtenerTodasLasFichasTecnicas(){
        return fichaTecnicaRepository.findAll();
    }
}