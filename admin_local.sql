-- SOLO PARA BASE DE DATOS LOCAL DE DESARROLLO / PRUEBAS.
-- La versión actual de AuthService compara contraseñas como texto plano.
-- Reemplazar por BCrypt y autenticación/autorización real antes de producción.
USE usuariosdb;

-- Inserta un Administrador sin modificar ningún registro existente.
INSERT INTO usuarios (tipo_usuario, correo, contrasena)
SELECT 'ADMINISTRADOR', 'admin@fullcomponents.cl', 'admin123'
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE correo = 'admin@fullcomponents.cl'
);

-- Verifica la existencia y el tipo de cuenta; no muestra la contraseña.
SELECT id_usuario, correo, tipo_usuario
FROM usuarios
WHERE correo = 'admin@fullcomponents.cl';
