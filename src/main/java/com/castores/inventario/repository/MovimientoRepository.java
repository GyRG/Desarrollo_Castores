package com.inventario.repository;

import com.inventario.model.Movimiento;
import com.inventario.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    
    // Buscar movimientos por tipo
    List<Movimiento> findByTipo(TipoMovimiento tipo);
    
    // Buscar movimientos por producto
    List<Movimiento> findByProductoId(Long productoId);
    
    // Buscar movimientos por usuario
    List<Movimiento> findByUsuario(String usuario);
    
    // Buscar movimientos por fecha range
    List<Movimiento> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Buscar movimientos ordenados por fecha descendente
    List<Movimiento> findAllByOrderByFechaHoraDesc();
    
    // Buscar movimientos por tipo y producto
    List<Movimiento> findByTipoAndProductoId(TipoMovimiento tipo, Long productoId);
    
    // Consulta personalizada para historial con joins
    @Query("SELECT m FROM Movimiento m JOIN FETCH m.producto p WHERE " +
           "(:tipo IS NULL OR m.tipo = :tipo) " +
           "ORDER BY m.fechaHora DESC")
    List<Movimiento> findMovimientosFiltrados(@Param("tipo") TipoMovimiento tipo);
}