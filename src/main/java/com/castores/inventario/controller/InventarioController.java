package com.inventario.controller;

import com.inventario.model.Producto;
import com.inventario.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventario")
@RequiredArgsConstructor
@Slf4j
public class InventarioController {
    
    private final ProductoService productoService;
    
    // Mostrar inventario con filtro
    @GetMapping
    public String verInventario(@RequestParam(defaultValue = "true") Boolean activos, 
                               Model model, 
                               Authentication authentication) {
        log.info("Cargando inventario para usuario: {}", authentication.getName());
        try {
            if (activos) {
                model.addAttribute("productos", productoService.obtenerProductosActivos());
            } else {
                model.addAttribute("productos", productoService.obtenerTodosProductos());
            }
            model.addAttribute("producto", new Producto());
            model.addAttribute("username", authentication.getName());
            model.addAttribute("mostrarActivos", activos);
            return "inventario";
        } catch (Exception e) {
            log.error("Error al cargar inventario", e);
            model.addAttribute("error", "Error al cargar el inventario: " + e.getMessage());
            model.addAttribute("username", authentication.getName());
            return "inventario";
        }
    }
    
    // CREAR - Agregar nuevo producto
    @PostMapping("/agregar")
    public String agregarProducto(@ModelAttribute Producto producto,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            productoService.guardarProducto(producto);
            redirectAttributes.addFlashAttribute("mensaje", 
                "✅ Producto agregado exitosamente: " + producto.getNombre());
        } catch (Exception e) {
            log.error("Error al agregar producto", e);
            redirectAttributes.addFlashAttribute("error", 
                "❌ Error al agregar producto: " + e.getMessage());
        }
        return "redirect:/inventario";
    }
    
    // UPDATE - Formulario para editar producto
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication authentication) {
        try {
            var productoOpt = productoService.obtenerProductoPorId(id);
            if (productoOpt.isPresent()) {
                model.addAttribute("producto", productoOpt.get());
                model.addAttribute("username", authentication.getName());
                return "editar-producto";
            } else {
                return "redirect:/inventario?error=Producto no encontrado";
            }
        } catch (Exception e) {
            log.error("Error al cargar formulario de edición", e);
            return "redirect:/inventario?error=" + e.getMessage();
        }
    }
    
    // UPDATE - Procesar edición
    @PostMapping("/editar/{id}")
    public String actualizarProducto(@PathVariable Long id,
                                    @ModelAttribute Producto producto,
                                    RedirectAttributes redirectAttributes) {
        try {
            productoService.actualizarProducto(id, producto);
            redirectAttributes.addFlashAttribute("mensaje", 
                "✅ Producto actualizado exitosamente: " + producto.getNombre());
        } catch (Exception e) {
            log.error("Error al actualizar producto", e);
            redirectAttributes.addFlashAttribute("error", 
                "❌ Error al actualizar producto: " + e.getMessage());
        }
        return "redirect:/inventario";
    }
    
    // DELETE LÓGICO - Dar de baja producto
    @PostMapping("/baja/{id}")
    public String darDeBajaProducto(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.darDeBajaProducto(id);
            redirectAttributes.addFlashAttribute("mensaje", 
                "✅ Producto dado de baja: " + producto.getNombre());
        } catch (Exception e) {
            log.error("Error al dar de baja producto", e);
            redirectAttributes.addFlashAttribute("error", 
                "❌ Error al dar de baja producto: " + e.getMessage());
        }
        return "redirect:/inventario";
    }
    
    // REACTIVAR producto
    @PostMapping("/reactivar/{id}")
    public String reactivarProducto(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.reactivarProducto(id);
            redirectAttributes.addFlashAttribute("mensaje", 
                "✅ Producto reactivado: " + producto.getNombre());
        } catch (Exception e) {
            log.error("Error al reactivar producto", e);
            redirectAttributes.addFlashAttribute("error", 
                "❌ Error al reactivar producto: " + e.getMessage());
        }
        return "redirect:/inventario?activos=false";
    }
    
    // ENTRADA de inventario
    @PostMapping("/entrada/{id}")
    public String entradaProducto(@PathVariable Long id,
                                 @RequestParam Integer cantidad,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.registrarEntrada(id, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", 
                "✅ Entrada registrada: +" + cantidad + " unidades de " + producto.getNombre());
        } catch (Exception e) {
            log.error("Error al registrar entrada", e);
            redirectAttributes.addFlashAttribute("error", 
                "❌ Error al registrar entrada: " + e.getMessage());
        }
        return "redirect:/inventario";
    }
    
    // SALIDA de inventario
    @PostMapping("/salida/{id}")
    public String salidaProducto(@PathVariable Long id,
                                @RequestParam Integer cantidad,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.registrarSalida(id, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", 
                "✅ Salida registrada: -" + cantidad + " unidades de " + producto.getNombre());
        } catch (Exception e) {
            log.error("Error al registrar salida", e);
            redirectAttributes.addFlashAttribute("error", 
                "❌ Error al registrar salida: " + e.getMessage());
        }
        return "redirect:/inventario";
    }
}