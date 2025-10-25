/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AccesoDatos;

import Entidad.InfoUsuario;
import Entidad.Usuario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase de acceso a datos para la tabla info_usuario.
 * Implementa las operaciones CRUD usando la interfaz ICRUD.
 * 
 * @author LENOVO
 */
public class BDInfoUsuario implements ICRUD {

    @Override
    public ArrayList<InfoUsuario> listar() throws Exception {
        ArrayList<InfoUsuario> lista = new ArrayList<>();
        String sql = """
            SELECT iu.*, u.id AS id_usuario, u.nombre AS nombre_usuario
            FROM info_usuario iu
            INNER JOIN usuario u ON iu.id_usuario = u.id
            WHERE iu.estado = 'ACTIVO'
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                InfoUsuario info = construirInfoDesdeResultSet(rs);
                lista.add(info);
            }

        } catch (SQLException e) {
            throw new Exception("Error al listar información de usuarios: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public int crear(Object object) throws SQLException {
        InfoUsuario info = (InfoUsuario) object;

        String sql = """
            INSERT INTO info_usuario 
            (id_usuario, nombre, apellido, telefono, correo, fecha_ingreso, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        int filasAfectadas = 0;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, info.getUsuario().getId());
            ps.setString(2, info.getNombre());
            ps.setString(3, info.getApellido());
            ps.setString(4, info.getTelefono());
            ps.setString(5, info.getCorreo());
            ps.setDate(6, info.getFechaIngreso());
            ps.setString(7, info.getEstado());

            filasAfectadas = ps.executeUpdate();

        }

        return filasAfectadas;
    }

    @Override
    public void actualizar(int id, Object object) throws Exception {
        InfoUsuario info = (InfoUsuario) object;

        String sql = """
            UPDATE info_usuario
            SET id_usuario = ?, nombre = ?, apellido = ?, telefono = ?, correo = ?, 
                fecha_ingreso = ?, estado = ?
            WHERE id = ?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, info.getUsuario().getId());
            ps.setString(2, info.getNombre());
            ps.setString(3, info.getApellido());
            ps.setString(4, info.getTelefono());
            ps.setString(5, info.getCorreo());
            ps.setDate(6, info.getFechaIngreso());
            ps.setString(7, info.getEstado());
            ps.setInt(8, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al actualizar información del usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "UPDATE info_usuario SET estado = 'INACTIVO' WHERE id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al eliminar información del usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public InfoUsuario get(int id) throws Exception {
        InfoUsuario info = null;
        String sql = """
            SELECT iu.*, u.id AS id_usuario, u.nombre AS nombre_usuario
            FROM info_usuario iu
            INNER JOIN usuario u ON iu.id_usuario = u.id
            WHERE iu.id = ? AND iu.estado = 'ACTIVO'
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    info = construirInfoDesdeResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error al obtener información del usuario: " + e.getMessage(), e);
        }

        return info;
    }

    // 🔧 Método auxiliar para construir InfoUsuario desde un ResultSet
    private InfoUsuario construirInfoDesdeResultSet(ResultSet rs) throws SQLException {
        return new InfoUsuario(
                rs.getInt("id"),
                new Usuario(rs.getInt("id_usuario"), rs.getString("nombre_usuario")),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getDate("fecha_ingreso"),
                rs.getString("estado")
        );
    }

    // 🔎 Método adicional (opcional): buscar por nombre o apellido
    public ArrayList<InfoUsuario> buscarPorNombre(String texto) throws Exception {
        ArrayList<InfoUsuario> lista = new ArrayList<>();
        String sql = """
            SELECT iu.*, u.id AS id_usuario, u.nombre AS nombre_usuario
            FROM info_usuario iu
            INNER JOIN usuario u ON iu.id_usuario = u.id
            WHERE (iu.nombre LIKE ? OR iu.apellido LIKE ?)
              AND iu.estado = 'ACTIVO'
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InfoUsuario info = construirInfoDesdeResultSet(rs);
                    lista.add(info);
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error al buscar usuario por nombre o apellido: " + e.getMessage(), e);
        }

        return lista;
    }
}
