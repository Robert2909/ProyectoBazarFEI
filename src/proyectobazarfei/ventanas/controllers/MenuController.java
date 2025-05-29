    package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import proyectobazarfei.Main;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.methods.SesionManager;
import proyectobazarfei.system.objects.dao.ProductoDAO;
import proyectobazarfei.system.objects.daoIMPL.ProductoDAOImpl;
import proyectobazarfei.system.objects.vo.ProductoVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;
import proyectobazarfei.ventanas.controllers.LoginController;

public class MenuController {

    @FXML
    private Label ayudaLabel;

    @FXML
    private Pane barraLateralPane;

    @FXML
    private Label bienvenidaLabel;

    @FXML
    private Button buscarButton;

    @FXML
    private TextField buscarTextField;

    @FXML
    private ComboBox<String> categoriaComboBox;

    @FXML
    private Label categoriasLabel;

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
    private AnchorPane menuAnchorPane;

    @FXML
    private Label perfilLabel;

    @FXML
    private TextField precioMaximoTextField;

    @FXML
    private TextField precioMinimoTextField;

    @FXML
    private FlowPane productosFlowPane;

    @FXML
    private ScrollPane productosPane;

    @FXML
    private Label publicarProductoLabel;

    @FXML
    private Label publicarProductoLabel1;

    @FXML
    private Label ventasLabel;
    
    @FXML
    void initialize() {
        UsuarioVO usuario = SesionManager.obtenerUsuarioSesionActiva();
        bienvenidaLabel.setText("¡Bienvenido, " + usuario.getApodo() + "!");
        categoriaComboBox.getItems().addAll("Alimentos", "Servicios", "Electrónica", "Accesorios", "Escolar");
        
        ProductoDAO productoDAO = new ProductoDAOImpl();
        List<ProductoVO> productos = productoDAO.obtenerTodosLosProductos();
        cargarTarjetasDeProductos(productos);
    }
    
    public void cargarTarjetasDeProductos(List<ProductoVO> productos) {
        productosFlowPane.getChildren().clear();
        for (ProductoVO producto : productos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyectobazarfei/ventanas/fxml/TarjetaPublicacion.fxml"));
                Parent tarjeta = loader.load();

                TarjetaPublicacionController controller = loader.getController();
                controller.rellenarDatosPublicacion(producto, menuAnchorPane);

                productosFlowPane.getChildren().add(tarjeta);

            } catch (IOException e) {
                AlertaSistema.error("Error al cargar una tarjeta de producto.");
            }
        }
    }
    
    @FXML
    void buscar(ActionEvent event) {
        String textoBusqueda = buscarTextField.getText().trim();
        String categoria = categoriaComboBox.getValue();
        String precioMin = precioMinimoTextField.getText().trim();
        String precioMax = precioMaximoTextField.getText().trim();

        Double min = null, max = null;
        try {
            if (!precioMin.isEmpty()) min = Double.parseDouble(precioMin);
            if (!precioMax.isEmpty()) max = Double.parseDouble(precioMax);
        } catch (NumberFormatException e) {
            AlertaSistema.error("Los precios deben ser valores numéricos.");
            return;
        }

        if (min != null && max != null && min > max) {
            AlertaSistema.error("El precio mínimo no puede ser mayor al máximo.");
            return;
        }

        // DAO
        ProductoDAO productoDAO = new ProductoDAOImpl();
        List<ProductoVO> resultados = productoDAO.buscarProductosConFiltros(textoBusqueda, categoria, min, max);

        // Mostrar resultados
        productosFlowPane.getChildren().clear();
        for (ProductoVO producto : resultados) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyectobazarfei/ventanas/fxml/TarjetaPublicacion.fxml"));
                Node nodo = loader.load();
                TarjetaPublicacionController controller = loader.getController();
                controller.rellenarDatosPublicacion(producto, menuAnchorPane);
                productosFlowPane.getChildren().add(nodo);
            } catch (IOException e) {
                LogManager.error("Error al cargar tarjeta: " + e.getMessage());
            }
        }
    }


    @FXML
    void perfil(MouseEvent event) throws IOException {
        new CambiarVentana(menuAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");
    }

    @FXML
    void menu(MouseEvent event) throws IOException {
        new CambiarVentana(menuAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }
    
    @FXML
    void publicarProducto(MouseEvent event) throws IOException {
        new CambiarVentana(menuAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProducto.fxml");
    }

    @FXML
    void productosActivos(MouseEvent event) throws IOException {
        new CambiarVentana(menuAnchorPane, "/proyectobazarfei/ventanas/fxml/ProductosActivos.fxml");
    }

    @FXML
    void chats(MouseEvent event) throws IOException {
        new CambiarVentana(menuAnchorPane, "/proyectobazarfei/ventanas/fxml/Chats.fxml");
    }

    @FXML
    void compras(MouseEvent event) throws IOException {
        new CambiarVentana(menuAnchorPane, "/proyectobazarfei/ventanas/fxml/Compras.fxml");
    }

    @FXML
    void ventas(MouseEvent event) throws IOException {
        new CambiarVentana(menuAnchorPane, "/proyectobazarfei/ventanas/fxml/Ventas.fxml");
    }

    @FXML
    void configuracion(MouseEvent event) throws IOException {
        new CambiarVentana(menuAnchorPane, "/proyectobazarfei/ventanas/fxml/Configuracion.fxml");
    }

    @FXML
    void ayuda(MouseEvent event) throws IOException {
        new CambiarVentana(menuAnchorPane, "/proyectobazarfei/ventanas/fxml/Ayuda.fxml");
    }

    @FXML
    void cerrarSesion(MouseEvent event) throws IOException {
        new CambiarVentana(menuAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
