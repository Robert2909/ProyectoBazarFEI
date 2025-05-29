package proyectobazarfei.system.methods;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {

    private static final String LOG_FILE = "src/proyectobazarfei/resources/system/files/log.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum LogTipo {
        INFO, ADVERTENCIA, ERROR, DEBUG
    }

    public static void log(LogTipo tipo, String mensaje) {
        String entrada = String.format("[%s] [%s] %s", LocalDateTime.now().format(FORMATTER), tipo, mensaje);

        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println(entrada);
        } catch (IOException e) {
            System.err.println("Error al escribir en el log: " + e.getMessage());
        }

        // También puedes mostrar en consola si deseas
        System.out.println(entrada);
    }

    public static void info(String mensaje) {
        log(LogTipo.INFO, mensaje);
    }

    public static void advertencia(String mensaje) {
        log(LogTipo.ADVERTENCIA, mensaje);
    }

    public static void error(String mensaje) {
        log(LogTipo.ERROR, mensaje);
    }

    public static void debug(String mensaje) {
        log(LogTipo.DEBUG, mensaje);
    }
}
