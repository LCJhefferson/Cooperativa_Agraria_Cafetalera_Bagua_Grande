package dao;

import conexion.Conexion;
import modelos.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean registrarUsuarioConRol(Usuario u) {
        String sql = """
            INSERT INTO usuarios(Usuario, id_tipoDocumento, nro_documento, contraseña, id_rol)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection cn = Conexion.getConnection();
             PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setString(1, u.getUsuario());
            pst.setInt(2, u.getIdTipoDocumento());
            pst.setString(3, u.getNroDocumento());
            pst.setString(4, u.getContraseña());
            pst.setInt(5, u.getIdRol());

            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY creado_en DESC";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement pst = cn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setUsuario(rs.getString("Usuario"));
                u.setIdTipoDocumento(rs.getInt("id_tipoDocumento"));
                u.setNroDocumento(rs.getString("nro_documento"));
                u.setContraseña(rs.getString("contraseña"));
                u.setIdRol(rs.getInt("id_rol"));

                lista.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }
}
