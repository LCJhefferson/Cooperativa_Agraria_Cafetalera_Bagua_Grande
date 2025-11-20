package dao;

import conexion.Conexion;
import modelos.PrecioDia;

import java.sql.*;

public class PrecioDiaDAO {

    public boolean registrarPrecioDia(PrecioDia p) {
        String sql = """
            INSERT INTO precio_dia(fecha, precio_base)
            VALUES (?, ?)
            """;

        try (Connection cn = Conexion.getConnection();
             PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setString(1, p.getFecha());
            pst.setDouble(2, p.getPrecioBase());

            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar precio del día: " + e.getMessage());
            return false;
        }
    }

    public PrecioDia obtenerPrecioDiaActual() {
        PrecioDia p = null;
        String sql = "SELECT * FROM precio_dia WHERE fecha = CURDATE() LIMIT 1";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement pst = cn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                p = new PrecioDia();
                p.setId(rs.getInt("id"));
                p.setFecha(rs.getString("fecha"));
                p.setPrecioBase(rs.getDouble("precio_base"));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener precio del día actual: " + e.getMessage());
        }

        return p;
    }
}
