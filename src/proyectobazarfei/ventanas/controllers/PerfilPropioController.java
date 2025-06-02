package proyectobazarfei.ventanas.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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
import proyectobazarfei.system.objects.dao.ProductoDAO;
import proyectobazarfei.system.objects.daoIMPL.PerfilUsuarioDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.ProductoDAOImpl;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;
import proyectobazarfei.system.objects.vo.ProductoVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class PerfilPropioController {

    @FXML
    private Label apodoLabel;

    @FXML
    private Label ayudaLabel;

    @FXML
    private Pane barraLateralPane;

    @FXML
    private Label calificacionLabel;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private Label cerrarSesionLabel;

    @FXML
    private Label chatsLabel;

    @FXML
    private Label comprasLabel;

    @FXML
    private Label configuracionLabel;

    @FXML
    private Pane contenidoPanel;

    @FXML
    private Label descripcionLabel;

    @FXML
    private TextArea descripcionTextArea;

    @FXML
    private Button editarPerfilButton;

    @FXML
    private ImageView imagenPerfilImageView;

    @FXML
    private Pane imagenPerfilPane;

    @FXML
    private Label inicioOpcionLabel;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Label nombreLabel;

    @FXML
    private Label perfilLabel;

    @FXML
    private AnchorPane perfilPropioAnchorPane;

    @FXML
    private FlowPane productosFlowPane;

    @FXML
    private Label productosLabel;

    @FXML
    private ScrollPane productosScrollPane;

    @FXML
    private Label publicarProductoLabel;

    @FXML
    private Label publicarProductoLabel1;

    @FXML
    private Label tituloLabel;

    @FXML
    private Label ventasLabel;
    
    @FXML
    public void initialize() {
        LogManager.info("Obteniendo productos del perfil.");
        
        UsuarioVO usuario = SesionManager.obtenerUsuarioSesionActiva();
        
        if (usuario == null) {
            AlertaSistema.error("No existe un usuario asociado a la sesión activa.");
            return;
        }

        PerfilUsuarioDAO perfilDAO = new PerfilUsuarioDAOImpl();
        PerfilUsuarioVO perfil = perfilDAO.obtenerPerfilPorUsuarioId(usuario.getId());

        if (perfil == null) {
            AlertaSistema.error("No existe un perfil asociado a este usuario.");
            return;
        }

        nombreLabel.setText(usuario.getNombre());
        apodoLabel.setText(usuario.getApodo());
        descripcionTextArea.setPromptText(perfil.getDescripcion());
        calificacionLabel.setText("Calificación: " + perfil.getCalificacionPromedio() + "/5");

        if (perfil.getFotoPerfil() != null && !perfil.getFotoPerfil().isBlank()) {
            File archivoFoto = new File(perfil.getFotoPerfil());

            if (archivoFoto.exists()) {
                LogManager.debug("[VERIFICACIÓN EXITOSA] Foto de perfil encontrada en: " + archivoFoto.getAbsolutePath());
                Image imagen = new Image(archivoFoto.toURI().toString());
                imagenPerfilImageView.setClip(new Circle(150, 150, 150));
                imagenPerfilImageView.setImage(imagen);
            } else {
                LogManager.error("[ERROR] No se encontró la foto de perfil: " + archivoFoto.getAbsolutePath());
            }
        }


        ProductoDAO productoDAO = new ProductoDAOImpl();
        List<ProductoVO> productos = productoDAO.obtenerProductosPorPerfilId(perfil.getId());

        productosFlowPane.getChildren().clear();
        for (ProductoVO producto : productos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyectobazarfei/ventanas/fxml/TarjetaPublicacion.fxml"));
                Node nodo = loader.load();
                TarjetaPublicacionController controller = loader.getController();
                controller.rellenarDatosPublicacion(producto, perfilPropioAnchorPane);
                productosFlowPane.getChildren().add(nodo);
            } catch (IOException e) {
                LogManager.error("No se pudo cargar tarjeta de producto: " + e.getMessage());
            }
        }
    }
    
    @FXML
    void verPerfilesFavoritos(ActionEvent event) {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilesFavoritos.fxml");
    }

    @FXML
    void perfil(MouseEvent event) throws IOException {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");
    }

    @FXML
    void menu(MouseEvent event) throws IOException {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }
    
    @FXML
    void publicarProducto(MouseEvent event) throws IOException {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProducto.fxml");
    }

    @FXML
    void productosActivos(MouseEvent event) throws IOException {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/ProductosActivos.fxml");
    }

    @FXML
    void chats(MouseEvent event) throws IOException {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/Chats.fxml");
    }

    @FXML
    void compras(MouseEvent event) throws IOException {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/Compras.fxml");
    }

    @FXML
    void ventas(MouseEvent event) throws IOException {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/Ventas.fxml");
    }

    @FXML
    void configuracion(MouseEvent event) throws IOException {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/Configuracion.fxml");
    }

    @FXML
    void ayuda(MouseEvent event) throws IOException {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/Ayuda.fxml");
    }

    @FXML
    void cerrarSesion(MouseEvent event) throws IOException {
        new CambiarVentana(perfilPropioAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
