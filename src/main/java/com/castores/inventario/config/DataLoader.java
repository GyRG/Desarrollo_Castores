package com.inventario.config;

import com.inventario.model.Rol;
import com.inventario.model.Usuario;
import com.inventario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 ===== INICIANDO DATALOADER - CONFIGURACIÓN DE USUARIOS =====");
        
        try {
            // Actualizar password del ADMIN
            Optional<Usuario> adminOpt = usuarioRepository.findByCorreo("admin@inventario.com");
            if (adminOpt.isPresent()) {
                Usuario admin = adminOpt.get();
                String encodedPassword = passwordEncoder.encode("admin123");
                admin.setContrasena(encodedPassword);
                usuarioRepository.save(admin);
                log.info("✅ ADMIN configurado - correo: admin@inventario.com, password: admin123");
                log.info("🔑 Hash generado: {}", encodedPassword);
            } else {
                log.error("❌ ADMIN no encontrado en la base de datos");
            }

            // Actualizar password del ALMACENISTA
            Optional<Usuario> almacenistaOpt = usuarioRepository.findByCorreo("almacenista@inventario.com");
            if (almacenistaOpt.isPresent()) {
                Usuario almacenista = almacenistaOpt.get();
                String encodedPassword = passwordEncoder.encode("almacen123");
                almacenista.setContrasena(encodedPassword);
                usuarioRepository.save(almacenista);
                log.info("✅ ALMACENISTA configurado - correo: almacenista@inventario.com, password: almacen123");
                log.info("🔑 Hash generado: {}", encodedPassword);
            } else {
                log.error("❌ ALMACENISTA no encontrado en la base de datos");
            }

            log.info("🎉 ===== DATALOADER COMPLETADO =====");
            
        } catch (Exception e) {
            log.error("💥 ERROR en DataLoader: ", e);
        }
    }
}