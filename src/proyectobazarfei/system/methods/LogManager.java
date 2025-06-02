package proyectobazarfei.system.methods;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase encargada de gestionar el registro de eventos del sistema.
 * Escribe mensajes con marcas de tiempo en un archivo de log local.
 */
public class LogManager {

    // Ruta del archivo donde se escriben los logs
    private static final String LOG_FILE = "src/proyectobazarfei/resources/system/files/log.txt";

    // Formato para la marca de tiempo en cada entrada
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Tipos de mensajes que se pueden registrar.
     */
    public enum LogTipo {
        INFO, ADVERTENCIA, ERROR, DEBUG
    }

    /**
     * Método central que registra un mensaje en el log.
     *
     * @param tipo    Tipo de mensaje (INFO, ADVERTENCIA, ERROR, DEBUG)
     * @param mensaje Contenido del mensaje
     */
    public static void log(LogTipo tipo, String mensaje) {
        String entrada = String.format("[%s] [%s] %s", LocalDateTime.now().format(FORMATTER), tipo, mensaje);

        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println(entrada);
        } catch (IOException e) {
            System.err.println("Error al escribir en el log: " + e.getMessage());
            // Se intenta registrar el fallo del log en consola
            System.out.println(String.format("[%s] [ERROR] Error al registrar en archivo log: %s",
                    LocalDateTime.now().format(FORMATTER), e.getMessage()));
        }

        // También mostrar en consola
        System.out.println(entrada);
    }

    /**
     * Atajo para registrar mensaje informativo.
     *
     * @param mensaje Mensaje a registrar
     */
    public static void info(String mensaje) {
        log(LogTipo.INFO, mensaje);
    }

    /**
     * Atajo para registrar mensaje de advertencia.
     *
     * @param mensaje Mensaje a registrar
     */
    public static void advertencia(String mensaje) {
        log(LogTipo.ADVERTENCIA, mensaje);
    }

    /**
     * Atajo para registrar mensaje de error.
     *
     * @param mensaje Mensaje a registrar
     */
    public static void error(String mensaje) {
        log(LogTipo.ERROR, mensaje);
    }

    /**
     * Atajo para registrar mensaje de depuración.
     *
     * @param mensaje Mensaje a registrar
     */
    public static void debug(String mensaje) {
        log(LogTipo.DEBUG, mensaje);
    }
}
