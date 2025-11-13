-- ##############################################################
-- ETAPA 1 - Modelado y definición de Constraints (Corregido)
-- ##############################################################

USE `tpi-bd-i`;

/* ================== BORRADO PREVIO (ORDEN CORRECTO) ================== */
-- Para poder ejecutar este script varias veces, borramos en orden:
-- 1. La tabla 'usuario' (que tiene la FK)
DROP TABLE IF EXISTS usuario;
-- 2. La tabla 'credencialacceso' (la tabla padre)
DROP TABLE IF EXISTS credencialacceso;


/* ================================================================ */
/* ============== CREACION TABLA "credencialacceso" =============== */
/* ================================================================ */
-- 
-- CAMBIO: Nombre en singular y minúsculas: 'credencialacceso'
--
CREATE TABLE credencialacceso (
    id_CredencialAcceso MEDIUMINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL,
    hashPassword VARCHAR(255) NOT NULL,
    salt VARCHAR(64) NOT NULL,
    ultimoCambio DATETIME,
    requiereReset BOOLEAN NOT NULL
);

/* ================================================================ */
/* ================== CREACION TABLA "usuario" ==================== */
/* ================================================================ */
--
-- CAMBIO: El nombre 'usuario' ya estaba bien (singular).
-- CAMBIO: Se actualiza la referencia de la FK a la nueva tabla 'credencialacceso'.
--
CREATE TABLE usuario (
    id MEDIUMINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL,
    username VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE, 
    activo BOOLEAN NOT NULL,
    fechaRegistro DATETIME NOT NULL,
    id_CredencialAcceso MEDIUMINT UNSIGNED UNIQUE,
    CONSTRAINT fk_id_credAcceso FOREIGN KEY (id_CredencialAcceso)
        REFERENCES credencialacceso (id_CredencialAcceso) -- <-- CORREGIDO
);

/* ================== VACIAR TABLAS COMPLETAS ===================== */
-- Desactivar las verificaciones de claves foraneas
SET FOREIGN_KEY_CHECKS = 0;

-- Vaciar primero la tabla dependiente
TRUNCATE TABLE usuario;

-- Luego vaciar la tabla referenciada
TRUNCATE TABLE credencialacceso; -- <-- CORREGIDO

-- Reactivar las verificaciones de claves foraneas
SET FOREIGN_KEY_CHECKS = 1;


/* ==================== TRIGGER DE VERIFICACION =================== */
/* ===================== EMAIL DEBE CONTENER @ ==================== */
-- Estos ya usaban 'usuario' (singular), así que están correctos.
DELIMITER $$
CREATE DEFINER=`root`@`localhost` TRIGGER `tpi-bd-i`.`usuario_BEFORE_INSERT` 
BEFORE INSERT ON `usuario` 
FOR EACH ROW
BEGIN
  IF INSTR(NEW.email, '@') = 0 THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'El email debe contener @';
  END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER usuario_BEFORE_UPDATE
BEFORE UPDATE ON usuario
FOR EACH ROW
BEGIN
  IF NEW.email <> OLD.email AND INSTR(NEW.email, '@') = 0 THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'El email debe contener @';
  END IF;
END$$
DELIMITER ;


-- ##############################################################
-- ######### VALIDACION DE CONSTRINTS MEDIANTE PRUEBAS ##########
-- ##############################################################
-- Todos los INSERTs deben apuntar a 'usuario' (singular)

-- 1) Violación de integridad referencial (Foreign Key)
INSERT INTO usuario
VALUES (200, false, 'usuario1', 'usuario1@gmail.com', true, '2025-10-14 15:43:00', 4562);

-- 2)	Violación de restricción UNIQUE en clave foránea
INSERT INTO usuario
VALUES 
(null, false, 'usuario1', 'usuario1@gmail.com', true, '2025-10-14 15:43:00', 4562), 
(null, false, 'usuario2', 'usuario2@gmail.com', true, '2025-10-14 20:43:00', 4562);

-- 3)	Violación de restricción UNIQUE en username
--
-- CAMBIO: Estaba como 'usuarios' (plural)
--
INSERT INTO usuario 
VALUES 
(null,false, 'usuario1', 'usuario1@gmail.com', true, '2025-10-14 15:43:00', 4562), 
(null,false, 'usuario1', 'usuario2@gmail.com', true, '2025-10-14 20:43:00', 4561);
