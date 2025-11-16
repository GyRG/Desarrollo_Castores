package com.inventario.controller;

import com.inventario.model.Usuario;
import com.inventario.repository.UsuarioRepository;
import com.inventario.model.Producto;
import com.inventario.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/salida")
@RequiredArgsConstructor
@Slf4j
public class SalidaController {
    
    private final ProductoService productoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public String salidaProductos(Model model, Authentication authentication) {
    
        String correo = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        log.info("Cargando módulo de salida para usuario: {}", authentication.getName());
        try {

            if (usuarioOpt.isPresent()) {
                model.addAttribute("nombreUsuario", usuarioOpt.get().getNombre());
            } else {
                model.addAttribute("nombreUsuario", authentication.getName());
            }

            model.addAttribute("productos", productoService.obtenerProductosActivos());
            model.addAttribute("username", authentication.getName());
            return "salida";
        } catch (Exception e) {
            log.error("Error al cargar módulo de salida", e);
            model.addAttribute("error", "Error al cargar los productos: " + e.getMessage());
            model.addAttribute("username", authentication.getName());
            return "salida";
        }
    }
    
    @PostMapping("/retirar/{id}")
public String retirarProducto(@PathVariable Long id,
                             @RequestParam Integer cantidad,
                             @RequestParam(required = false) String observaciones,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
    try {
        String usuario = authentication.getName();
        Producto producto = productoService.registrarSalida(id, cantidad, usuario, observaciones);
        
        redirectAttributes.addFlashAttribute("mensaje", 
            "✅ Salida registrada: " + cantidad + " unidades de " + producto.getNombre() +
            (observaciones != null && !observaciones.isEmpty() ? " - " + observaciones : ""));
            
    } catch (Exception e) {
        log.error("Error al retirar producto", e);
        redirectAttributes.addFlashAttribute("error", 
            "❌ Error al retirar producto: " + e.getMessage());
    }
    return "redirect:/salida";
}
}