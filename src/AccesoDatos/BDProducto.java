package AccesoDatos;

import Entidad.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO para la tabla producto.
 * Implementa operaciones CRUD usando la interfaz ICRUD.
 * 
 * @author LENOVO
 */
public class BDProducto implements ICRUD {

    @Override
    public ArrayList<Producto> listar() throws Exception {
        ArrayList<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE estado = 1";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombreProducto(rs.getString("nombre"));
                p.setUnidad(rs.getString("unidad"));
                p.setPrecioBase(rs.getDouble("precio_base"));
                lista.add(p);
            }

        } catch (SQLException e) {
            throw new Exception("Error al listar productos: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public int crear(Object object) throws SQLException {
        Producto p = (Producto) object;
        String sql = """
            INSERT INTO producto (nombre, unidad, precio_base, estado)
            VALUES (?, ?, ?, ?)
        """;

        int filasAfectadas = 0;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getUnidad());
            ps.setDouble(3, p.getPrecioBase());
            ps.setBoolean(4, true);

            filasAfectadas = ps.executeUpdate();
        }

        return filasAfectadas;
    }

    @Override
    public void actualizar(int id, Object object) throws Exception {
        Producto p = (Producto) object;
        String sql = """
            UPDATE producto
            SET nombre = ?, unidad = ?, precio_base = ?
            WHERE id = ?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getUnidad());
            ps.setDouble(3, p.getPrecioBase());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al actualizar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "UPDATE producto SET estado = 0 WHERE id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al eliminar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public Producto get(int id) throws Exception {
        Producto p = null;
        String sql = "SELECT * FROM producto WHERE id = ? AND estado = 1";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setNombreProducto(rs.getString("nombre"));
                    p.setUnidad(rs.getString("unidad"));
                    p.setPrecioBase(rs.getDouble("precio_base"));
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error al obtener producto: " + e.getMessage(), e);
        }

        return p;
    }
}