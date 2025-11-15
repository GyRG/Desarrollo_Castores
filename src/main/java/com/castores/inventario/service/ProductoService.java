package com.inventario.service;

import com.inventario.model.Producto;
import com.inventario.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    
    public List<Producto> obtenerTodosProductos() {
        try {
            return productoRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener productos", e);
            return List.of();
        }
    }
    
    public List<Producto> obtenerProductosActivos() {
        try {
            return productoRepository.findByActivoTrue();
        } catch (Exception e) {
            log.error("Error al obtener productos activos", e);
            return List.of();
        }
    }
    
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }
}