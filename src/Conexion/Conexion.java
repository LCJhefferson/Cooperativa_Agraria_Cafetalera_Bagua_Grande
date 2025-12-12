package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/cooperativa_cafetalera"
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String USER = "root";
    private static final String PASS = "";

    // ¡NUNCA MÁS CONEXIÓN ESTÁTICA!
    // Cada vez que se llame getConexion() → devuelve una conexión NUEVA
    public static Connection getConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection nuevaConexion = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conexión exitosa (nueva conexión creada)");
            return nuevaConexion;
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Driver MySQL no encontrado → " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("ERROR al conectar a MySQL → " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Método opcional para cerrar conexión (útil si quieres cerrarla manualmente)
    public static void cerrarConexion(Connection cn) {
        if (cn != null) {
            try {
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}