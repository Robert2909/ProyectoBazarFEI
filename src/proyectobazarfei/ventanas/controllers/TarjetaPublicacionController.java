package proyectobazarfei.ventanas.controllers;

import java.io.InputStream;
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

    private ProductoVO productoAsociado;

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

        if (producto.getPortada() != null) {
            String ruta = producto.getPortada().startsWith("/") 
                          ? producto.getPortada() 
                          : "/" + producto.getPortada();

            try (InputStream imgStream = getClass().getResourceAsStream(ruta)) {
                if (imgStream != null) {
                    Image imagen = new Image(imgStream);
                    imagenImageView.setImage(imagen);
                } else {
                    LogManager.error("No se encontró la portada: " + ruta);
                }
            } catch (Exception e) {
                LogManager.error("Error al cargar portada: " + ruta);
                e.printStackTrace();
            }
        }
    }
}
