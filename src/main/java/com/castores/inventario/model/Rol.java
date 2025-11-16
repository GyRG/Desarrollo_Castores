package com.inventario.model;

import lombok.Getter;

@Getter
public enum Rol {
    ADMIN(1, "ROLE_ADMIN"),
    ALMACENISTA(2, "ROLE_ALMACENISTA");
    
    private final Integer id;
    private final String authority;
    
    Rol(Integer id, String authority) {
        this.id = id;
        this.authority = authority;
    }
    
    public static Rol fromId(Integer id) {
        for (Rol rol : values()) {
            if (rol.getId().equals(id)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("ID de rol no válido: " + id);
    }
    
    public static Rol fromAuthority(String authority) {
        for (Rol rol : values()) {
            if (rol.getAuthority().equals(authority)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("Authority no válido: " + authority);
    }
}