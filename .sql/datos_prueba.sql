-- ##############################################################
-- ETAPA 2 - Script de Datos de Prueba (INSERTS)
-- ##############################################################
USE `tpi-bd-i`;

/* ================================================================ */
/* ======================= LIMPIEZA PREVIA ======================== */
/* ================================================================ */
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE usuario; -- <-- CORREGIDO (singular)
TRUNCATE TABLE credencialacceso; -- <-- CORREGIDO (singular)
SET FOREIGN_KEY_CHECKS = 1;


/* ===========================  (1)  ============================== */
/* ====================== INSERCIÓN DE DATOS ====================== */
/* ================================================================ */
INSERT INTO credencialacceso 
    (id_CredencialAcceso, eliminado, hashPassword, salt, ultimoCambio, requiereReset)
VALUES
    -- Fila 1 (Será para el admin)
    (1, false, 'hash_largo_para_admin_con_sha256', 'salt_aleatorio_admin', '2025-10-01 10:00:00', false),
    
    -- Fila 2 (Será para un usuario común)
    (2, false, 'hash_largo_para_user_con_sha256', 'salt_aleatorio_user', '2025-10-02 11:30:00', false),
    
    -- Fila 3 (Será para un usuario eliminado lógicamente)
    (3, true, 'hash_largo_para_baja_con_sha256', 'salt_aleatorio_baja', '2025-09-15 08:00:00', true);


/* ===========================  (2)  ============================== */
/* ================ Creamos 3 usuarios y los vinculamos 
                        a las credenciales creadas ================ */
/* ================================================================ */
--
-- CAMBIO: El nombre de la tabla ahora es 'usuario' (singular)
--
INSERT INTO usuario 
    (id, eliminado, username, email, activo, fechaRegistro, id_CredencialAcceso)
VALUES
    -- Usuario 1 (Admin, vinculado a credencial 1)
    (1, false, 'admin', 'admin@mail.com', true, '2025-10-01 10:00:00', 1),

    -- Usuario 2 (Común, vinculado a credencial 2)
    (2, false, 'usuario_comun', 'user@mail.com', true, '2025-10-02 11:30:00', 2),

    -- Usuario 3 (Eliminado lógicamente, vinculado a credencial 3)
    (3, true, 'usuario_baja', 'baja@mail.com', false, '2025-09-15 08:00:00', 3);


/* ===========================  (3)  ============================== */
/* ============== VERIFICACIÓN DE LA RELACIÓN 1-A-1 =============== */
/* ================================================================ */
--
-- CAMBIO: El nombre de la tabla ahora es 'usuario' (singular)
--
SELECT
    u.id AS usuario_id,
    u.username,
    u.eliminado AS usuario_eliminado,
    c.id_CredencialAcceso AS credencial_id,
    c.eliminado AS credencial_eliminada
FROM usuario u -- <-- CORREGIDO
JOIN credencialacceso c ON u.id_CredencialAcceso = c.id_CredencialAcceso;
