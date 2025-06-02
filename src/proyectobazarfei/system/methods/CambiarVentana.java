package proyectobazarfei.system.methods;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Clase para cambiar de ventana en el sistema BazarFEI usando JavaFX.
 * Permite pasar datos al nuevo controlador si es necesario, y ajusta la ventana a pantalla completa centrada.
 */
public class CambiarVentana {

    /**
     * Constructor básico que cambia de ventana sin pasar parámetros al nuevo controlador.
     *
     * @param ventanaActualAnchorPane Pane actual desde donde se hace el cambio
     * @param rutaSiguienteFXML       Ruta del archivo FXML al que se cambiará
     */
    public CambiarVentana(AnchorPane ventanaActualAnchorPane, String rutaSiguienteFXML) {
        this(ventanaActualAnchorPane, rutaSiguienteFXML, null);
    }

    /**
     * Constructor principal que cambia de ventana y permite pasar un objeto al nuevo controlador.
     *
     * @param ventanaActualAnchorPane Pane actual desde donde se hace el cambio
     * @param rutaSiguienteFXML       Ruta del archivo FXML al que se cambiará
     * @param objetoParaControlador   Objeto que se desea pasar al nuevo controlador
     */
    public CambiarVentana(AnchorPane ventanaActualAnchorPane, String rutaSiguienteFXML, Object objetoParaControlador) {
        try {
            LogManager.info("Cambiando a ventana: " + rutaSiguienteFXML);

            // Si se va a la pantalla de login, se elimina cualquier sesión guardada
            if (rutaSiguienteFXML.equals("/proyectobazarfei/ventanas/fxml/Login.fxml")) {
                SesionManager.olvidarSesionGuardada();
            }

            // Cargar la nueva ventana desde su archivo FXML
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(rutaSiguienteFXML)));
            AnchorPane ventanaSiguiente = loader.load();

            // Si se proporciona un objeto, se intenta pasar al controlador usando un método setX()
            if (objetoParaControlador != null) {
                Object controller = loader.getController();
                boolean asignado = false;

                for (Method metodo : controller.getClass().getMethods()) {
                    if (metodo.getName().startsWith("set") &&
                        metodo.getParameterCount() == 1 &&
                        metodo.getParameterTypes()[0].isAssignableFrom(objetoParaControlador.getClass())) {

                        metodo.invoke(controller, objetoParaControlador);
                        LogManager.debug("Objeto pasado al controlador usando: " + metodo.getName());
                        asignado = true;
                        break;
                    }
                }

                if (!asignado) {
                    LogManager.error("No se encontró un método set compatible en el controlador.");
                }
            }

            // Obtener la ventana actual y redimensionarla a pantalla completa centrada
            Stage stage = (Stage) ventanaActualAnchorPane.getScene().getWindow();
            stage.setWidth(1920);
            stage.setHeight(1080);

            Rectangle pantalla = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            stage.setX((pantalla.getWidth() - stage.getWidth()) / 2);
            stage.setY((pantalla.getHeight() - stage.getHeight()) / 2);

            // Reemplazar el contenido del AnchorPane actual con la nueva vista
            ventanaActualAnchorPane.getChildren().setAll(ventanaSiguiente);

        } catch (IOException e) {
            LogManager.error("No se pudo cargar la ventana: " + rutaSiguienteFXML + ". Error de IO: " + e.getMessage());
            AlertaSistema.error("Error al cambiar de ventana.");
        } catch (Exception e) {
            LogManager.error("Error general al cambiar de ventana: " + e.getMessage());
        }
    }
}
