package com.inventario.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    @Column(nullable = false, length = 100)
    private String usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @PrePersist
    protected void onCreate() {
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
    }
    
    public Movimiento(TipoMovimiento tipo, Integer cantidad, String usuario, Producto producto) {
        this();
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.usuario = usuario;
        this.producto = producto;
    }
    
    public Movimiento(TipoMovimiento tipo, Integer cantidad, String usuario, Producto producto, String observaciones) {
        this(tipo, cantidad, usuario, producto);
        this.observaciones = observaciones;
    }
    
    public String getTipoDisplay() {
        return tipo == TipoMovimiento.ENTRADA ? "Entrada" : "Salida";
    }
    
    public String getCantidadDisplay() {
        return (tipo == TipoMovimiento.ENTRADA ? "+" : "-") + cantidad;
    }
}