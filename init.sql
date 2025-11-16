-- init.sql - Estructura con nombres consistentes
CREATE DATABASE IF NOT EXISTS inventario_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE inventario_db;

-- Eliminar tablas si existen (en orden correcto)
DROP TABLE IF EXISTS movimientos;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS usuarios;

-- Tabla de usuarios con nombres de columnas exactos
CREATE TABLE usuarios (
    idUsuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    idRol INT NOT NULL,
    estatus INT NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de productos
CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    cantidad INT NOT NULL DEFAULT 0,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de movimientos
CREATE TABLE movimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('ENTRADA', 'SALIDA') NOT NULL,
    cantidad INT NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(100) NOT NULL,
    producto_id BIGINT NOT NULL,
    observaciones TEXT,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar productos de ejemplo
INSERT INTO productos (nombre, descripcion, cantidad) VALUES 
('Laptop Dell XPS 13', 'Laptop empresarial Intel i7, 16GB RAM, 512GB SSD', 15),
('Monitor Samsung 24"', 'Monitor LED Full HD 24 pulgadas, 75Hz', 25),
('Teclado Mecánico RGB', 'Teclado mecánico gaming con switches blue retroiluminado', 30);

-- Insertar usuarios con passwords CORRECTOS (serán actualizados por DataLoader)
INSERT INTO usuarios (nombre, correo, contrasena, idRol, estatus) VALUES 
('Administrador del Sistema', 'admin@inventario.com', 'temp_password', 1, 1),
('Almacenista Principal', 'almacenista@inventario.com', 'temp_password', 2, 1);

-- Insertar movimientos de ejemplo
INSERT INTO movimientos (tipo, cantidad, usuario, producto_id, observaciones) VALUES 
('ENTRADA', 10, 'admin@inventario.com', 1, 'Compra inicial de laptops');

-- Mostrar información
SELECT '=== ESTRUCTURA CREADA ===' as '';
SELECT 'Usuarios:' as '', COUNT(*) as total FROM usuarios
UNION
SELECT 'Productos:' as '', COUNT(*) as total FROM productos
UNION
SELECT 'Movimientos:' as '', COUNT(*) as total FROM movimientos;