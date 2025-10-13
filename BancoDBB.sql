-- =====================================
-- 1️⃣ Crear base de datos
-- =====================================
CREATE DATABASE IF NOT EXISTS BancoDBB;
USE BancoDBB;
USE BancoDB;

USE BancoDBB;

SELECT 
    id AS UsuarioID,
    nombre,
    apellidos,
    email,
    fecha_registro
FROM usuarios
ORDER BY id;

-- Mostrar el ID del usuario, su nombre y el saldo de su cuenta
SELECT 
    c.id AS CuentaID,
    u.id AS UsuarioID,
    u.nombre,
    u.apellidos,
    c.saldo
FROM cuentas c
JOIN usuarios u ON c.usuario_id = u.id
ORDER BY c.id;



-- =====================================
-- 2️⃣ Borrar procedimientos y tablas si existen
-- =====================================
DROP PROCEDURE IF EXISTS registrar_usuario;
DROP PROCEDURE IF EXISTS abrir_cuenta;
DROP PROCEDURE IF EXISTS transferir;

DROP TABLE IF EXISTS transferencias;
DROP TABLE IF EXISTS cuentas;
DROP TABLE IF EXISTS usuarios;

-- =====================================
-- 3️⃣ Crear tabla de usuarios
-- =====================================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================
-- 4️⃣ Crear tabla de cuentas
-- =====================================
CREATE TABLE cuentas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    saldo DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    fecha_apertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- =====================================
-- 5️⃣ Crear tabla de transferencias
-- =====================================
CREATE TABLE transferencias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cuenta_emisora INT NOT NULL,
    cuenta_receptora INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    nota VARCHAR(255),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_emisora) REFERENCES cuentas(id),
    FOREIGN KEY (cuenta_receptora) REFERENCES cuentas(id)
);

-- =====================================
-- 6️⃣ Procedimiento: registrar_usuario
-- =====================================
DELIMITER //
CREATE PROCEDURE registrar_usuario(
    IN p_email VARCHAR(100),
    IN p_nombre VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_password VARCHAR(255)
)
BEGIN
    INSERT INTO usuarios (email, nombre, apellidos, password)
    VALUES (p_email, p_nombre, p_apellidos, p_password);
END //
DELIMITER ;

-- =====================================
-- 7️⃣ Procedimiento: abrir_cuenta
-- =====================================
DELIMITER //
CREATE PROCEDURE abrir_cuenta(
    IN p_usuario_id INT,
    IN p_saldo_inicial DECIMAL(10,2)
)
BEGIN
    INSERT INTO cuentas (usuario_id, saldo)
    VALUES (p_usuario_id, p_saldo_inicial);
END //
DELIMITER ;

-- =====================================
-- 8️⃣ Procedimiento: transferir
-- =====================================
DELIMITER //
CREATE PROCEDURE transferir(
    IN p_cuenta_emisora INT,
    IN p_cuenta_receptora INT,
    IN p_monto DECIMAL(10,2),
    IN p_nota VARCHAR(255)
)
BEGIN
    DECLARE saldo_actual DECIMAL(10,2);

    -- Verificar saldo suficiente
    SELECT saldo INTO saldo_actual FROM cuentas WHERE id = p_cuenta_emisora;

    IF saldo_actual >= p_monto THEN
        -- Restar del emisor
        UPDATE cuentas
        SET saldo = saldo - p_monto
        WHERE id = p_cuenta_emisora;

        -- Sumar al receptor
        UPDATE cuentas
        SET saldo = saldo + p_monto
        WHERE id = p_cuenta_receptora;

        -- Registrar la transferencia
        INSERT INTO transferencias (cuenta_emisora, cuenta_receptora, monto, nota)
        VALUES (p_cuenta_emisora, p_cuenta_receptora, p_monto, p_nota);
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Saldo insuficiente para realizar la transferencia';
    END IF;
END //
DELIMITER ;
