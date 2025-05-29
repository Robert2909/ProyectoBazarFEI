package proyectobazarfei.system.methods;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class SesionManager {

    private static final String SESION_ACTIVA_FILE = "src/proyectobazarfei/resources/system/files/sesion_activa.txt";
    private static final String SESION_GUARDADA_FILE = "src/proyectobazarfei/resources/system/files/sesion_guardada.txt";

    public static void guardarSesionActiva(String correo) {
        guardarEnArchivo(SESION_ACTIVA_FILE, correo);
    }

    public static void guardarSesionRecordada(String correo) {
        guardarEnArchivo(SESION_GUARDADA_FILE, correo);
    }

    public static String obtenerSesionActiva() {
        return leerDesdeArchivo(SESION_ACTIVA_FILE);
    }

    public static String obtenerSesionGuardada() {
        return leerDesdeArchivo(SESION_GUARDADA_FILE);
    }
    
    public static boolean haySesionRecordada() {
        String correo = obtenerSesionGuardada();
        return correo != null && !correo.isEmpty();
    }
    
    public static UsuarioVO obtenerUsuarioSesionActiva() {
        String correo = obtenerSesionActiva();
        if (correo != null && !correo.isEmpty()) {
            UsuarioDAO dao = new UsuarioDAOImpl();
            return dao.obtenerUsuarioPorCorreo(correo);
        }
        return null;
    }

    public static void cerrarSesion() {
        borrarArchivo(SESION_ACTIVA_FILE);
    }

    public static void olvidarSesionGuardada() {
        borrarArchivo(SESION_GUARDADA_FILE);
    }

    private static void guardarEnArchivo(String ruta, String contenido) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta))) {
            writer.write(contenido);
        } catch (IOException e) {
            LogManager.error("No se pudo guardar la sesión en: " + ruta);
        }
    }

    private static String leerDesdeArchivo(String ruta) {
        try {
            if (Files.exists(Paths.get(ruta))) {
                return new String(Files.readAllBytes(Paths.get(ruta))).trim();
            }
        } catch (IOException e) {
            LogManager.error("No se pudo leer la sesión desde: " + ruta);
        }
        return null;
    }

    private static void borrarArchivo(String ruta) {
        try {
            Files.deleteIfExists(Paths.get(ruta));
        } catch (IOException e) {
            LogManager.error("No se pudo borrar el archivo de sesión: " + ruta);
        }
    }
}
