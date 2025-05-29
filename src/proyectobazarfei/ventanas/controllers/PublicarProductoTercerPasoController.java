package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import proyectobazarfei.system.methods.CambiarVentana;

public class PublicarProductoTercerPasoController {
    
    @FXML
    private AnchorPane PublicarProductoTercerPasoAnchorPane;

    @FXML
    private Button aceptarButton;

    @FXML
    private Label ayudaLabel;

    @FXML
    private Pane barraLateralPane;

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
    private Label tituloLabel;

    @FXML
    private Label ventasLabel;

    @FXML
    void aceptar(ActionEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }

    @FXML
    void menu(MouseEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");

    }
    @FXML
    void publicarProducto(MouseEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProducto.fxml");

    }

    @FXML
    void productosActivos(MouseEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/ProductosActivos.fxml");

    }

    @FXML
    void chats(MouseEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/Chats.fxml");

    }

    @FXML
    void compras(MouseEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/Compras.fxml");

    }

    @FXML
    void ventas(MouseEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/Ventas.fxml");

    }

    @FXML
    void configuracion(MouseEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/Configuracion.fxml");

    }

    @FXML
    void ayuda(MouseEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/Ayuda.fxml");

    }

    @FXML
    void cerrarSesion(MouseEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");

    }

    @FXML
    void perfil(MouseEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");

    }
    
    @FXML
    void siguiente(ActionEvent event) throws IOException {
        new CambiarVentana(PublicarProductoTercerPasoAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProductoSegundoPaso.fxml");
    }
    
    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }

}
