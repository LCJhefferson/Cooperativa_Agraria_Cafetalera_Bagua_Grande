/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import Conexion.Conexion;
/**
 *
 * @author VICTUS´
 */
public class TestConexion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Connection conn = (Connection) Conexion.getConexion();

        if (conn != null) {
            System.out.println("TODO OK: Conectado a la base de datos.");
        } else {
            System.out.println("ERROR: No se pudo conectar.");
        }
    }
    
}
