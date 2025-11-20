package dao;

import conexion.Conexion;
import modelos.Compra;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    // Registrar compra (los triggers calcularán precio y validarán requisitos)
    public boolean registrarCompra(Compra c) {
        String sql = """
            INSERT INTO compra(id_usuario, id_producto, id_socio, rendimiento, humedad, guia_ingreso, cantidad, precio)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection cn = Conexion.getConnection();
             PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setInt(1, c.getIdUsuario());
            pst.setInt(2, c.getIdProducto());
            pst.setInt(3, c.getIdSocio());
            pst.setDouble(4, c.getRendimiento());
            pst.setDouble(5, c.getHumedad());
            pst.setString(6, c.getGuiaIngreso());
            pst.setDouble(7, c.getCantidad());
            pst.setDouble(8, c.getPrecio());

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

        try (Connection cn = Conexion.getConnection();
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
}
