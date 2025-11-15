package com.inventario.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        model.addAttribute("username", authentication.getName());
        return "dashboard";
    }
}