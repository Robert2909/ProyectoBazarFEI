package proyectobazarfei.system.methods;

import com.sun.tools.javac.Main;
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

    // Constructor que acepta un objeto para pasar al controlador
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

                try {
                    Method metodo = controller.getClass().getMethod("setProducto", objetoParaControlador.getClass());
                    metodo.invoke(controller, objetoParaControlador);
                    LogManager.debug("Objeto pasado correctamente al controlador.");
                } catch (NoSuchMethodException e) {
                    LogManager.error("El controlador no tiene un método setProducto(...) compatible.");
                } catch (Exception e) {
                    LogManager.error("Error al pasar el objeto al controlador: " + e.getMessage());
                }
            }

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
        }
    }
}
