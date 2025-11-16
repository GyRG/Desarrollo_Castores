package com.inventario.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/login", "/css/**", "/js/**", "/error").permitAll()
                
                // Módulo de Inventario - Ambos roles
                .requestMatchers("/inventario").hasAnyRole("ADMIN", "ALMACENISTA")
                
                // Funciones solo para ADMIN en inventario
                .requestMatchers("/inventario/agregar").hasRole("ADMIN")
                .requestMatchers("/inventario/entrada/**").hasRole("ADMIN")
                .requestMatchers("/inventario/baja/**").hasRole("ADMIN")
                .requestMatchers("/inventario/reactivar/**").hasRole("ADMIN")
                .requestMatchers("/inventario/editar/**").hasRole("ADMIN")
                
                // Módulo de Salida - Solo ALMACENISTA
                .requestMatchers("/salida/**").hasRole("ALMACENISTA")
                
                // Módulo de Historial - Solo ADMIN
                .requestMatchers("/historial/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}