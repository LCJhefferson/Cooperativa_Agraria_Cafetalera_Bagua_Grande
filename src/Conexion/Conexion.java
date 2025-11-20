package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/cooperativa_cafetalera?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection conn;

    public static Connection getConexion() {
        try {
            // Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USER, PASS);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: Driver MySQL no encontrado → " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("ERROR al conectar a MySQL → " + e.getMessage());
        }

        return conn;
    }
}
