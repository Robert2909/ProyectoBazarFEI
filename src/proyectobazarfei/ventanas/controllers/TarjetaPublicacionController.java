package proyectobazarfei.ventanas.controllers;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.objects.vo.ProductoVO;

public class TarjetaPublicacionController {
    
    private AnchorPane anchorRaizGlobal;
    
    private ProductoVO productoAsociado;

    @FXML
    private AnchorPane TarjetaPublicacionAnchorPane;

    @FXML
    private Label categoriaLabel;

    @FXML
    private Pane datosPane;

    @FXML
    private ImageView imagenImageView;

    @FXML
    private StackPane imagenStackPane;

    @FXML
    private Label precioLabel;

    @FXML
    private Label tituloLabel;


    @FXML
    void abrirProducto(MouseEvent event) {
        LogManager.info("Producto abierto: " + productoAsociado.getTitulo());
        new CambiarVentana(anchorRaizGlobal, "/proyectobazarfei/ventanas/fxml/Producto.fxml", productoAsociado);
    }

    public void rellenarDatosPublicacion(ProductoVO producto, AnchorPane anchorRaiz) {
        this.productoAsociado = producto;
        this.anchorRaizGlobal = anchorRaiz;

        tituloLabel.setText(producto.getTitulo());
        precioLabel.setText("$" + producto.getPrecio());
        categoriaLabel.setText(producto.getCategoria());

        if (producto.getPortada() != null && !producto.getPortada().isBlank()) {
            File archivoPortada = new File(producto.getPortada());

            if (archivoPortada.exists()) {
                LogManager.debug("[VERIFICACIÓN EXITOSA] Portada encontrada en: " + archivoPortada.getAbsolutePath());
                Image imagen = new Image(archivoPortada.toURI().toString());
                imagenImageView.setImage(imagen);
            } else {
                LogManager.error("[ERROR] No se encontró la portada: " + archivoPortada.getAbsolutePath());
            }
        } else {
            LogManager.error("[ERROR] Ruta de portada vacía o nula.");
        }
    }

}
