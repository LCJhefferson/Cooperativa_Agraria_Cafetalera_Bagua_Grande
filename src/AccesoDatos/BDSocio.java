package AccesoDatos;

import Entidad.Socio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

/**
 * DAO para la tabla socio.
 * Implementa operaciones CRUD usando la interfaz ICRUD.
 * 
 * @author LENOVO
 */
public class BDSocio implements ICRUD {

    @Override
    public ArrayList<Socio> listar() throws Exception {
        ArrayList<Socio> lista = new ArrayList<>();
        String sql = "SELECT * FROM socio WHERE estado = 'activo'";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Socio s = new Socio();
                s.setId(rs.getInt("id"));
                s.setNombre(rs.getString("nombre"));
                s.setDni(rs.getString("dni"));
                s.setCobase(rs.getString("cobase"));
                s.setEstado(rs.getString("estado"));
                s.setFechaIngreso(rs.getDate("fecha_ingreso"));
                lista.add(s);
            }

        } catch (SQLException e) {
            throw new Exception("Error al listar socios: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public int crear(Object object) throws SQLException {
        Socio s = (Socio) object;
        String sql = """
            INSERT INTO socio (nombre, dni, cobase, estado, fecha_ingreso)
            VALUES (?, ?, ?, ?, ?)
        """;

        int filasAfectadas = 0;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDni());
            ps.setString(3, s.getCobase());
            ps.setString(4, s.getEstado());
            ps.setDate(5, new Date(s.getFechaIngreso().getTime()));

            filasAfectadas = ps.executeUpdate();
        }

        return filasAfectadas;
    }

    @Override
    public void actualizar(int id, Object object) throws Exception {
        Socio s = (Socio) object;
        String sql = """
            UPDATE socio
            SET nombre = ?, dni = ?, cobase = ?, estado = ?, fecha_ingreso = ?
            WHERE id = ?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDni());
            ps.setString(3, s.getCobase());
            ps.setString(4, s.getEstado());
            ps.setDate(5, new Date(s.getFechaIngreso().getTime()));
            ps.setInt(6, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al actualizar socio: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "UPDATE socio SET estado = 'inactivo' WHERE id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al eliminar socio: " + e.getMessage(), e);
        }
    }

    @Override
    public Socio get(int id) throws Exception {
        Socio s = null;
        String sql = "SELECT * FROM socio WHERE id = ? AND estado = 'activo'";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    s = new Socio();
                    s.setId(rs.getInt("id"));
                    s.setNombre(rs.getString("nombre"));
                    s.setDni(rs.getString("dni"));
                    s.setCobase(rs.getString("cobase"));
                    s.setEstado(rs.getString("estado"));
                    s.setFechaIngreso(rs.getDate("fecha_ingreso"));
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error al obtener socio: " + e.getMessage(), e);
        }

        return s;
    }
}