package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.objects.dao.PerfilUsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.PerfilUsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;
import proyectobazarfei.system.objects.vo.ProductoVO;
import static proyectobazarfei.ventanas.controllers.ProductoController.perfilVendedor;

public class ComprarController {
    
    private ProductoVO productoActual;
    
    public static PerfilUsuarioVO perfilVendedor;

    @FXML
    private Label TituloCategoriaLabel;

    @FXML
    private Label apodoVendedorLabel;

    @FXML
    private Label apodoVendedorLabel1;

    @FXML
    private Label ayudaLabel;

    @FXML
    private Pane barraLateralPane;

    @FXML
    private Label categoriaLabel;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private Label cerrarSesionLabel;

    @FXML
    private Label chatsLabel;

    @FXML
    private AnchorPane comprarAnchorPane;

    @FXML
    private Button comprarButton;

    @FXML
    private Label comprasLabel;

    @FXML
    private Label configuracionLabel;

    @FXML
    private Pane contenidoPane;

    @FXML
    private Label datosEntregaLabel;

    @FXML
    private Pane datosVendedorPane;

    @FXML
    private Label descripcionLabel;

    @FXML
    private TextArea descripcionTextArea;

    @FXML
    private Label elegirMetodoEntregaLabel;

    @FXML
    private Label elegirMetodoPagoLabel;

    @FXML
    private TextField horaTextField;

    @FXML
    private ImageView imagenVendedorImageView;

    @FXML
    private Pane imagenVendedorPane;
    
    @FXML
    private Label informacionLabel;

    @FXML
    private Label inicioOpcionLabel;

    @FXML
    private ImageView logoImageView;

    @FXML
    private TextField lugarTextField;

    @FXML
    private ComboBox<String> metodoEntregaComboBox;

    @FXML
    private ComboBox<String> metodoPagoComboBox;

    @FXML
    private Label perfilLabel;

    @FXML
    private ImageView portadaImageView;

    @FXML
    private Pane portadaPane;

    @FXML
    private Label precioLabel;

    @FXML
    private Label publicarProductoLabel;

    @FXML
    private Label publicarProductoLabel1;

    @FXML
    private Button regresarButton;

    @FXML
    private Label tituloLabel;

    @FXML
    private Label tituloPrecioLabel;

    @FXML
    private Label ventasLabel;

    @FXML
    private Button verPerfilVendedorButton;
    
    public void setProducto(ProductoVO producto) {
        this.productoActual = producto;

        if (productoActual == null) {
            AlertaSistema.error("No existe un producto.");
            return;
        }

        tituloLabel.setText(productoActual.getTitulo());
        precioLabel.setText("$" + productoActual.getPrecio());
        categoriaLabel.setText(productoActual.getCategoria());
        descripcionTextArea.setPromptText(productoActual.getDescripcion());

        if (productoActual.getPortada() != null) {
            InputStream stream = getClass().getResourceAsStream(productoActual.getPortada());
            if (stream != null) {
                portadaImageView.setImage(new Image(stream));
            }
        }

        PerfilUsuarioDAO perfilDAO = new PerfilUsuarioDAOImpl();
        perfilVendedor = perfilDAO.obtenerPerfilPorProductoId(productoActual.getId());

        if (perfilVendedor != null && perfilVendedor.getDatosUsuario() != null) {
            apodoVendedorLabel.setText(perfilVendedor.getDatosUsuario().getApodo());

            if (perfilVendedor.getFotoPerfil() != null) {
                InputStream stream = getClass().getResourceAsStream(perfilVendedor.getFotoPerfil());
                if (stream != null) {
                    imagenVendedorImageView.setClip(new Circle(37, 37, 37));
                    imagenVendedorImageView.setImage(new Image(stream));
                }
            }
        } else {
            LogManager.error("No se pudo cargar el perfil del vendedor.");
        }
        
        metodoPagoComboBox.getItems().addAll("Efectivo", "Transferencia", "Tarjeta");
        metodoEntregaComboBox.getItems().addAll("Ven a entregarlo", "Voy a recogerlo");
        
        datosEntregaLabel.setVisible(false);
        lugarTextField.setVisible(false);
        horaTextField.setVisible(false);
        informacionLabel.setVisible(false);
        
        metodoEntregaComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                datosEntregaLabel.setVisible(false);
                informacionLabel.setVisible(false);
                lugarTextField.setVisible(false);
                horaTextField.setVisible(false);
                return;
            }
            if (newValue.equals("Ven a entregarlo")) {
                datosEntregaLabel.setVisible(true);
                informacionLabel.setVisible(false);
                lugarTextField.setVisible(true);
                horaTextField.setVisible(true);
            } else if (newValue.equals("Voy a recogerlo")) {
                datosEntregaLabel.setVisible(true);
                informacionLabel.setVisible(true);
                lugarTextField.setVisible(false);
                horaTextField.setVisible(false);
            } else {
                datosEntregaLabel.setVisible(false);
                informacionLabel.setVisible(false);
                lugarTextField.setVisible(false);
                horaTextField.setVisible(false);
            }
        });
    }
    
    @FXML
    void verPerfilVendedor(ActionEvent event) {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilAjeno.fxml", this.perfilVendedor);
    }

    @FXML
    void hacerCompra(ActionEvent event) {
        
    }

    @FXML
    void perfil(MouseEvent event) throws IOException {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");
    }

    @FXML
    void menu(MouseEvent event) throws IOException {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }
    
    @FXML
    void publicarProducto(MouseEvent event) throws IOException {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProducto.fxml");
    }

    @FXML
    void productosActivos(MouseEvent event) throws IOException {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/ProductosActivos.fxml");
    }

    @FXML
    void chats(MouseEvent event) throws IOException {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/Chats.fxml");
    }

    @FXML
    void compras(MouseEvent event) throws IOException {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/Compras.fxml");
    }

    @FXML
    void ventas(MouseEvent event) throws IOException {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/Ventas.fxml");
    }

    @FXML
    void configuracion(MouseEvent event) throws IOException {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/Configuracion.fxml");
    }

    @FXML
    void ayuda(MouseEvent event) throws IOException {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/Ayuda.fxml");
    }

    @FXML
    void cerrarSesion(MouseEvent event) throws IOException {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML
    void regresarVentanaAnterior(ActionEvent event) {
        new CambiarVentana(comprarAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }
}
