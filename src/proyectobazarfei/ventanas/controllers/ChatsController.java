package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.methods.SesionManager;
import proyectobazarfei.system.objects.dao.PerfilUsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.ChatDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.PerfilUsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.ChatVO;
import proyectobazarfei.system.objects.vo.MensajeVO;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class ChatsController implements Initializable {
    
    private ChatVO chatActual;
    
    private PerfilUsuarioVO contactoActual;

    @FXML
    private Label apodoContactoBarraSuperiorLabel;

    @FXML
    private Label ayudaLabel;

    @FXML
    private Pane barraInferiorChatPane;

    @FXML
    private Pane barraLateralPane;

    @FXML
    private Pane barraSuperiorChatPane;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private Label cerrarSesionLabel;

    @FXML
    private Pane chatPane;

    @FXML
    private AnchorPane chatsAnchorPane;

    @FXML
    private Label chatsLabel;

    @FXML
    private Label comprasLabel;

    @FXML
    private Label configuracionLabel;

    @FXML
    private Pane contenidoPanel;

    @FXML
    private Button enviarMensajeButton;

    @FXML
    private TextField escribirMensajeTextField;

    @FXML
    private FlowPane espacioMensajeriaFlowPane;

    @FXML
    private ScrollPane espacioMensajeriaScrollPane;

    @FXML
    private ImageView fotoPerfilContactoBarraSuperiorImageView;

    @FXML
    private Pane fotoPerfilContactoBarraSuperiorPane;

    @FXML
    private Label inicioOpcionLabel;

    @FXML
    private FlowPane listaDeChatsFlowPane;

    @FXML
    private ScrollPane listaDeChatsScrollPane;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Label perfilLabel;

    @FXML
    private Label publicarProductoLabel;

    @FXML
    private Label publicarProductoLabel1;

    @FXML
    private Label tituloLabel;

    @FXML
    private Label ventasLabel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarListaChats();
    }
    
    private void cargarListaChats() {
        listaDeChatsFlowPane.getChildren().clear();

        try {
            UsuarioVO usuarioSesion = SesionManager.obtenerUsuarioSesionActiva();
            PerfilUsuarioVO miPerfil = new PerfilUsuarioDAOImpl().obtenerPerfilPorUsuarioId(usuarioSesion.getId());

            List<ChatVO> misChats = new ChatDAOImpl().obtenerChatsPorPerfilId(miPerfil.getId());

            // Ordenar por fecha del último mensaje (mayor a menor)
            misChats.sort((a, b) -> {
                List<MensajeVO> mensajesA = a.getMensajes();
                List<MensajeVO> mensajesB = b.getMensajes();

                if (mensajesA.isEmpty() || mensajesB.isEmpty()) return 0;

                Date fechaA = mensajesA.get(mensajesA.size() - 1).getFechaEnvio();
                Date fechaB = mensajesB.get(mensajesB.size() - 1).getFechaEnvio();

                return fechaB.compareTo(fechaA);
            });

            for (ChatVO chat : misChats) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyectobazarfei/ventanas/fxml/ChatListado.fxml"));
                AnchorPane panel = loader.load();
                ChatListadoController controller = loader.getController();

                // Determinar el otro perfil del chat (no soy yo)
                PerfilUsuarioVO otro = (chat.getVendedor().getId() == miPerfil.getId())
                    ? chat.getComprador()
                    : chat.getVendedor();

                controller.mostrarDatos(otro, chat, this);

                listaDeChatsFlowPane.getChildren().add(panel);
            }

        } catch (Exception e) {
            LogManager.error("Error al cargar los chats: " + e.getMessage());
            AlertaSistema.error("No se pudieron cargar tus chats.");
        }
    }
    
    public void mostrarChat(ChatVO chat, PerfilUsuarioVO contacto) {

        this.chatActual = chat;
        this.contactoActual = contacto;

        try {
            // Mostrar nombre y foto del contacto
            apodoContactoBarraSuperiorLabel.setText(contacto.getDatosUsuario().getApodo());
            if (contacto.getFotoPerfil() != null) {
                Image imagen = new Image(getClass().getResourceAsStream(contacto.getFotoPerfil()));
                fotoPerfilContactoBarraSuperiorImageView.setClip(new Circle(35, 35, 35));
                fotoPerfilContactoBarraSuperiorImageView.setImage(imagen);
            }

            // Limpiar mensajes anteriores
            espacioMensajeriaFlowPane.getChildren().clear();

            // Obtener mi perfil (usuario de sesión)
            PerfilUsuarioVO miPerfil = new PerfilUsuarioDAOImpl().obtenerPerfilPorUsuarioId(
                SesionManager.obtenerUsuarioSesionActiva().getId()
            );

            // Obtener el chat completo
            ChatVO chatCompleto = new ChatDAOImpl().obtenerChatPorId(chat.getId());

            for (MensajeVO mensaje : chatCompleto.getMensajes()) {
                FXMLLoader loader;
                AnchorPane mensajePane;

                boolean esMio = mensaje.getRemitente() != null && 
                                mensaje.getRemitente().getId() == miPerfil.getId();

                if (esMio) {
                    loader = new FXMLLoader(getClass().getResource("/proyectobazarfei/ventanas/fxml/ChatMensajeEnviado.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/proyectobazarfei/ventanas/fxml/ChatMensajeRecibido.fxml"));
                }

                mensajePane = loader.load();

                if (esMio) {
                    ChatMensajeEnviadoController controller = loader.getController();
                    controller.setMensaje(mensaje);
                } else {
                    ChatMensajeRecibidoController controller = loader.getController();
                    controller.setMensaje(mensaje);
                }

                espacioMensajeriaFlowPane.getChildren().add(mensajePane);
            }

            espacioMensajeriaScrollPane.setVvalue(1.0);

        } catch (Exception e) {
            LogManager.error("Error al mostrar el chat: " + e.getMessage());
            AlertaSistema.error("No se pudo mostrar el chat.");
        }
    }

    
    @FXML
    void enviarMensaje(ActionEvent event) {
        if (chatActual == null) {
            AlertaSistema.advertencia("Selecciona un chat antes de enviar un mensaje.");
            return;
        }

        String texto = escribirMensajeTextField.getText().trim();

        if (texto.isEmpty()) {
            AlertaSistema.advertencia("No puedes enviar un mensaje vacío.");
            return;
        }

        try {
            Date fechaActual = new Date();
            String hora = new SimpleDateFormat("HH:mm:ss").format(fechaActual);

            PerfilUsuarioDAO perfilUsuarioDAO = new PerfilUsuarioDAOImpl();
            PerfilUsuarioVO remitente = perfilUsuarioDAO.obtenerPerfilPorUsuarioId(SesionManager.obtenerUsuarioSesionActiva().getId());

            MensajeVO nuevoMensaje = new MensajeVO(texto, fechaActual, hora, remitente);
            nuevoMensaje.setRemitente(remitente); // <-- NUEVO

            new ChatDAOImpl().agregarMensajeAlChat(chatActual.getId(), nuevoMensaje);

            escribirMensajeTextField.clear();

            mostrarChat(chatActual, contactoActual);

        } catch (Exception e) {
            LogManager.error("Error al enviar mensaje: " + e.getMessage());
            AlertaSistema.error("No se pudo enviar el mensaje.");
        }
    }


    @FXML
    void perfil(MouseEvent event) throws IOException {
        new CambiarVentana(chatsAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");
    }

    @FXML
    void menu(MouseEvent event) throws IOException {
        new CambiarVentana(chatsAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }
    
    @FXML
    void publicarProducto(MouseEvent event) throws IOException {
        new CambiarVentana(chatsAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProducto.fxml");
    }

    @FXML
    void productosActivos(MouseEvent event) throws IOException {
        new CambiarVentana(chatsAnchorPane, "/proyectobazarfei/ventanas/fxml/ProductosActivos.fxml");
    }

    @FXML
    void chats(MouseEvent event) throws IOException {
        new CambiarVentana(chatsAnchorPane, "/proyectobazarfei/ventanas/fxml/Chats.fxml");
    }

    @FXML
    void compras(MouseEvent event) throws IOException {
        new CambiarVentana(chatsAnchorPane, "/proyectobazarfei/ventanas/fxml/Compras.fxml");
    }

    @FXML
    void ventas(MouseEvent event) throws IOException {
        new CambiarVentana(chatsAnchorPane, "/proyectobazarfei/ventanas/fxml/Ventas.fxml");
    }

    @FXML
    void configuracion(MouseEvent event) throws IOException {
        new CambiarVentana(chatsAnchorPane, "/proyectobazarfei/ventanas/fxml/Configuracion.fxml");
    }

    @FXML
    void ayuda(MouseEvent event) throws IOException {
        new CambiarVentana(chatsAnchorPane, "/proyectobazarfei/ventanas/fxml/Ayuda.fxml");
    }

    @FXML
    void cerrarSesion(MouseEvent event) throws IOException {
        new CambiarVentana(chatsAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
