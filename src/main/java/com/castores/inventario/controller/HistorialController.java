package com.inventario.controller;

import com.inventario.model.Usuario;
import com.inventario.repository.UsuarioRepository;
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

import java.util.Optional;

@Controller
@RequestMapping("/historial")
@RequiredArgsConstructor
@Slf4j
public class HistorialController {
    
    private final MovimientoService movimientoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public String verHistorial(@RequestParam(required = false) TipoMovimiento tipo,
                              Model model, 
                              Authentication authentication) {
        String correo = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        
        log.info("Cargando historial para usuario: {}", authentication.getName());
        try {
            
            if (usuarioOpt.isPresent()) {
                model.addAttribute("nombreUsuario", usuarioOpt.get().getNombre());
            } else {
                model.addAttribute("nombreUsuario", authentication.getName());
            }

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