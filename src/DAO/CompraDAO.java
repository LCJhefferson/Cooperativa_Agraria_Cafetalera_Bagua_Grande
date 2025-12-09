package DAO;

import Conexion.Conexion;
import Modelos.Compra;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    // Registrar compra (los triggers calcularán precio y validarán requisitos)
    public boolean registrarCompra(Compra c) {
    String sql = """
        INSERT INTO compra (id_usuario, id_producto, cantidad, humedad,
                 precio, rendimiento, guia_ingreso, id_socio, fecha_registro)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    try (Connection cn = Conexion.getConexion();
            
         PreparedStatement pst = cn.prepareStatement(sql)) {

        pst.setInt(1, c.getIdUsuario());
        pst.setInt(2, c.getIdProducto());
        pst.setDouble(3, c.getCantidad());
        pst.setDouble(4, c.getHumedad());
        pst.setDouble(5, c.getPrecio());
        pst.setDouble(6, c.getRendimiento());
        pst.setString(7, c.getGuiaIngreso());
        pst.setInt(8, c.getIdSocio());
        pst.setString(9, c.getFechaRegistro());

        pst.executeUpdate();
        return true;

    } catch (SQLException e) {
        System.out.println("Error al registrar compra: " + e.getMessage());
        return false;
    }
}

    // Listar todas las compras
    public List<Compra> listarCompras() {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT * FROM compra ORDER BY fecha_registro DESC";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement pst = cn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Compra c = new Compra();
                c.setId(rs.getInt("id"));
                c.setIdUsuario(rs.getInt("id_usuario"));
                c.setIdProducto(rs.getInt("id_producto"));
                c.setIdPrecioDia(rs.getInt("id_precio_dia"));
                c.setIdSocio(rs.getInt("id_socio"));
                c.setRendimiento(rs.getDouble("rendimiento"));
                c.setHumedad(rs.getDouble("humedad"));
                c.setGuiaIngreso(rs.getString("guia_ingreso"));
                c.setCantidad(rs.getDouble("cantidad"));
                c.setPrecio(rs.getDouble("precio"));
                c.setPrecioCalculado(rs.getDouble("precio_calculado"));
                c.setFechaRegistro(rs.getString("fecha_registro"));

                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar compras: " + e.getMessage());
        }

        return lista;
    }
    
    
    
    public boolean actualizarCompra(Compra c) {
    String sql = """
        UPDATE compra SET cantidad=?, precio=?, humedad=?, rendimiento=?, guia_ingreso=? WHERE id=?
        """;
    try (Connection cn = Conexion.getConexion();
         PreparedStatement pst = cn.prepareStatement(sql)) {

        pst.setDouble(1, c.getCantidad());
        pst.setDouble(2, c.getPrecio());
        pst.setDouble(3, c.getHumedad());
        pst.setDouble(4, c.getRendimiento());
        pst.setString(5, c.getGuiaIngreso());
        pst.setInt(6, c.getId());

        pst.executeUpdate();
        return true;
    } catch (SQLException e) {
        System.out.println("Error al actualizar compra: " + e.getMessage());
        return false;
    }
}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
