package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.methods.SesionManager;
import proyectobazarfei.system.objects.dao.PerfilUsuarioDAO;
import proyectobazarfei.system.objects.dao.TransaccionDAO;
import proyectobazarfei.system.objects.daoIMPL.PerfilUsuarioDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.TransaccionDAOImpl;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;
import proyectobazarfei.system.objects.vo.TransaccionVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class PerfilesFavoritosController {
    
    public UsuarioVO usuarioSesion = SesionManager.obtenerUsuarioSesionActiva();

    @FXML
    private Label ayudaLabel;

    @FXML
    private Pane barraLateralPane;

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
    private Label inicioOpcionLabel;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Label perfilLabel;

    @FXML
    private AnchorPane perfilesFavoritosAnchorPane;

    @FXML
    private FlowPane perfilesFavoritosFlowPane;

    @FXML
    private ScrollPane perfilesFavoritosScrollPane;

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
        if (usuarioSesion == null) {
            LogManager.error("No existe un usuario (PerfilesFavoritosController).");
            return;
        }

        PerfilUsuarioDAO perfilUsuarioDAO = new PerfilUsuarioDAOImpl();
        PerfilUsuarioVO perfil = perfilUsuarioDAO.obtenerPerfilPorUsuarioId(usuarioSesion.getId());
        if (perfil == null) {
            LogManager.error("No existe un perfil de usuario (PerfilesFavoritosController).");
            return;
        }

        List<PerfilUsuarioVO> vendedoresFavoritos = perfilUsuarioDAO.obtenerVendedoresFavoritos(perfil.getId());
        cargarListaPerfilesFavoritos(vendedoresFavoritos);
    }
    
    private void cargarListaPerfilesFavoritos(List<PerfilUsuarioVO> perfiles) {
        LogManager.debug("Cargando lista de perfiles favoritos...");
        perfilesFavoritosFlowPane.getChildren().clear();
        for (PerfilUsuarioVO perfil : perfiles) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyectobazarfei/ventanas/fxml/PerfilFavoritoLista.fxml"));
                Node nodo = loader.load();
                
                PerfilFavoritoListaController controller = loader.getController();
                controller.rellenarDatos(perfil, perfilesFavoritosAnchorPane);
                
                perfilesFavoritosFlowPane.getChildren().add(nodo);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void perfil(MouseEvent event) throws IOException {
        new CambiarVentana(perfilesFavoritosAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");
    }

    @FXML
    void menu(MouseEvent event) throws IOException {
        new CambiarVentana(perfilesFavoritosAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }
    
    @FXML
    void publicarProducto(MouseEvent event) throws IOException {
        new CambiarVentana(perfilesFavoritosAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProducto.fxml");
    }

    @FXML
    void productosActivos(MouseEvent event) throws IOException {
        new CambiarVentana(perfilesFavoritosAnchorPane, "/proyectobazarfei/ventanas/fxml/ProductosActivos.fxml");
    }

    @FXML
    void chats(MouseEvent event) throws IOException {
        new CambiarVentana(perfilesFavoritosAnchorPane, "/proyectobazarfei/ventanas/fxml/Chats.fxml");
    }

    @FXML
    void compras(MouseEvent event) throws IOException {
        new CambiarVentana(perfilesFavoritosAnchorPane, "/proyectobazarfei/ventanas/fxml/Compras.fxml");
    }

    @FXML
    void ventas(MouseEvent event) throws IOException {
        new CambiarVentana(perfilesFavoritosAnchorPane, "/proyectobazarfei/ventanas/fxml/Ventas.fxml");
    }

    @FXML
    void configuracion(MouseEvent event) throws IOException {
        new CambiarVentana(perfilesFavoritosAnchorPane, "/proyectobazarfei/ventanas/fxml/Configuracion.fxml");
    }

    @FXML
    void ayuda(MouseEvent event) throws IOException {
        new CambiarVentana(perfilesFavoritosAnchorPane, "/proyectobazarfei/ventanas/fxml/Ayuda.fxml");
    }

    @FXML
    void cerrarSesion(MouseEvent event) throws IOException {
        new CambiarVentana(perfilesFavoritosAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
