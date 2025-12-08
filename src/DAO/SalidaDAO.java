package DAO;

import Conexion.Conexion;
import Modelos.Salida;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalidaDAO {

    // Obtener ID del producto por su nombre (activo)
    public int obtenerIdProductoPorNombre(String nombre) {
        String sql = "SELECT id FROM producto WHERE nombre = ? AND estado = 'activo' LIMIT 1";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, nombre);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error obtenerIdProductoPorNombre: " + e.getMessage());
        }
        return -1;
    }

    // Registrar salida (no actualiza stock: la BD tiene triggers que lo hacen)
    public boolean registrarSalida(Salida s) {
        String sql = "INSERT INTO salida(id_usuario, id_producto, cantidad_salida, numero_orden, destino, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement pst = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, s.getIdUsuario());
            pst.setInt(2, s.getIdProducto());
            pst.setDouble(3, s.getCantidadSalida());
            pst.setString(4, s.getNumeroOrden());
            pst.setString(5, s.getDestino());
            pst.setString(6, s.getObservaciones());

            int affected = pst.executeUpdate();
            if (affected == 0) return false;

            // opcional: obtener id generado y asignarlo a s
            try (ResultSet gk = pst.getGeneratedKeys()) {
                if (gk.next()) s.setId(gk.getInt(1));
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error registrarSalida: " + e.getMessage());
            return false;
        }
    }

    // Listar salidas con nombre del producto via JOIN
    public List<Salida> listarSalidas() {
        List<Salida> lista = new ArrayList<>();
        String sql = "SELECT s.*, p.nombre AS nombreProducto FROM salida s INNER JOIN producto p ON p.id = s.id_producto ORDER BY s.fecha_salida DESC";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement pst = cn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Salida s = new Salida();
                s.setId(rs.getInt("id"));
                s.setIdUsuario(rs.getInt("id_usuario"));
                s.setIdProducto(rs.getInt("id_producto"));
                s.setCantidadSalida(rs.getDouble("cantidad_salida"));
                s.setNumeroOrden(rs.getString("numero_orden"));
                s.setDestino(rs.getString("destino"));
                s.setFechaSalida(rs.getTimestamp("fecha_salida"));
                s.setObservaciones(rs.getString("observaciones"));
                s.setNombreProducto(rs.getString("nombreProducto"));
                lista.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Error listarSalidas: " + e.getMessage());
        }
        return lista;
    }

    // Obtener salida por id (con nombre producto)
    public Salida obtenerSalidaPorId(int id) {
        String sql = "SELECT s.*, p.nombre AS nombreProducto FROM salida s INNER JOIN producto p ON p.id = s.id_producto WHERE s.id = ? LIMIT 1";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Salida s = new Salida();
                    s.setId(rs.getInt("id"));
                    s.setIdUsuario(rs.getInt("id_usuario"));
                    s.setIdProducto(rs.getInt("id_producto"));
                    s.setCantidadSalida(rs.getDouble("cantidad_salida"));
                    s.setNumeroOrden(rs.getString("numero_orden"));
                    s.setDestino(rs.getString("destino"));
                    s.setFechaSalida(rs.getTimestamp("fecha_salida"));
                    s.setObservaciones(rs.getString("observaciones"));
                    s.setNombreProducto(rs.getString("nombreProducto"));
                    return s;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenerSalidaPorId: " + e.getMessage());
        }
        return null;
    }

    // Actualizar salida (BD triggers manejarán stock)
    public boolean actualizarSalida(Salida s) {
        String sql = "UPDATE salida SET id_producto = ?, cantidad_salida = ?, numero_orden = ?, destino = ?, observaciones = ? WHERE id = ?";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setInt(1, s.getIdProducto());
            pst.setDouble(2, s.getCantidadSalida());
            pst.setString(3, s.getNumeroOrden());
            pst.setString(4, s.getDestino());
            pst.setString(5, s.getObservaciones());
            pst.setInt(6, s.getId());

            int a = pst.executeUpdate();
            return a > 0;

        } catch (SQLException e) {
            System.out.println("Error actualizarSalida: " + e.getMessage());
            return false;
        }
    }

    // Eliminar salida (BD triggers manejarán devolución de stock)
    public boolean eliminarSalida(int id) {
        String sql = "DELETE FROM salida WHERE id = ?";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setInt(1, id);
            int a = pst.executeUpdate();
            return a > 0;

        } catch (SQLException e) {
            System.out.println("Error eliminarSalida: " + e.getMessage());
            return false;
        }
    }
}
