package com.inventario.security;

import com.inventario.model.Usuario;
import com.inventario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        log.info("🔍 Buscando usuario por correo: {}", correo);
        
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> {
                    log.error("❌ Usuario no encontrado con correo: {}", correo);
                    return new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
                });

        log.info("✅ Usuario encontrado: {}", usuario.getNombre());
        log.info("🔑 Contraseña en BD: {}", usuario.getContrasena());
        log.info("👥 Rol del usuario: {}", usuario.getRol());
        log.info("📊 Estatus: {}", usuario.getEstatus());

        if (!usuario.isActivo()) {
            log.error("❌ Usuario inactivo: {}", correo);
            throw new UsernameNotFoundException("Usuario inactivo: " + correo);
        }

        // Crear authorities basado en el idRol
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(usuario.getRol().getAuthority())
        );

        log.info("🎯 Authorities asignadas: {}", authorities);

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getContrasena())
                .authorities(authorities)
                .build();
    }
}