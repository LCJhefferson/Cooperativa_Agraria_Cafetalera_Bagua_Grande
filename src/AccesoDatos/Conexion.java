/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AccesoDatos;

import java.sql.*;

public class Conexion {
    private static String url = "jdbc:mysql://localhost:3306/CA_bagua";
    private static String user = "root";
    private static String password = "";
    
    
    public static Connection conectar() throws SQLException {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(url, user, password);
            //System.out.println("CONEXION EXITOSA!");
        } catch (SQLException e) {
            throw e;
        }
        return conexion;
    }
}
