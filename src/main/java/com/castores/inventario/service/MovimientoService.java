package com.inventario.service;

import com.inventario.model.Movimiento;
import com.inventario.model.TipoMovimiento;
import com.inventario.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimientoService {
    
    private final MovimientoRepository movimientoRepository;
    
    public List<Movimiento> obtenerTodosMovimientos() {
        try {
            List<Movimiento> movimientos = movimientoRepository.findAllByOrderByFechaHoraDesc();
            log.info("📊 Obtenidos {} movimientos del historial", movimientos.size());
            return movimientos;
        } catch (Exception e) {
            log.error("Error al obtener movimientos", e);
            return List.of();
        }
    }
    
    public List<Movimiento> obtenerMovimientosPorTipo(TipoMovimiento tipo) {
        try {
            List<Movimiento> movimientos = movimientoRepository.findByTipo(tipo);
            log.info("📊 Obtenidos {} movimientos de tipo: {}", movimientos.size(), tipo);
            return movimientos;
        } catch (Exception e) {
            log.error("Error al obtener movimientos por tipo: {}", tipo, e);
            return List.of();
        }
    }
    
    public Movimiento guardarMovimiento(Movimiento movimiento) {
        try {
            Movimiento movimientoGuardado = movimientoRepository.save(movimiento);
            log.info("📝 Movimiento guardado - ID: {}, Tipo: {}, Producto: {}", 
                    movimientoGuardado.getId(), movimientoGuardado.getTipo(), 
                    movimientoGuardado.getProducto().getNombre());
            return movimientoGuardado;
        } catch (Exception e) {
            log.error("❌ Error al guardar movimiento", e);
            throw e;
        }
    }
    
    public List<Movimiento> buscarMovimientosFiltrados(TipoMovimiento tipo) {
        try {
            return movimientoRepository.findMovimientosFiltrados(tipo);
        } catch (Exception e) {
            log.error("Error al buscar movimientos filtrados", e);
            return List.of();
        }
    }
}