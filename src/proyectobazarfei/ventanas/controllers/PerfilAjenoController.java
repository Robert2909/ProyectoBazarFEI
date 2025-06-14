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
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.PerfilUsuarioDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.ProductoDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;
import proyectobazarfei.system.objects.vo.ProductoVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class PerfilAjenoController {
    
    private PerfilUsuarioVO perfil;

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
    private AnchorPane perfilAjenoAnchorPane;

    @FXML
    private Label perfilLabel;

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

    }
    
    public void setPerfil(PerfilUsuarioVO perfil) {
        this.perfil = perfil;
        inicializarPerfil();
    }
    
    private void inicializarPerfil() {
        if (perfil == null) {
            AlertaSistema.error("No se recibió perfil para mostrar.");
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        UsuarioVO usuario = usuarioDAO.obtenerUsuarioPorPerfilId(perfil.getId());

        if (usuario == null) {
            AlertaSistema.error("No existe un usuario para este perfil.");
            return;
        }

        nombreLabel.setText(usuario.getNombre());
        apodoLabel.setText(usuario.getApodo());
        descripcionTextArea.setPromptText(perfil.getDescripcion());
        calificacionLabel.setText("Calificación: " + perfil.getCalificacionPromedio() + "/5");

        if (perfil.getFotoPerfil() != null && !perfil.getFotoPerfil().isEmpty()) {
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
                controller.rellenarDatosPublicacion(producto, perfilAjenoAnchorPane);
                productosFlowPane.getChildren().add(nodo);
            } catch (IOException e) {
                LogManager.error("Error al cargar tarjeta: " + e.getMessage());
            }
        }
    }

    
    @FXML
    void reportarPerfil(ActionEvent event) {

    }
    
    @FXML
    void anadirFavoritos(ActionEvent event) {
        LogManager.debug("Agregando perfil a la lista de favoritos: " + perfil.getId());
        PerfilUsuarioDAO perfilUsuarioDAO = new PerfilUsuarioDAOImpl();
        PerfilUsuarioVO perfilActual = perfilUsuarioDAO.obtenerPerfilPorUsuarioId(SesionManager.obtenerUsuarioSesionActiva().getId());
        perfilUsuarioDAO.agregarVendedorFavorito(perfilActual.getId(), perfil.getId());
    }

    @FXML
    void perfil(MouseEvent event) throws IOException {
        new CambiarVentana(perfilAjenoAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");
    }

    @FXML
    void menu(MouseEvent event) throws IOException {
        new CambiarVentana(perfilAjenoAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }
    
    @FXML
    void publicarProducto(MouseEvent event) throws IOException {
        new CambiarVentana(perfilAjenoAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProducto.fxml");
    }

    @FXML
    void productosActivos(MouseEvent event) throws IOException {
        new CambiarVentana(perfilAjenoAnchorPane, "/proyectobazarfei/ventanas/fxml/ProductosActivos.fxml");
    }

    @FXML
    void chats(MouseEvent event) throws IOException {
        new CambiarVentana(perfilAjenoAnchorPane, "/proyectobazarfei/ventanas/fxml/Chats.fxml");
    }

    @FXML
    void compras(MouseEvent event) throws IOException {
        new CambiarVentana(perfilAjenoAnchorPane, "/proyectobazarfei/ventanas/fxml/Compras.fxml");
    }

    @FXML
    void ventas(MouseEvent event) throws IOException {
        new CambiarVentana(perfilAjenoAnchorPane, "/proyectobazarfei/ventanas/fxml/Ventas.fxml");
    }

    @FXML
    void configuracion(MouseEvent event) throws IOException {
        new CambiarVentana(perfilAjenoAnchorPane, "/proyectobazarfei/ventanas/fxml/Configuracion.fxml");
    }

    @FXML
    void ayuda(MouseEvent event) throws IOException {
        new CambiarVentana(perfilAjenoAnchorPane, "/proyectobazarfei/ventanas/fxml/Ayuda.fxml");
    }

    @FXML
    void cerrarSesion(MouseEvent event) throws IOException {
        new CambiarVentana(perfilAjenoAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
