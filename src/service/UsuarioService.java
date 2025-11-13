package service;

import config.DatabaseConnection;
import dao.UsuarioDao;
import dao.CredencialAccesoDao;
import entities.Usuario;
import entities.CredencialAcceso;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class UsuarioService implements GenericService<Usuario> {

    // -----------------------------------------------------------------
    // INICIO DE LA LÓGICA DEL PUNTO 4 (Validación)
    // -----------------------------------------------------------------
    
    /**
     * Método privado que valida la entidad Usuario ANTES de
     * enviarla a la base de datos.
     * Lanza IllegalArgumentException si los datos son inválidos.
     * * @param usuario El objeto Usuario a validar.
     * @throws IllegalArgumentException si los datos no cumplen las reglas de negocio.
     */
    private void validar(Usuario usuario) throws IllegalArgumentException {
        if (usuario == null) {
            throw new IllegalArgumentException("El objeto Usuario no puede ser nulo.");
        }
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario (username) es obligatorio.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        // Nota: La base de datos (con el Trigger) ya valida el formato del email,
        // pero una validación simple en el servicio es buena práctica.
        if (!usuario.getEmail().contains("@")) {
             throw new IllegalArgumentException("El email debe contener un '@'.");
        }

        if (usuario.getCredencial() == null) {
            throw new IllegalArgumentException("La credencial del usuario no puede ser nula.");
        }
        if (usuario.getCredencial().getHashPassword() == null || usuario.getCredencial().getHashPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("El hashPassword de la credencial es obligatorio.");
        }
    }
    
    // -----------------------------------------------------------------
    // FIN DE LA LÓGICA DEL PUNTO 4
    // -----------------------------------------------------------------


    @Override
    public void insertar(Usuario usuario) throws SQLException, IllegalArgumentException {
        
        // 1. Validar (Punto 4)
        validar(usuario); // Lanza excepción si falla

        // 2. Transacción (Punto 2)
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); 

            CredencialAccesoDao credDao = new CredencialAccesoDao(conn);
            UsuarioDao usuarioDao = new UsuarioDao(conn);

            CredencialAcceso cred = usuario.getCredencial();
            cred.setUltimoCambio(LocalDateTime.now());
            credDao.crear(cred); 

            usuario.setFechaRegistro(LocalDateTime.now());
            usuarioDao.crear(usuario);

            conn.commit();
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            // Relanza la excepción SQL (ej. por "username duplicado" de la DB)
            throw new SQLException("Error al insertar en la base de datos: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            // Relanza la excepción de validación
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void actualizar(Usuario usuario) throws SQLException, IllegalArgumentException {
        
        // 1. Validar (Punto 4)
        validar(usuario); // Lanza excepción si falla
        if (usuario.getId() <= 0) { // Validación extra para actualizar
            throw new IllegalArgumentException("El ID del usuario debe ser válido para actualizar.");
        }

        // 2. Transacción (Punto 2)
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            UsuarioDao usuarioDao = new UsuarioDao(conn);
            CredencialAccesoDao credDao = new CredencialAccesoDao(conn);

            credDao.actualizar(usuario.getCredencial());
            usuarioDao.actualizar(usuario);

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error al actualizar en la base de datos: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void eliminar(long id) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); 

            UsuarioDao usuarioDao = new UsuarioDao(conn);
            CredencialAccesoDao credDao = new CredencialAccesoDao(conn);
            
            Usuario usuario = usuarioDao.leer(id);
            
            if (usuario != null) {
                usuarioDao.eliminar(usuario.getId()); 
                credDao.eliminar(usuario.getCredencial().getId());
                conn.commit();
            } else {
                throw new SQLException("El usuario con id " + id + " no existe o ya fue eliminado.");
            }

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error al eliminar en la base de datos: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public Usuario getById(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            UsuarioDao dao = new UsuarioDao(conn);
            return dao.leer(id);
        }
    }

    @Override
    public List<Usuario> getAll() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            UsuarioDao dao = new UsuarioDao(conn);
            return dao.leerTodos();
        }
    }
}