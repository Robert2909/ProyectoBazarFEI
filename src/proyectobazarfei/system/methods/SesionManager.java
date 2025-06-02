package proyectobazarfei.system.methods;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.UsuarioVO;

/**
 * Clase encargada de manejar la sesión del usuario dentro del sistema.
 * Permite guardar, obtener y eliminar sesiones activas o recordadas,
 * y recuperar al usuario activo a partir del correo.
 */
public class SesionManager {

    private static final String SESION_ACTIVA_FILE = "src/proyectobazarfei/resources/system/files/sesion_activa.txt";
    private static final String SESION_GUARDADA_FILE = "src/proyectobazarfei/resources/system/files/sesion_guardada.txt";

    /**
     * Guarda la sesión activa en el archivo correspondiente.
     *
     * @param correo Correo del usuario autenticado
     */
    public static void guardarSesionActiva(String correo) {
        guardarEnArchivo(SESION_ACTIVA_FILE, correo);
    }

    /**
     * Guarda la sesión recordada (cuando el usuario elige "recordar sesión").
     *
     * @param correo Correo del usuario a recordar
     */
    public static void guardarSesionRecordada(String correo) {
        guardarEnArchivo(SESION_GUARDADA_FILE, correo);
    }

    /**
     * Obtiene el correo almacenado como sesión activa.
     *
     * @return Correo del usuario activo o null si no hay sesión
     */
    public static String obtenerSesionActiva() {
        return leerDesdeArchivo(SESION_ACTIVA_FILE);
    }

    /**
     * Obtiene el correo almacenado como sesión guardada.
     *
     * @return Correo del usuario recordado o null si no existe
     */
    public static String obtenerSesionGuardada() {
        return leerDesdeArchivo(SESION_GUARDADA_FILE);
    }

    /**
     * Verifica si hay una sesión recordada disponible.
     *
     * @return true si existe una sesión recordada, false en caso contrario
     */
    public static boolean haySesionRecordada() {
        String correo = obtenerSesionGuardada();
        return correo != null && !correo.isEmpty();
    }

    /**
     * Devuelve el objeto UsuarioVO de la sesión activa actual.
     *
     * @return UsuarioVO correspondiente al correo en sesión activa, o null si no existe
     */
    public static UsuarioVO obtenerUsuarioSesionActiva() {
        String correo = obtenerSesionActiva();
        if (correo != null && !correo.isEmpty()) {
            UsuarioDAO dao = new UsuarioDAOImpl();
            return dao.obtenerUsuarioPorCorreo(correo);
        }
        return null;
    }

    /**
     * Cierra la sesión eliminando el archivo de sesión activa.
     */
    public static void cerrarSesion() {
        borrarArchivo(SESION_ACTIVA_FILE);
    }

    /**
     * Elimina la sesión guardada (por ejemplo, al cerrar sesión manualmente).
     */
    public static void olvidarSesionGuardada() {
        borrarArchivo(SESION_GUARDADA_FILE);
    }

    /**
     * Guarda un contenido de texto simple en un archivo.
     *
     * @param ruta     Ruta del archivo
     * @param contenido Texto a escribir
     */
    private static void guardarEnArchivo(String ruta, String contenido) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta))) {
            writer.write(contenido);
        } catch (IOException e) {
            LogManager.error("No se pudo guardar la sesión en: " + ruta + " - " + e.getMessage());
        }
    }

    /**
     * Lee el contenido de un archivo como texto plano.
     *
     * @param ruta Ruta del archivo
     * @return Contenido del archivo o null si no se puede leer
     */
    private static String leerDesdeArchivo(String ruta) {
        try {
            if (Files.exists(Paths.get(ruta))) {
                return new String(Files.readAllBytes(Paths.get(ruta))).trim();
            }
        } catch (IOException e) {
            LogManager.error("No se pudo leer la sesión desde: " + ruta + " - " + e.getMessage());
        }
        return null;
    }

    /**
     * Borra un archivo si existe.
     *
     * @param ruta Ruta del archivo a borrar
     */
    private static void borrarArchivo(String ruta) {
        try {
            Files.deleteIfExists(Paths.get(ruta));
        } catch (IOException e) {
            LogManager.error("No se pudo borrar el archivo de sesión: " + ruta + " - " + e.getMessage());
        }
    }
}
