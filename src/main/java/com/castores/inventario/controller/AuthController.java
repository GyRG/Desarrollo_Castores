package com.inventario.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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
    public String dashboard() {
        log.info("Cargando dashboard");
        return "dashboard";
    }
}