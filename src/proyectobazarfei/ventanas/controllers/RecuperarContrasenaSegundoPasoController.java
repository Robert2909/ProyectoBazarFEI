package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.PreguntaSeguridadVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class RecuperarContrasenaSegundoPasoController {
    
    public static UsuarioVO usuario;

    @FXML
    private AnchorPane RecuperarContrasenaSegundoPasoAnchorPane;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private Label codigoLabel;

    @FXML
    private TextField respuestaTextField;

    @FXML
    private Button confirmarButton;

    @FXML
    private PasswordField nuevaContrasenaPasswordField;

    @FXML
    private Label preguntaSeguridadLabel;

    @FXML
    private PasswordField repetirContrasenaPasswordField;

    @FXML
    private Label respondeLabel;
    
    @FXML
    void initialize() {
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        PreguntaSeguridadVO objetoPregunta = RecuperarContrasenaPrimerPasoController.usuario.getPreguntaSeguridad();
        String pregunta = objetoPregunta.getPregunta();
        preguntaSeguridadLabel.setText(pregunta);
    }

    @FXML
    void confirmar(ActionEvent event) throws IOException {
        
        usuario = RecuperarContrasenaPrimerPasoController.usuario;
        
        String respuesta = respuestaTextField.getText();
        
        if (respuesta == null) {
            AlertaSistema.error("Por favor, responde la pregunta de seguridad.");
            return;
        }
        
        String respuestaCorrecta = RecuperarContrasenaPrimerPasoController.usuario.getRespuestaSeguridad();
        
        if (!respuesta.equals(respuestaCorrecta)) {
            AlertaSistema.error("La respuesta de seguridad no es correcta.");
            return;
        }
        
        new CambiarVentana(RecuperarContrasenaSegundoPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/RecuperarContrasenaTercerPaso.fxml");
    }
    
    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}