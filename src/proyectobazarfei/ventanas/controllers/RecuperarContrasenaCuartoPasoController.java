package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import proyectobazarfei.system.methods.CambiarVentana;

public class RecuperarContrasenaCuartoPasoController {

    @FXML
    private AnchorPane RecuperarContrasenaExitosamenteAnchorPane;

    @FXML
    private Button aceptarButton;

    @FXML
    private Label contrasenaRecuperadaLabel;

    @FXML
    void aceptar(ActionEvent event) throws IOException {
        new CambiarVentana(RecuperarContrasenaExitosamenteAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }
    
    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
