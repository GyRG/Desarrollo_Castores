package com.inventario.controller;

import com.inventario.model.Producto;
import com.inventario.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventario")
@RequiredArgsConstructor
@Slf4j
public class InventarioController {
    
    private final ProductoService productoService;
    
    @GetMapping
    public String verInventario(Model model, Authentication authentication) {
        log.info("Cargando inventario para usuario: {}", authentication.getName());
        try {
            model.addAttribute("productos", productoService.obtenerTodosProductos());
            model.addAttribute("producto", new Producto());
            model.addAttribute("username", authentication.getName());
            return "inventario";
        } catch (Exception e) {
            log.error("Error al cargar inventario", e);
            model.addAttribute("error", "Error al cargar el inventario");
            model.addAttribute("username", authentication.getName());
            return "inventario";
        }
    }
}