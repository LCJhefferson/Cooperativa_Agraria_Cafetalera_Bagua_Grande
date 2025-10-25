package AccesoDatos;

import Entidad.Usuario;
import Entidad.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO para la tabla usuario.
 * Implementa operaciones CRUD usando la interfaz ICRUD.
 * 
 * @author LENOVO
 */
public class BDUsuario implements ICRUD {

    @Override
    public ArrayList<Usuario> listar() throws Exception {
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = """
            SELECT u.*, r.id AS id_rol, r.nombre AS nombre_rol
            FROM usuario u
            INNER JOIN rol r ON u.id_rol = r.id
            WHERE u.estado = 1
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setUsuario(rs.getString("usuario"));
                u.setContrasena(rs.getString("contrasena"));
                u.setRol(new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol")));
                lista.add(u);
            }

        } catch (SQLException e) {
            throw new Exception("Error al listar usuarios: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public int crear(Object object) throws SQLException {
        Usuario u = (Usuario) object;
        String sql = """
            INSERT INTO usuario (usuario, contrasena, id_rol, estado)
            VALUES (?, ?, ?, ?)
        """;

        int filasAfectadas = 0;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getContrasena());
            ps.setInt(3, u.getRol().getId());
            ps.setBoolean(4, true);

            filasAfectadas = ps.executeUpdate();
        }

        return filasAfectadas;
    }

    @Override
    public void actualizar(int id, Object object) throws Exception {
        Usuario u = (Usuario) object;
        String sql = """
            UPDATE usuario
            SET usuario = ?, contrasena = ?, id_rol = ?
            WHERE id = ?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getContrasena());
            ps.setInt(3, u.getRol().getId());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "UPDATE usuario SET estado = 0 WHERE id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario get(int id) throws Exception {
        Usuario u = null;
        String sql = """
            SELECT u.*, r.id AS id_rol, r.nombre AS nombre_rol
            FROM usuario u
            INNER JOIN rol r ON u.id_rol = r.id
            WHERE u.id = ? AND u.estado = 1
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setContrasena(rs.getString("contrasena"));
                    u.setRol(new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol")));
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error al obtener usuario: " + e.getMessage(), e);
        }

        return u;
    }
}