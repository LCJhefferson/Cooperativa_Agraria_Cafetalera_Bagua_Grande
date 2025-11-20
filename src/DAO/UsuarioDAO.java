package DAO;

import Conexion.Conexion;
import Modelos.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean registrarUsuarioConRol(Usuario u) {
        String sql = """
            INSERT INTO usuarios(Usuario, id_tipoDocumento, nro_documento, contraseña, id_rol)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection cn = Conexion.getConexion();
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

        try (Connection cn = Conexion.getConexion();
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
    
    
    
    public int validarLogin(String usuario, String contraseña) {
        String sql = "SELECT id_rol FROM usuarios WHERE Usuario = ? AND contraseña = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usuario);
            ps.setString(2, contraseña);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_rol");
            }

        } catch (Exception e) {
            System.out.println("Error en validarLogin: " + e.getMessage());
        }

        return -1; // no existe
    }
    
    public String obtenerCorreoPorUsuario(String usuario) {
    String sql = "SELECT iu.correo FROM usuarios u "
               + "INNER JOIN info_usuario iu ON u.id = iu.id_usuario "
               + "WHERE u.Usuario = ?";

    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, usuario);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("correo");
        }

    } catch (Exception e) {
        System.out.println("Error obtener correo: " + e.getMessage());
    }
    return null;
}

    
    public void actualizarClave(String usuario, String nuevaClave) {
    String sql = "UPDATE usuarios SET contraseña=? WHERE Usuario=?";

    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nuevaClave);
        ps.setString(2, usuario);
        ps.executeUpdate();

    } catch (Exception e) {
        System.out.println("Error actualizar clave: " + e.getMessage());
    }
}

    
    
    
    
    
    
    
    
    
    
    
    
}
