package proyectobazarfei.ventanas.controllers;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.objects.vo.ChatVO;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;

public class ChatListadoController {

    private PerfilUsuarioVO contacto;

    private ChatVO chat;
    
    private ChatsController controladorPrincipal;

    @FXML
    private Label apodoContactoLabel;

    @FXML
    private AnchorPane chatListadoAnchorPane;

    @FXML
    private ImageView imagenContactoImageView;

    @FXML
    private Pane imagenContactoPane;

    public void mostrarDatos(PerfilUsuarioVO perfilContacto, ChatVO chatRelacionado, ChatsController controladorPrincipal) {
        this.contacto = perfilContacto;
        this.chat = chatRelacionado;
        this.controladorPrincipal = controladorPrincipal;

        apodoContactoLabel.setText(perfilContacto.getDatosUsuario().getApodo());

        try {
            LogManager.debug("Ruta recibida de fotoPerfil (contacto): '" + contacto.getFotoPerfil() + "'");
            if (contacto.getFotoPerfil() != null && !contacto.getFotoPerfil().isBlank()) {
                File archivoFoto = new File(contacto.getFotoPerfil());
                if (archivoFoto.exists()) {
                    LogManager.debug("[VERIFICACIÓN EXITOSA] Foto de perfil encontrada en: " + archivoFoto.getAbsolutePath());
                    imagenContactoImageView.setClip(new Circle(35, 35, 35));
                    imagenContactoImageView.setImage(new Image(archivoFoto.toURI().toString()));
                } else {
                    LogManager.error("[VERIFICACIÓN FALLIDA] No se encontró la foto de perfil: " + archivoFoto.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            LogManager.advertencia("No se pudo cargar la imagen de perfil: " + e.getMessage());
        }
        LogManager.info("Los datos de este chat en lista se mostraron. Se mostraron las verificaciones?");
    }

    @FXML
    void abrirChat(MouseEvent event) {
        if (controladorPrincipal == null || chat == null || contacto == null) {
            AlertaSistema.error("No se puede abrir el chat porque falta información del contacto o chat.");
            return;
        }

        LogManager.info("Abriendo chat con " + contacto.getDatosUsuario().getApodo());
        controladorPrincipal.mostrarChat(chat, contacto);
    }
}
