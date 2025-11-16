package com.inventario.service;

import com.inventario.model.Movimiento;
import com.inventario.model.Producto;
import com.inventario.model.TipoMovimiento;
import com.inventario.repository.ProductoRepository;
import com.inventario.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    private final MovimientoRepository movimientoRepository; // Agregado
    
    // CREATE - Agregar nuevo producto
    public Producto guardarProducto(Producto producto) {
        try {
            if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
            }
            
            if (producto.getCantidad() == null) {
                producto.setCantidad(0);
            }
            if (producto.getActivo() == null) {
                producto.setActivo(true);
            }
            
            Producto productoGuardado = productoRepository.save(producto);
            log.info("✅ Producto guardado: {}", productoGuardado.getNombre());
            return productoGuardado;
            
        } catch (Exception e) {
            log.error("❌ Error al guardar producto: {}", e.getMessage());
            throw e;
        }
    }
    
    // READ - Obtener todos los productos
    public List<Producto> obtenerTodosProductos() {
        try {
            return productoRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener productos", e);
            return List.of();
        }
    }
    
    // READ - Obtener productos activos
    public List<Producto> obtenerProductosActivos() {
        try {
            return productoRepository.findByActivoTrue();
        } catch (Exception e) {
            log.error("Error al obtener productos activos", e);
            return List.of();
        }
    }
    
    // READ - Obtener producto por ID
    public Optional<Producto> obtenerProductoPorId(Long id) {
        try {
            return productoRepository.findById(id);
        } catch (Exception e) {
            log.error("Error al obtener producto con ID: {}", id, e);
            return Optional.empty();
        }
    }
    
    // UPDATE - Actualizar producto
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        try {
            Optional<Producto> productoExistente = productoRepository.findById(id);
            
            if (productoExistente.isPresent()) {
                Producto producto = productoExistente.get();
                
                producto.setNombre(productoActualizado.getNombre());
                producto.setDescripcion(productoActualizado.getDescripcion());
                producto.setCantidad(productoActualizado.getCantidad());
                producto.setActivo(productoActualizado.getActivo());
                
                Producto productoActualizadoDb = productoRepository.save(producto);
                log.info("✅ Producto actualizado: {}", productoActualizadoDb.getNombre());
                return productoActualizadoDb;
            } else {
                throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
            }
            
        } catch (Exception e) {
            log.error("❌ Error al actualizar producto: {}", e.getMessage());
            throw e;
        }
    }
    
    // DELETE LÓGICO - Dar de baja producto
    public Producto darDeBajaProducto(Long id) {
        try {
            Optional<Producto> productoOpt = productoRepository.findById(id);
            
            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();
                producto.setActivo(false);
                
                Producto productoActualizado = productoRepository.save(producto);
                log.info("✅ Producto dado de baja: {}", productoActualizado.getNombre());
                return productoActualizado;
            } else {
                throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
            }
            
        } catch (Exception e) {
            log.error("❌ Error al dar de baja producto: {}", e.getMessage());
            throw e;
        }
    }
    
    // REACTIVAR producto
    public Producto reactivarProducto(Long id) {
        try {
            Optional<Producto> productoOpt = productoRepository.findById(id);
            
            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();
                producto.setActivo(true);
                
                Producto productoActualizado = productoRepository.save(producto);
                log.info("✅ Producto reactivado: {}", productoActualizado.getNombre());
                return productoActualizado;
            } else {
                throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
            }
            
        } catch (Exception e) {
            log.error("❌ Error al reactivar producto: {}", e.getMessage());
            throw e;
        }
    }
    
    // ENTRADA de inventario CON registro de movimiento
    public Producto registrarEntrada(Long id, Integer cantidad, String usuario, String observaciones) {
        try {
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
            }
            
            Optional<Producto> productoOpt = productoRepository.findById(id);
            
            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();
                
                if (!producto.getActivo()) {
                    throw new IllegalArgumentException("No se puede agregar inventario a un producto inactivo");
                }
                
                producto.setCantidad(producto.getCantidad() + cantidad);
                Producto productoActualizado = productoRepository.save(producto);
                
                // REGISTRAR MOVIMIENTO
                Movimiento movimiento = new Movimiento(
                    TipoMovimiento.ENTRADA, 
                    cantidad, 
                    usuario, 
                    productoActualizado,
                    observaciones != null ? observaciones : "Entrada de inventario"
                );
                movimientoRepository.save(movimiento);
                log.info("📝 Movimiento de ENTRADA registrado para producto: {}", productoActualizado.getNombre());
                
                log.info("✅ Entrada registrada: {} unidades de {}", cantidad, productoActualizado.getNombre());
                return productoActualizado;
            } else {
                throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
            }
            
        } catch (Exception e) {
            log.error("❌ Error al registrar entrada: {}", e.getMessage());
            throw e;
        }
    }
    
    // SALIDA de inventario CON registro de movimiento
    public Producto registrarSalida(Long id, Integer cantidad, String usuario, String observaciones) {
        try {
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
            }
            
            Optional<Producto> productoOpt = productoRepository.findById(id);
            
            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();
                
                if (!producto.getActivo()) {
                    throw new IllegalArgumentException("No se puede retirar inventario de un producto inactivo");
                }
                
                if (producto.getCantidad() < cantidad) {
                    throw new IllegalArgumentException(
                        String.format("No hay suficiente inventario. Disponible: %d, Solicitado: %d", 
                        producto.getCantidad(), cantidad)
                    );
                }
                
                producto.setCantidad(producto.getCantidad() - cantidad);
                Producto productoActualizado = productoRepository.save(producto);
                
                // REGISTRAR MOVIMIENTO
                Movimiento movimiento = new Movimiento(
                    TipoMovimiento.SALIDA, 
                    cantidad, 
                    usuario, 
                    productoActualizado,
                    observaciones != null ? observaciones : "Salida de inventario"
                );
                movimientoRepository.save(movimiento);
                log.info("📝 Movimiento de SALIDA registrado para producto: {}", productoActualizado.getNombre());
                
                log.info("✅ Salida registrada: {} unidades de {}", cantidad, productoActualizado.getNombre());
                return productoActualizado;
            } else {
                throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
            }
            
        } catch (Exception e) {
            log.error("❌ Error al registrar salida: {}", e.getMessage());
            throw e;
        }
    }
    
    // ENTRADA simple (para compatibilidad con código existente)
    public Producto registrarEntrada(Long id, Integer cantidad) {
        return registrarEntrada(id, cantidad, "Sistema", "Entrada manual");
    }
    
    // SALIDA simple (para compatibilidad con código existente)
    public Producto registrarSalida(Long id, Integer cantidad) {
        return registrarSalida(id, cantidad, "Sistema", "Salida manual");
    }
    
    // Buscar productos por nombre
    public List<Producto> buscarProductosPorNombre(String nombre) {
        try {
            return productoRepository.buscarProductosActivosPorNombre(nombre);
        } catch (Exception e) {
            log.error("Error al buscar productos por nombre: {}", nombre, e);
            return List.of();
        }
    }
}