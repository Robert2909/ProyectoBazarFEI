package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.objects.vo.PreguntaSeguridadVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class RegistroController {
    
    public static String nombre;
    public static String apodo;
    public static String correo;
    public static String contrasena;
    public static String contrasenaRepetida;

    @FXML
    private AnchorPane RegistroAnchorPane;

    @FXML
    private TextField apodoTextField;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private PasswordField contrasenaTextField;

    @FXML
    private TextField correoTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private Button registrarseButton;

    @FXML
    private Label registrarseLabel;

    @FXML
    private Label regresarLoginButton;

    @FXML
    private PasswordField repetirContrasenaTextField;

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void registrarse(ActionEvent event) throws IOException {
        nombre = nombreTextField.getText();
        apodo = apodoTextField.getText();
        correo = correoTextField.getText();
        contrasena = contrasenaTextField.getText();
        contrasenaRepetida = repetirContrasenaTextField.getText();
        
        if (nombre.isEmpty() ||
            apodo.isEmpty() ||
            correo.isEmpty() ||
            contrasena.isEmpty() ||
            contrasenaRepetida.isEmpty()) {
            AlertaSistema.error("Por favor, ingresa todos los datos.");
            return;
        }
        
        if (!correo.contains("@estudiantes.uv.mx")) {
            AlertaSistema.error("El correo debe contener '@estudiantes.uv.mx'.");
            return;
        }
        
        if (!contrasena.equals(contrasenaRepetida)) {
            AlertaSistema.error("Las contraseñas no coinciden.");
            return;
        }
        
        new CambiarVentana(RegistroAnchorPane, "/proyectobazarfei/ventanas/fxml/RegistroSiguiente.fxml");
    }

    @FXML
    void regresarLogin(MouseEvent event) throws IOException {
        new CambiarVentana(RegistroAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }
}
