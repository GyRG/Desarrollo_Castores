package com.inventario.service;

import com.inventario.model.Movimiento;
import com.inventario.model.TipoMovimiento;
import com.inventario.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoService {
    
    @Autowired
    private MovimientoRepository movimientoRepository;
    
    public List<Movimiento> obtenerTodosMovimientos() {
        return movimientoRepository.findAllByOrderByFechaHoraDesc();
    }
    
    public List<Movimiento> obtenerMovimientosPorTipo(TipoMovimiento tipo) {
        return movimientoRepository.findByTipo(tipo);
    }
    
    public List<Movimiento> obtenerMovimientosPorProducto(Long productoId) {
        return movimientoRepository.findByProductoId(productoId);
    }
    
    public List<Movimiento> obtenerMovimientosPorUsuario(String usuario) {
        return movimientoRepository.findByUsuario(usuario);
    }
    
    public Movimiento guardarMovimiento(Movimiento movimiento) {
        return movimientoRepository.save(movimiento);
    }
    
    public List<Movimiento> buscarMovimientosFiltrados(TipoMovimiento tipo, Long productoId) {
        return movimientoRepository.findMovimientosFiltrados(tipo, productoId);
    }
}