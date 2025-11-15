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
    List<Movimiento> findByTipo(TipoMovimiento tipo);
    List<Movimiento> findByProductoId(Long productoId);
    List<Movimiento> findByUsuario(String usuario);
    List<Movimiento> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Movimiento> findAllByOrderByFechaHoraDesc();
    List<Movimiento> findByTipoAndProductoId(TipoMovimiento tipo, Long productoId);
    
    @Query("SELECT m FROM Movimiento m JOIN m.producto p WHERE " +
           "(:tipo IS NULL OR m.tipo = :tipo) AND " +
           "(:productoId IS NULL OR p.id = :productoId) " +
           "ORDER BY m.fechaHora DESC")
    List<Movimiento> findMovimientosFiltrados(
        @Param("tipo") TipoMovimiento tipo,
        @Param("productoId") Long productoId
    );
}