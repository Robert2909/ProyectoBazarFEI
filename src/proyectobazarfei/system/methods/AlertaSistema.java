package proyectobazarfei.system.methods;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

public class AlertaSistema {

    private static final String CSS_PATH = "/proyectobazarfei/resources/system/styles/AlertaSistema.css";

    private static void mostrar(Alert.AlertType tipo, String mensaje, LogManager.LogTipo logTipo) {
        Alert alert = new Alert(tipo);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(AlertaSistema.class.getResource(CSS_PATH).toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait();

        // Registrar en el log
        LogManager.log(logTipo, mensaje);
    }
    
    public static void info(String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, mensaje, LogManager.LogTipo.INFO);
    }
    
    public static void advertencia(String mensaje) {
        mostrar(Alert.AlertType.WARNING, mensaje, LogManager.LogTipo.ADVERTENCIA);
    }

    public static void error(String mensaje) {
        mostrar(Alert.AlertType.ERROR, mensaje, LogManager.LogTipo.ERROR);
    }
}
