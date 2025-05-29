package proyectobazarfei.system.methods;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CambiarVentana {

    // Constructor básico
    public CambiarVentana(AnchorPane ventanaActualAnchorPane, String rutaSiguienteFXML) {
        this(ventanaActualAnchorPane, rutaSiguienteFXML, null);
    }

    // Constructor con paso de objeto al controlador
    public CambiarVentana(AnchorPane ventanaActualAnchorPane, String rutaSiguienteFXML, Object objetoParaControlador) {
        try {
            LogManager.info("Cambiando a ventana: " + rutaSiguienteFXML);

            if (rutaSiguienteFXML.equals("/proyectobazarfei/ventanas/fxml/Login.fxml")) {
                SesionManager.olvidarSesionGuardada();
            }

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(rutaSiguienteFXML)));
            AnchorPane ventanaSiguiente = loader.load();

            if (objetoParaControlador != null) {
                Object controller = loader.getController();

                // Busca un método "set..." que sea compatible con el objeto que se quiere pasar
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

            // Redimensionar y centrar ventana
            Stage stage = (Stage) ventanaActualAnchorPane.getScene().getWindow();
            stage.setWidth(1920);
            stage.setHeight(1080);

            Rectangle pantalla = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            stage.setX((pantalla.getWidth() - stage.getWidth()) / 2);
            stage.setY((pantalla.getHeight() - stage.getHeight()) / 2);

            ventanaActualAnchorPane.getChildren().setAll(ventanaSiguiente);

        } catch (IOException e) {
            LogManager.error("No se pudo cargar la ventana: " + rutaSiguienteFXML + ". Error: " + e.getMessage());
            AlertaSistema.error("Error al cambiar de ventana.");
        } catch (Exception e) {
            LogManager.error("Error general en cambio de ventana: " + e.getMessage());
        }
    }
}
