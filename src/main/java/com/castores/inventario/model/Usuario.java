package com.inventario.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(nullable = false, length = 255)
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "usuario_roles", 
        joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private Set<Rol> roles = new HashSet<>();
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
    
    // Constructor para facilitar la creación
    public Usuario(String username, String password, Set<Rol> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}