package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.methods.SesionManager;
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class LoginController {
    
    public static UsuarioVO sesion = null;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private PasswordField contrasenaTextField;

    @FXML
    private TextField correoTextField;

    @FXML
    private Button ingresarButton;

    @FXML
    private Label iniciarSesionLabel;

    @FXML
    private AnchorPane loginAnchorPane;

    @FXML
    private Label olvideContraseña;

    @FXML
    private CheckBox recuerdameCheckBox;

    @FXML
    private Label registrarseLabelButton;

    @FXML
    void ingresar(ActionEvent event) throws IOException {
        
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        String correo = correoTextField.getText().trim();
        String contrasena = contrasenaTextField.getText().trim();
        
        if (correo.isEmpty() || contrasena.isEmpty()) {
            AlertaSistema.error("Por favor, rellena todos los espacios.");
            return;
        }
        
        if (!correo.endsWith("@estudiantes.uv.mx")) {
            AlertaSistema.error("El correo debe terminar con '@estudiantes.uv.mx'.");
            return;
        }
        
        UsuarioVO usuario = usuarioDAO.iniciarSesion(correo, contrasena);
        
        if (usuario == null) {
            AlertaSistema.error("No se encontró el usuario.");
            return;
        }
        
        SesionManager.guardarSesionActiva(usuario.getCorreo());
        if (recuerdameCheckBox.isSelected()) {
            SesionManager.guardarSesionRecordada(usuario.getCorreo());
        }
        
        sesion = usuario;
        
        AlertaSistema.info("Inicio de sesión exitoso. Bienvenido, " + sesion.getApodo());
        
        new CambiarVentana(loginAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }

    @FXML
    void olvideMiContrasena(MouseEvent event) throws IOException {
        new CambiarVentana(loginAnchorPane, "/proyectobazarfei/ventanas/fxml/RecuperarContrasenaPrimerPaso.fxml");
    }

    @FXML
    void registrarse(MouseEvent event) throws IOException {
        new CambiarVentana(loginAnchorPane, "/proyectobazarfei/ventanas/fxml/Registro.fxml");
    }
    
    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
