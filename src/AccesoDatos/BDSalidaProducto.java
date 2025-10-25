/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AccesoDatos;

import Entidad.Producto;
import Entidad.Usuario;
import Entidad.SalidaProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Clase de acceso a datos para la tabla salida_producto.
 * Implementa las operaciones CRUD usando la interfaz ICRUD.
 * 
 * @author LENOVO
 */
public class BDSalidaProducto implements ICRUD {

    @Override
    public ArrayList<SalidaProducto> listar() throws Exception {
        ArrayList<SalidaProducto> lista = new ArrayList<>();
        String sql = """
            SELECT sp.*, 
                   p.id AS id_producto, p.nombre AS nombre_producto,
                   u.id AS id_usuario, u.nombre AS nombre_usuario
            FROM salida_producto sp
            INNER JOIN producto p ON sp.id_producto = p.id
            INNER JOIN usuario u ON sp.id_usuario = u.id
            WHERE sp.estado = 1
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SalidaProducto sp = construirSalidaDesdeResultSet(rs);
                lista.add(sp);
            }

        } catch (SQLException e) {
            throw new Exception("Error al listar salidas de producto: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public int crear(Object object) throws SQLException {
        SalidaProducto sp = (SalidaProducto) object;
        String sql = """
            INSERT INTO salida_producto 
            (id_producto, id_usuario, cantidad, destino, observaciones, fecha_registro, guia_salida, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
        sp.setFechaRegistro(fechaActual);

        int filasAfectadas = 0;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sp.getProducto().getId());
            ps.setInt(2, sp.getUsuario().getId());
            ps.setDouble(3, sp.getCantidad());
            ps.setString(4, sp.getDestino());
            ps.setString(5, sp.getObservaciones());
            ps.setTimestamp(6, sp.getFechaRegistro());
            ps.setString(7, sp.getGuiaSalida());
            ps.setBoolean(8, true);

            filasAfectadas = ps.executeUpdate();

        }

        return filasAfectadas;
    }

    @Override
    public void actualizar(int id, Object object) throws Exception {
        SalidaProducto sp = (SalidaProducto) object;

        String sql = """
            UPDATE salida_producto
            SET id_producto = ?, id_usuario = ?, cantidad = ?, destino = ?, observaciones = ?, 
                fecha_registro = ?, guia_salida = ?
            WHERE id = ?
        """;

        Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
        sp.setFechaRegistro(fechaActual);

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sp.getProducto().getId());
            ps.setInt(2, sp.getUsuario().getId());
            ps.setDouble(3, sp.getCantidad());
            ps.setString(4, sp.getDestino());
            ps.setString(5, sp.getObservaciones());
            ps.setTimestamp(6, sp.getFechaRegistro());
            ps.setString(7, sp.getGuiaSalida());
            ps.setInt(8, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al actualizar salida de producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "UPDATE salida_producto SET estado = 0 WHERE id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al eliminar salida de producto: " + e.getMessage(), e);
        }
    }

    @Override
    public SalidaProducto get(int id) throws Exception {
        SalidaProducto sp = null;
        String sql = """
            SELECT sp.*, 
                   p.id AS id_producto, p.nombre AS nombre_producto,
                   u.id AS id_usuario, u.nombre AS nombre_usuario
            FROM salida_producto sp
            INNER JOIN producto p ON sp.id_producto = p.id
            INNER JOIN usuario u ON sp.id_usuario = u.id
            WHERE sp.id = ? AND sp.estado = 1
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sp = construirSalidaDesdeResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error al obtener salida de producto: " + e.getMessage(), e);
        }

        return sp;
    }

    // 🔍 Método auxiliar para construir un objeto SalidaProducto desde un ResultSet
    private SalidaProducto construirSalidaDesdeResultSet(ResultSet rs) throws SQLException {
        return new SalidaProducto(
                rs.getInt("id"),
                new Producto(rs.getInt("id_producto"), rs.getString("nombre_producto")),
                new Usuario(rs.getInt("id_usuario"), rs.getString("nombre_usuario")),
                rs.getDouble("cantidad"),
                rs.getString("destino"),
                rs.getString("observaciones"),
                rs.getTimestamp("fecha_registro"),
                rs.getString("guia_salida")
        );
    }

    // 🔎 Método adicional de búsqueda (opcional)
    public ArrayList<SalidaProducto> buscarPorDestino(String destino) throws Exception {
        ArrayList<SalidaProducto> lista = new ArrayList<>();
        String sql = """
            SELECT sp.*, 
                   p.id AS id_producto, p.nombre AS nombre_producto,
                   u.id AS id_usuario, u.nombre AS nombre_usuario
            FROM salida_producto sp
            INNER JOIN producto p ON sp.id_producto = p.id
            INNER JOIN usuario u ON sp.id_usuario = u.id
            WHERE sp.destino LIKE ? AND sp.estado = 1
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + destino + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SalidaProducto sp = construirSalidaDesdeResultSet(rs);
                    lista.add(sp);
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error al buscar salidas por destino: " + e.getMessage(), e);
        }

        return lista;
    }
}
