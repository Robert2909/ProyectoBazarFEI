package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import proyectobazarfei.system.methods.CambiarVentana;

public class RegistroExitoController {

    @FXML
    private AnchorPane RegistroExitoAnchorPane;

    @FXML
    private Button aceptarButton;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private Label registroCompletoLabel;

    @FXML
    void aceptar(ActionEvent event) throws IOException {
        new CambiarVentana(RegistroExitoAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }

}
