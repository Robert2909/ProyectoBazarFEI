package proyectobazarfei.system.methods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "C##usuarioBD";
    private static final String PASSWORD = "123456789";

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            LogManager.debug("Driver de Oracle cargado correctamente.");
        } catch (ClassNotFoundException e) {
            AlertaSistema.error("No se pudo cargar el driver de Oracle.");
        }
    }

    public static Connection getConnection() throws SQLException {
        LogManager.debug("Conectando a la base de datos: " + URL);
        LogManager.debug("Conexión a la base de datos exitosa.");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                LogManager.debug("Cerrando sesión en la base de datos...");
                conn.close();
            } catch (SQLException e) {
                AlertaSistema.error("Error al cerrar la conexión.");
            }
        }
    }
}
