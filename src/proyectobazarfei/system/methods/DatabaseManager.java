package proyectobazarfei.system.methods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar las conexiones a la base de datos Oracle.
 * Proporciona métodos para obtener y cerrar conexiones, y carga el driver en un bloque estático.
 */
public class DatabaseManager {

    // Datos de conexión a la base de datos Oracle
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "C##usuarioBD";
    private static final String PASSWORD = "123456789";

    // Carga del driver de Oracle al iniciar la clase
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            LogManager.debug("Driver de Oracle cargado correctamente.");
        } catch (ClassNotFoundException e) {
            LogManager.error("No se pudo cargar el driver de Oracle: " + e.getMessage());
            AlertaSistema.error("No se pudo cargar el driver de Oracle.");
        }
    }

    /**
     * Obtiene una conexión activa a la base de datos.
     *
     * @return Objeto Connection conectado a Oracle
     * @throws SQLException si ocurre un error en la conexión
     */
    public static Connection getConnection() throws SQLException {
        LogManager.debug("Estableciendo conexión con la base de datos...");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Cierra una conexión existente si no es null.
     *
     * @param conn La conexión a cerrar
     */
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                LogManager.debug("Cerrando sesión en la base de datos...");
                conn.close();
            } catch (SQLException e) {
                LogManager.error("Error al cerrar la conexión: " + e.getMessage());
                AlertaSistema.error("Error al cerrar la conexión.");
            }
        }
    }
}
