package com.inventario.controller;

import com.inventario.model.Usuario;
import com.inventario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final UsuarioRepository usuarioRepository;
    
    @GetMapping("/")
    public String home() {
        log.info("Redirigiendo a dashboard desde home");
        return "redirect:/dashboard";
    }
    
    @GetMapping("/login")
    public String login() {
        log.info("Cargando página de login");
        return "login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String correo = authentication.getName();
        log.info("Cargando dashboard para usuario: {}", correo);
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("correoUsuario", usuario.getCorreo());
            model.addAttribute("isAdmin", usuario.getRol() == com.inventario.model.Rol.ADMIN);
            model.addAttribute("isAlmacenista", usuario.getRol() == com.inventario.model.Rol.ALMACENISTA);
        } else {
            model.addAttribute("nombreUsuario", authentication.getName());
            model.addAttribute("correoUsuario", authentication.getName());
            model.addAttribute("isAdmin", false);
            model.addAttribute("isAlmacenista", false);
        }
        
        return "dashboard";
    }
}