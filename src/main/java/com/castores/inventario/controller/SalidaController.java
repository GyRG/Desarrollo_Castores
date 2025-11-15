package com.inventario.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/salida")
@Slf4j
public class SalidaController {
    
    @GetMapping
    public String salidaProductos(Model model) {
        log.info("Cargando página de salida de productos");
        model.addAttribute("mensaje", "Módulo de salida de productos - En desarrollo");
        return "salida";
    }
}