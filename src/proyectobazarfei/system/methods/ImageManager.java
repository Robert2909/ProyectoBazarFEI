package proyectobazarfei.system.methods;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class ImageManager {

    private static final Path BASE_DIR = Paths.get(System.getProperty("user.home"), "BazarFEI", "temp");
    private static final String RUTA_PORTADAS = "productos/portadas/";
    private static final String RUTA_IMAGENES = "productos/imagenes/";
    private static final String RUTA_PERFILES = "perfiles/";

    public static String guardarPortadaProducto(File imagenOriginal) throws IOException {
        return copiarImagen(imagenOriginal, RUTA_PORTADAS);
    }

    public static String guardarImagenProducto(File imagenOriginal) throws IOException {
        return copiarImagen(imagenOriginal, RUTA_IMAGENES);
    }

    public static String guardarFotoPerfil(File imagenOriginal) throws IOException {
        return copiarImagen(imagenOriginal, RUTA_PERFILES);
    }

    private static String copiarImagen(File origen, String carpetaRelativa) throws IOException {
        if (origen == null || !origen.exists()) {
            throw new IOException("El archivo de imagen no existe: " + origen);
        }

        String nombreNuevo = UUID.randomUUID() + "_" + origen.getName();
        Path destino = BASE_DIR.resolve(Paths.get(carpetaRelativa, nombreNuevo));

        Files.createDirectories(destino.getParent());
        Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

        LogManager.info("Imagen copiada a: " + destino.toString());

        return destino.toString().replace("\\", "/"); // Ruta absoluta tipo Unix
    }

    public static String obtenerRutaAbsoluta(String rutaRelativa) {
        return Paths.get(rutaRelativa).toAbsolutePath().toString();
    }
}
