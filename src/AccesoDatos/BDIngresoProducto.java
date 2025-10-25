package AccesoDatos;

import Entidad.IngresoProducto;
import Entidad.Producto;
import Entidad.Socio;
import Entidad.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * DAO para la tabla ingreso_producto.
 * Implementa operaciones CRUD usando la interfaz ICRUD.
 * 
 * @author LENOVO
 */
public class BDIngresoProducto implements ICRUD {

    @Override
    public ArrayList<IngresoProducto> listar() throws Exception {
        ArrayList<IngresoProducto> lista = new ArrayList<>();
        String sql = """
            SELECT ip.*, 
                   p.id AS id_producto, p.nombre AS nombre_producto,
                   s.id AS id_socio, s.nombre AS nombre_socio,
                   u.id AS id_usuario, u.usuario AS nombre_usuario
            FROM ingreso_producto ip
            INNER JOIN producto p ON ip.id_producto = p.id
            INNER JOIN socio s ON ip.id_socio = s.id
            INNER JOIN usuario u ON ip.id_usuario = u.id
            WHERE ip.estado = 1
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                IngresoProducto ip = construirIngresoDesdeResultSet(rs);
                lista.add(ip);
            }

        } catch (SQLException e) {
            throw new Exception("Error al listar ingresos de producto: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public int crear(Object object) throws SQLException {
        IngresoProducto ip = (IngresoProducto) object;
        String sql = """
            INSERT INTO ingreso_producto 
            (id_producto, id_socio, id_usuario, cantidad, cobase, fecha_registro, guia_ingreso, humedad, precio, rendimiento, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        ip.setFechaRegistro(Timestamp.valueOf(LocalDateTime.now()));

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ip.getProducto().getId());
            ps.setInt(2, ip.getSocio().getId());
            ps.setInt(3, ip.getUsuario().getId());
            ps.setDouble(4, ip.getCantidad());
            ps.setString(5, ip.getCobase());
            ps.setTimestamp(6, ip.getFechaRegistro());
            ps.setString(7, ip.getGuiaIngreso());
            ps.setDouble(8, ip.getHumedad());
            ps.setDouble(9, ip.getPrecio());
            ps.setString(10, ip.getRendimiento());
            ps.setBoolean(11, true);

            return ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(int id, Object object) throws Exception {
        IngresoProducto ip = (IngresoProducto) object;
        String sql = """
            UPDATE ingreso_producto
            SET id_producto = ?, id_socio = ?, id_usuario = ?, cantidad = ?, cobase = ?, 
                fecha_registro = ?, guia_ingreso = ?, humedad = ?, precio = ?, rendimiento = ?
            WHERE id = ?
        """;

        ip.setFechaRegistro(Timestamp.valueOf(LocalDateTime.now()));

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ip.getProducto().getId());
            ps.setInt(2, ip.getSocio().getId());
            ps.setInt(3, ip.getUsuario().getId());
            ps.setDouble(4, ip.getCantidad());
            ps.setString(5, ip.getCobase());
            ps.setTimestamp(6, ip.getFechaRegistro());
            ps.setString(7, ip.getGuiaIngreso());
            ps.setDouble(8, ip.getHumedad());
            ps.setDouble(9, ip.getPrecio());
            ps.setString(10, ip.getRendimiento());
            ps.setInt(11, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al actualizar ingreso de producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "UPDATE ingreso_producto SET estado = 0 WHERE id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al eliminar ingreso de producto: " + e.getMessage(), e);
        }
    }

    @Override
    public IngresoProducto get(int id) throws Exception {
        IngresoProducto ip = null;
        String sql = """
            SELECT ip.*, 
                   p.id AS id_producto, p.nombre AS nombre_producto,
                   s.id AS id_socio, s.nombre AS nombre_socio,
                   u.id AS id_usuario, u.usuario AS nombre_usuario
            FROM ingreso_producto ip
            INNER JOIN producto p ON ip.id_producto = p.id
            INNER JOIN socio s ON ip.id_socio = s.id
            INNER JOIN usuario u ON ip.id_usuario = u.id
            WHERE ip.id = ? AND ip.estado = 1
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ip = construirIngresoDesdeResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error al obtener ingreso de producto: " + e.getMessage(), e);
        }

        return ip;
    }

    private IngresoProducto construirIngresoDesdeResultSet(ResultSet rs) throws SQLException {
        return new IngresoProducto(
                rs.getInt("id"),
                new Producto(rs.getInt("id_producto"), rs.getString("nombre_producto")),
                new Socio(rs.getInt("id_socio"), rs.getString("nombre_socio"), null, null, null, null),
                new Usuario(rs.getInt("id_usuario"), rs.getString("nombre_usuario")),
                rs.getDouble("cantidad"),
                rs.getString("cobase"),
                rs.getTimestamp("fecha_registro"),
                rs.getString("guia_ingreso"),
                rs.getDouble("humedad"),
                rs.getDouble("precio"),
                rs.getString("rendimiento")
        );
    }
}