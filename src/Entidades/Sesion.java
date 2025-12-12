/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author jheff
 */
public class Sesion {
    // Identificador único del usuario logeado, clave foránea en muchas tablas
    public static int userId; 
    
    // Nombre de usuario (username)
    public static String userNombre; 
    
    // Nombre del rol (para determinar permisos a nivel de aplicación)
    public static String userRol; 
    
    // Opcional: para almacenar el rol ID
    public static int rolId; 
    public static String usuarioActual;

    // Método para limpiar la sesión al cerrar la aplicación o hacer logout
    public static void cerrarSesion() {
        userId = 0;
        userNombre = null;
        userRol = null;
        rolId = 0;
    }
}