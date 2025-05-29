package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.objects.dao.PreguntaSeguridadDAO;
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.PreguntaSeguridadDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.PreguntaSeguridadVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class RegistroSiguienteController {
    
    public static String nombre = RegistroController.nombre;
    public static String apodo = RegistroController.apodo;
    public static String correo = RegistroController.correo;
    public static String contrasena = RegistroController.contrasena;
    public static String contrasenaRepetida = RegistroController.contrasenaRepetida;
    public static PreguntaSeguridadVO preguntaSeguridad;
    public static String respuesta;
    
    @FXML
    private AnchorPane RegistroSiguienteAnchorPane;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private Button finalizarButton;

    @FXML
    private Label instruccionLabel;

    @FXML
    private Label preguntaSeguridadLabel;

    @FXML
    private Label registrarseLabel;

    @FXML
    private TextField respuestaTextField;

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML
    void initialize() {
        PreguntaSeguridadDAO preguntaSeguridadDAO = new PreguntaSeguridadDAOImpl();
        preguntaSeguridad = preguntaSeguridadDAO.obtenerPreguntaAleatoria();
        preguntaSeguridadLabel.setText(preguntaSeguridad.getPregunta());
    }

    @FXML
    void finalizar(ActionEvent event) throws IOException {
        respuesta = respuestaTextField.getText();
        
        if (respuesta.isEmpty()) {
            AlertaSistema.error("Por favor, contesta la pregunta.");
            return;
        }
        
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        UsuarioVO registro = usuarioDAO.registrarUsuario(new UsuarioVO(nombre, apodo, correo, contrasena, preguntaSeguridad, respuesta));
        
        if (registro != null) {
            new CambiarVentana(RegistroSiguienteAnchorPane, "/proyectobazarfei/ventanas/fxml/RegistroExito.fxml");
        } else {
            AlertaSistema.error("No se pudo crear el usuario.");
            return;
        }
    }
}
