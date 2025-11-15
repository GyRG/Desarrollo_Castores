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
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("🚀 ===== INICIANDO DATALOADER =====");
        
        try {
            // Diagnosticar la base de datos
            long totalUsuarios = usuarioRepository.count();
            log.info("📊 Diagnóstico - Total de usuarios en BD: {}", totalUsuarios);
            
            // Crear usuario ADMIN
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                log.info("👤 Creando usuario ADMIN...");
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(Rol.ADMIN));
                usuarioRepository.save(admin);
                log.info("✅ ADMIN creado - usuario: admin, password: admin123");
            } else {
                log.info("ℹ️ ADMIN ya existe");
            }
            
            // Crear usuario ALMACENISTA
            if (usuarioRepository.findByUsername("almacenista").isEmpty()) {
                log.info("👤 Creando usuario ALMACENISTA...");
                Usuario almacenista = new Usuario();
                almacenista.setUsername("almacenista");
                almacenista.setPassword(passwordEncoder.encode("almacen123"));
                almacenista.setRoles(Set.of(Rol.ALMACENISTA));
                usuarioRepository.save(almacenista);
                log.info("✅ ALMACENISTA creado - usuario: almacenista, password: almacen123");
            } else {
                log.info("ℹ️ ALMACENISTA ya existe");
            }
            
            // Verificación final
            long finalCount = usuarioRepository.count();
            log.info("📈 Resumen - Total de usuarios después: {}", finalCount);
            log.info("🎉 ===== DATALOADER COMPLETADO =====");
            
        } catch (Exception e) {
            log.error("💥 ERROR CRÍTICO en DataLoader: ", e);
            throw e;
        }
    }
}