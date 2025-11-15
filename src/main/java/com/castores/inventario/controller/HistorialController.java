package com.inventario.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/historial")
@Slf4j
public class HistorialController {
    
    @GetMapping
    public String verHistorial(Model model) {
        log.info("Cargando página de historial");
        try {
            model.addAttribute("movimientos", new ArrayList<>());
            return "historial";
        } catch (Exception e) {
            log.error("Error al cargar historial", e);
            model.addAttribute("error", "Error al cargar el historial");
            return "historial";
        }
    }
}