package com.borboletta.vueltaaltablero.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.time.Year;

@ControllerAdvice
public class GlobalControllerAdvice{

    @ModelAttribute("currentYear")
    public int getCurrentYear(){
        return Year.now().getValue();
    }
}