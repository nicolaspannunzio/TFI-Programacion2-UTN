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

    @Override
    public void insertar(Usuario usuario) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
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
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
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

    @Override
    public void actualizar(Usuario u) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            UsuarioDao dao = new UsuarioDao(conn);
            dao.actualizar(u);
        }
    }

    @Override
    public void eliminar(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            UsuarioDao dao = new UsuarioDao(conn);
            dao.eliminar(id);
        }
    }
}
