package com.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long idUsuario;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "correo", nullable = false, length = 50, unique = true)
    private String correo;
    
    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;
    
    @Column(name = "idRol", nullable = false)
    private Integer idRol;
    
    @Column(name = "estatus", nullable = false)
    private Integer estatus = 1;
    
    // Método para obtener el rol como enum
    public Rol getRol() {
        return Rol.fromId(this.idRol);
    }
    
    // Método para verificar si el usuario está activo
    public boolean isActivo() {
        return this.estatus == 1;
    }
}