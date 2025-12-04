package DAO;

import Conexion.Conexion;
import Modelos.PrecioDia;

import java.sql.*;

public class PrecioDiaDAO {

    public boolean registrarPrecioDia(PrecioDia p) {
        String sql = """
            INSERT INTO precio_dia(fecha, precio_base)
            VALUES (?, ?)
            """;

        try (Connection cn = Conexion.getConexion();
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

        try (Connection cn = Conexion.getConexion();
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
    
    
    public boolean registrarPrecioDia(double precio) {
    String sql = "INSERT INTO precio_dia (fecha, precio_base, created_at) VALUES (CURDATE(), ?, NOW())";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setDouble(1, precio);
        ps.executeUpdate();
        return true;

    } catch (SQLException e) {
        System.out.println("Error al registrar precio del día: " + e.getMessage());
        return false;
    }
}

    
    
    public boolean yaRegistradoHoy() {
    String sql = "SELECT COUNT(*) FROM precio_dia WHERE fecha = CURDATE()";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        rs.next();
        return rs.getInt(1) > 0;

    } catch (SQLException e) {
        return false;
    }
}

    
    
    
    public boolean guardarOActualizarPrecio(double precio) {
    if (yaRegistradoHoy()) {
        String sql = "UPDATE precio_dia SET precio_base = ? WHERE fecha = CURDATE()";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, precio);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar precio del día: " + e.getMessage());
            return false;
        }
    } else {
        return registrarPrecioDia(precio);
    }
}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
