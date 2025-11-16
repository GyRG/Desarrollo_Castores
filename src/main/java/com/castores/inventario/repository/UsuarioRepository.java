package com.inventario.repository;

import com.inventario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Usar consulta nativa para evitar problemas de mapeo
    @Query(value = "SELECT * FROM usuarios u WHERE u.correo = :correo AND u.estatus = 1", nativeQuery = true)
    Optional<Usuario> findByCorreo(@Param("correo") String correo);
    
    // Verificar si existe un correo
    @Query(value = "SELECT COUNT(*) > 0 FROM usuarios u WHERE u.correo = :correo", nativeQuery = true)
    Boolean existsByCorreo(@Param("correo") String correo);
    
    // Buscar por nombre
    @Query(value = "SELECT * FROM usuarios u WHERE u.nombre = :nombre", nativeQuery = true)
    Optional<Usuario> findByNombre(@Param("nombre") String nombre);
}