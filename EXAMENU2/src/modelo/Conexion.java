package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // ============================
    // CONFIGURACIÓN DE CONEXIÓN
    // ============================
    private static final String URL = "jdbc:mysql://localhost:3306/BancoDBB?serverTimezone=UTC";
    private static final String USUARIO = "root"; // <-- tu usuario de MySQL Workbench
    private static final String CONTRASENA = "#adminmiguel31"; // <-- tu contraseña de MySQL Workbench
    private static Connection conexion = null;

    // ============================
    // MÉTODO PARA CONECTARSE
    // ============================
    public static Connection getConnection() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Cargar el driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Conectar
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                System.out.println("✅ Conexión exitosa a la base de datos.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontró el driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    // ============================
    // MÉTODO PARA CERRAR CONEXIÓN
    // ============================
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
