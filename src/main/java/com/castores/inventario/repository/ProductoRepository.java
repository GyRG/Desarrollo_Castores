package com.inventario.repository;

import com.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    List<Producto> findByActivoFalse();
    List<Producto> findByActivo(Boolean activo);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.nombre LIKE %:nombre%")
    List<Producto> buscarProductosActivosPorNombre(@org.springframework.data.repository.query.Param("nombre") String nombre);
    
    Optional<Producto> findByNombre(String nombre);
}