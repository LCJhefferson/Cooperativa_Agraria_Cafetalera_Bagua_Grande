package conexion;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // DATOS DE CONEXIÓN (CÁMBIALOS SI ES NECESARIO)
    private static final String URL = "jdbc:mysql://localhost:3306/cooperativa_cafetalera?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection conn = null;

    // MÉTODO PARA OBTENER CONEXIÓN
    public static Connection getConnection() {

        try {
            if (conn == null || conn.isClosed()) {

                // Cargar driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Crear conexión
                conn = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("✔ Conexión exitosa a la base de datos.");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error: No se encontró el Driver de MySQL.");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("❌ Error de conexión con la base de datos.");
            e.printStackTrace();
        }

        return conn;
    }

    // MÉTODO PARA CERRAR CONEXIÓN
    public static void cerrarConexion() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("↪ Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("⚠ No se pudo cerrar la conexión.");
            e.printStackTrace();
        }
    }
}
