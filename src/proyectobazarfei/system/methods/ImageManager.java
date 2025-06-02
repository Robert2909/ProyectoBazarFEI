package proyectobazarfei.system.methods;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * Clase encargada de gestionar la copia y almacenamiento de imágenes
 * utilizadas en productos y perfiles dentro del sistema BazarFEI.
 */
public class ImageManager {

    // Ruta base temporal para almacenamiento de imágenes
    private static final Path BASE_DIR = Paths.get(System.getProperty("user.home"), "BazarFEI", "temp");

    // Subrutas relativas para distintos tipos de imágenes
    private static final String RUTA_PORTADAS = "productos/portadas/";
    private static final String RUTA_IMAGENES = "productos/imagenes/";
    private static final String RUTA_PERFILES = "perfiles/";

    /**
     * Guarda la imagen de portada de un producto en su ruta correspondiente.
     *
     * @param imagenOriginal Archivo original seleccionado por el usuario
     * @return Ruta absoluta donde fue guardada la imagen
     * @throws IOException Si ocurre un error durante el guardado
     */
    public static String guardarPortadaProducto(File imagenOriginal) throws IOException {
        return copiarImagen(imagenOriginal, RUTA_PORTADAS);
    }

    /**
     * Guarda una imagen adicional del producto.
     *
     * @param imagenOriginal Archivo original seleccionado por el usuario
     * @return Ruta absoluta donde fue guardada la imagen
     * @throws IOException Si ocurre un error durante el guardado
     */
    public static String guardarImagenProducto(File imagenOriginal) throws IOException {
        return copiarImagen(imagenOriginal, RUTA_IMAGENES);
    }

    /**
     * Guarda una foto de perfil del usuario.
     *
     * @param imagenOriginal Archivo original seleccionado por el usuario
     * @return Ruta absoluta donde fue guardada la imagen
     * @throws IOException Si ocurre un error durante el guardado
     */
    public static String guardarFotoPerfil(File imagenOriginal) throws IOException {
        return copiarImagen(imagenOriginal, RUTA_PERFILES);
    }

    /**
     * Método interno para copiar una imagen a una carpeta destino.
     * Genera un nombre único y reemplaza si ya existe.
     *
     * @param origen          Archivo fuente a copiar
     * @param carpetaRelativa Ruta relativa de destino
     * @return Ruta absoluta donde fue guardada la imagen (con separadores Unix)
     * @throws IOException Si la imagen no existe o falla la copia
     */
    private static String copiarImagen(File origen, String carpetaRelativa) throws IOException {
        if (origen == null || !origen.exists()) {
            LogManager.error("Intento de guardar imagen fallido. Archivo no encontrado: " + origen);
            throw new IOException("El archivo de imagen no existe: " + origen);
        }

        // Generar nombre único con UUID
        String nombreNuevo = UUID.randomUUID() + "_" + origen.getName();
        Path destino = BASE_DIR.resolve(Paths.get(carpetaRelativa, nombreNuevo));

        // Crear carpetas si no existen
        Files.createDirectories(destino.getParent());

        // Copiar imagen al destino
        Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

        LogManager.info("Imagen copiada a: " + destino.toString());

        // Retornar ruta con formato compatible (evita doble backslash en Windows)
        return destino.toString().replace("\\", "/");
    }

    /**
     * Convierte una ruta relativa a ruta absoluta.
     *
     * @param rutaRelativa Ruta parcial o relativa
     * @return Ruta absoluta como string
     */
    public static String obtenerRutaAbsoluta(String rutaRelativa) {
        return Paths.get(rutaRelativa).toAbsolutePath().toString();
    }
}
