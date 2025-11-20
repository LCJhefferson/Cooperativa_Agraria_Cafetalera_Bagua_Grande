package dao;

import conexion.Conexion;
import modelos.Salida;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalidaDAO {

    public boolean registrarSalida(Salida s) {
        String sql = """
            INSERT INTO salida(id_usuario, id_producto, cantidad_salida, destino, observaciones)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection cn = Conexion.getConnection();
             PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setInt(1, s.getIdUsuario());
            pst.setInt(2, s.getIdProducto());
            pst.setDouble(3, s.getCantidadSalida());
            pst.setString(4, s.getDestino());
            pst.setString(5, s.getObservaciones());

            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar salida: " + e.getMessage());
            return false;
        }
    }

    public List<Salida> listarSalidas() {
        List<Salida> lista = new ArrayList<>();
        String sql = "SELECT * FROM salida ORDER BY fecha_salida DESC";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement pst = cn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Salida s = new Salida();
                s.setId(rs.getInt("id"));
                s.setIdUsuario(rs.getInt("id_usuario"));
                s.setIdProducto(rs.getInt("id_producto"));
                s.setCantidadSalida(rs.getDouble("cantidad_salida"));
                s.setDestino(rs.getString("destino"));
                s.setFechaSalida(rs.getString("fecha_salida"));
                s.setObservaciones(rs.getString("observaciones"));

                lista.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar salidas: " + e.getMessage());
        }

        return lista;
    }
}
