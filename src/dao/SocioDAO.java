package dao;

import conexion.Conexion;
import modelos.Socio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocioDAO {

    public boolean registrarSocio(Socio s) {
        String sql = """
            INSERT INTO socio(nombre, id_tipoDocumento, nro_documento, cobase, estado)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection cn = Conexion.getConnection();
             PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setString(1, s.getNombre());
            pst.setInt(2, s.getIdTipoDocumento());
            pst.setString(3, s.getNroDocumento());
            pst.setString(4, s.getCobase());
            pst.setString(5, s.getEstado());

            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar socio: " + e.getMessage());
            return false;
        }
    }

    public List<Socio> listarSocios() {
        List<Socio> lista = new ArrayList<>();
        String sql = "SELECT * FROM socio ORDER BY nombre ASC";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement pst = cn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Socio s = new Socio();
                s.setId(rs.getInt("id"));
                s.setNombre(rs.getString("nombre"));
                s.setIdTipoDocumento(rs.getInt("id_tipoDocumento"));
                s.setNroDocumento(rs.getString("nro_documento"));
                s.setCobase(rs.getString("cobase"));
                s.setEstado(rs.getString("estado"));

                lista.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar socios: " + e.getMessage());
        }

        return lista;
    }
}
