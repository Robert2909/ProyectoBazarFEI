package proyectobazarfei.system.methods;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

/**
 * Clase para mostrar alertas estilizadas en el sistema.
 * Utiliza JavaFX Alert y aplica estilos CSS personalizados.
 * También registra los mensajes en el LogManager.
 */
public class AlertaSistema {

    // Ruta del archivo CSS para estilizar las alertas
    private static final String CSS_PATH = "/proyectobazarfei/resources/system/styles/AlertaSistema.css";

    /**
     * Método central para mostrar una alerta.
     *
     * @param tipo    Tipo de alerta (INFORMATION, WARNING, ERROR)
     * @param mensaje Mensaje a mostrar en la alerta
     * @param logTipo Tipo de mensaje para registrar en LogManager
     */
    private static void mostrar(Alert.AlertType tipo, String mensaje, LogManager.LogTipo logTipo) {
        // Crear alerta sin bordes
        Alert alert = new Alert(tipo);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Aplicar estilo CSS personalizado
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(AlertaSistema.class.getResource(CSS_PATH).toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        // Mostrar y esperar confirmación del usuario
        alert.showAndWait();

        // Registrar en el log del sistema
        LogManager.log(logTipo, mensaje);
    }

    /**
     * Muestra una alerta informativa.
     *
     * @param mensaje Mensaje a mostrar
     */
    public static void info(String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, mensaje, LogManager.LogTipo.INFO);
    }

    /**
     * Muestra una alerta de advertencia.
     *
     * @param mensaje Mensaje a mostrar
     */
    public static void advertencia(String mensaje) {
        mostrar(Alert.AlertType.WARNING, mensaje, LogManager.LogTipo.ADVERTENCIA);
    }

    /**
     * Muestra una alerta de error.
     *
     * @param mensaje Mensaje a mostrar
     */
    public static void error(String mensaje) {
        mostrar(Alert.AlertType.ERROR, mensaje, LogManager.LogTipo.ERROR);
    }
}
