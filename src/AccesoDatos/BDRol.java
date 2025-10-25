package AccesoDatos;

import Entidad.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**

 * @author 
 */
public class BDRol implements ICRUD {

    @Override
    public ArrayList listar() throws Exception {
        ArrayList<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM rol";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol(
                    rs.getInt("id_rol"),
                    rs.getString("nombre_rol")
                );
                roles.add(rol);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar roles: " + e.getMessage());
        }

        return roles;
    }

    @Override
    public int crear(Object object) throws SQLException {
        int filas = 0;
        Rol rol = (Rol) object;
        String sql = "INSERT INTO rol (nombre_rol) VALUES (?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rol.getNombreRol());
            filas = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al crear rol: " + e.getMessage());
            throw e;
        }

        return filas;
    }

    @Override
    public void actualizar(int id, Object object) throws Exception {
        Rol rol = (Rol) object;
        String sql = "UPDATE rol SET nombre_rol = ? WHERE id_rol = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rol.getNombreRol());
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al actualizar rol: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM rol WHERE id_rol = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al eliminar rol: " + e.getMessage(), e);
        }
    }

    @Override
    public Rol get(int id) throws Exception {
        Rol rol = null;
        String sql = "SELECT * FROM rol WHERE id_rol = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rol = new Rol(
                        rs.getInt("id_rol"),
                        rs.getString("nombre_rol")
                    );
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener rol: " + e.getMessage(), e);
        }

        return rol;
    }
}
