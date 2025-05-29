package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class RecuperarContrasenaPrimerPasoController {
        
    public static UsuarioVO usuario;

    @FXML
    private AnchorPane RecuperarContrasenaPrimerPasoAnchorPane;

    @FXML
    private Button aceptarButton;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private TextField correoTextField;

    @FXML
    private Label infoLabel;

    @FXML
    private Label recuperarContrasenaLabel;

    @FXML
    void verificarInformacion(ActionEvent event) throws IOException {
        
        String correo = correoTextField.getText();
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        
        if (correo == null) {
            AlertaSistema.error("Por favor, introduce un correo.");
            return;
        }
        
        if (!correo.endsWith("@estudiantes.uv.mx")) {
            AlertaSistema.error("Por favor, escribe un correo válido.");
            return;
        }
        
        usuario = usuarioDAO.obtenerUsuarioPorCorreo(correo);
        
        if (usuario == null) {
            AlertaSistema.error("No se encontró el correo.");
            return;
        }
        
        new CambiarVentana(RecuperarContrasenaPrimerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/RecuperarContrasenaSegundoPaso.fxml");
    }
    
    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
