package com.inventario.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
@Slf4j
public class AuthController {
    
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
        log.info("Cargando dashboard para usuario: {}", authentication.getName());
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isAlmacenista = authorities.stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ALMACENISTA"));
        
        model.addAttribute("username", authentication.getName());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isAlmacenista", isAlmacenista);
        
        return "dashboard";
    }
}