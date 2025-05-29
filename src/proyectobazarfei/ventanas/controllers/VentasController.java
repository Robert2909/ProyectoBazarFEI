
package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.methods.SesionManager;
import proyectobazarfei.system.objects.dao.PerfilUsuarioDAO;
import proyectobazarfei.system.objects.dao.TransaccionDAO;
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.PerfilUsuarioDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.TransaccionDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;
import proyectobazarfei.system.objects.vo.TransaccionVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class VentasController {
    
    public UsuarioVO usuarioSesion = SesionManager.obtenerUsuarioSesionActiva();

    @FXML
    private FlowPane listaVentasFlowPane;

    @FXML
    private ScrollPane listaVentasScrollPane;

    @FXML
    private Label ayudaLabel;

    @FXML
    private Pane barraLateralPane;

    @FXML
    private Label bienvenidaLabel;

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
    private Label publicarProductoLabel;

    @FXML
    private Label publicarProductoLabel1;

    @FXML
    private AnchorPane ventasAnchorPane;

    @FXML
    private Label ventasLabel;
    
    @FXML
    public void initialize() {
        if (usuarioSesion == null) {
            LogManager.error("No existe un usuario (VentasController).");
            return;
        }

        PerfilUsuarioDAO perfilUsuarioDAO = new PerfilUsuarioDAOImpl();
        TransaccionDAO transaccionDAO = new TransaccionDAOImpl();
        PerfilUsuarioVO perfil = perfilUsuarioDAO.obtenerPerfilPorUsuarioId(usuarioSesion.getId());
        if (perfil == null) {
            LogManager.error("No existe un perfil de usuario (VentasController).");
            return;
        }

        List<TransaccionVO> ventas = transaccionDAO.obtenerVentasPorPerfilId(perfil.getId());
        cargarVentasDelUsuario(ventas);
    }
     
    private void cargarVentasDelUsuario(List<TransaccionVO> transacciones) {
        listaVentasFlowPane.getChildren().clear();
        for (TransaccionVO transaccion : transacciones) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyectobazarfei/ventanas/fxml/VentaEnLista.fxml"));
                Node nodo = loader.load();
                
                VentaEnListaController controller = loader.getController();
                controller.rellenarDatosTransaccion(transaccion);
                
                listaVentasFlowPane.getChildren().add(nodo);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    void verProducto(MouseEvent event) throws IOException {
        LogManager.info("Ver producto.");
    }
    
    @FXML
    void perfil(MouseEvent event) throws IOException {
        new CambiarVentana(ventasAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");
    }

    @FXML
    void menu(MouseEvent event) throws IOException {
        new CambiarVentana(ventasAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }
    
    @FXML
    void publicarProducto(MouseEvent event) throws IOException {
        new CambiarVentana(ventasAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProducto.fxml");
    }

    @FXML
    void productosActivos(MouseEvent event) throws IOException {
        new CambiarVentana(ventasAnchorPane, "/proyectobazarfei/ventanas/fxml/ProductosActivos.fxml");
    }

    @FXML
    void chats(MouseEvent event) throws IOException {
        new CambiarVentana(ventasAnchorPane, "/proyectobazarfei/ventanas/fxml/Chats.fxml");
    }

    @FXML
    void compras(MouseEvent event) throws IOException {
        new CambiarVentana(ventasAnchorPane, "/proyectobazarfei/ventanas/fxml/Compras.fxml");
    }

    @FXML
    void ventas(MouseEvent event) throws IOException {
        new CambiarVentana(ventasAnchorPane, "/proyectobazarfei/ventanas/fxml/Ventas.fxml");
    }

    @FXML
    void configuracion(MouseEvent event) throws IOException {
        new CambiarVentana(ventasAnchorPane, "/proyectobazarfei/ventanas/fxml/Configuracion.fxml");
    }

    @FXML
    void ayuda(MouseEvent event) throws IOException {
        new CambiarVentana(ventasAnchorPane, "/proyectobazarfei/ventanas/fxml/Ayuda.fxml");
    }

    @FXML
    void cerrarSesion(MouseEvent event) throws IOException {
        new CambiarVentana(ventasAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
