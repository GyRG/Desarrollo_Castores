package com.inventario.controller;

import com.inventario.model.TipoMovimiento;
import com.inventario.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/historial")
@RequiredArgsConstructor
@Slf4j
public class HistorialController {
    
    private final MovimientoService movimientoService;
    
    @GetMapping
    public String verHistorial(@RequestParam(required = false) TipoMovimiento tipo,
                              Model model, 
                              Authentication authentication) {
        log.info("Cargando historial para usuario: {}", authentication.getName());
        try {
            if (tipo != null) {
                model.addAttribute("movimientos", movimientoService.obtenerMovimientosPorTipo(tipo));
                model.addAttribute("tipoFiltro", tipo);
            } else {
                model.addAttribute("movimientos", movimientoService.obtenerTodosMovimientos());
                model.addAttribute("tipoFiltro", null);
            }
            
            model.addAttribute("username", authentication.getName());
            model.addAttribute("tiposMovimiento", TipoMovimiento.values());
            return "historial";
            
        } catch (Exception e) {
            log.error("Error al cargar historial", e);
            model.addAttribute("error", "Error al cargar el historial: " + e.getMessage());
            model.addAttribute("username", authentication.getName());
            return "historial";
        }
    }
}