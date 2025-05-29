package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class RecuperarContrasenaTercerPasoController {
    
    public static UsuarioVO usuario;

    @FXML
    private AnchorPane RecuperarContrasenaTercerPasoAnchorPane;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private Label codigoLabel;

    @FXML
    private Button confirmarButton;

    @FXML
    private PasswordField nuevaContrasenaPasswordField;

    @FXML
    private PasswordField repetirContrasenaPasswordField;

    @FXML
    private Label respondeLabel;
    
    @FXML
    void confirmar(ActionEvent event) throws IOException {
        
        usuario = RecuperarContrasenaSegundoPasoController.usuario;
        
        String correo = usuario.getCorreo();
        
        String primeraContrasena = nuevaContrasenaPasswordField.getText();
        String segundaContrasena = repetirContrasenaPasswordField.getText();
        
        if (primeraContrasena == null || segundaContrasena == null) {
            AlertaSistema.error("Por favor, rellena todos los espacios.");
            return;
        }
        
        if (!primeraContrasena.equals(segundaContrasena)) {
            AlertaSistema.error("Las contraseñas no coinciden.");
            return;
        }
        
        String nuevaContrasena = primeraContrasena;
        
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        
        UsuarioVO usuarioActualizado = usuarioDAO.actualizarContrasena(correo, nuevaContrasena);
        
        if (usuarioActualizado == null) {
            AlertaSistema.error("No se pudo actualizar la contraseña.");
            return;
        }
        
        new CambiarVentana(RecuperarContrasenaTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/RecuperarContrasenaCuartoPaso.fxml");
        
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }



}
