package proyectobazarfei.ventanas.controllers;

import java.io.File;
import javafx.scene.paint.Color;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.objects.dao.ProductoDAO;
import proyectobazarfei.system.objects.dao.TransaccionDAO;
import proyectobazarfei.system.objects.daoIMPL.ProductoDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.TransaccionDAOImpl;
import proyectobazarfei.system.objects.vo.ProductoVO;
import proyectobazarfei.system.objects.vo.TransaccionVO;

public class VentaEnListaController {
    
    private ProductoVO producto;
    
    private AnchorPane anchorRaizGlobal;
    
    @FXML
    private AnchorPane ventaEnListaAnchorPane;

    @FXML
    private Label calificacionProductoLabel;

    @FXML
    private Label categoriaProductoLabel;

    @FXML
    private Label estadoProductoLabel;

    @FXML
    private Label fechaYHoraProductoLabel;

    @FXML
    private Label metodoPagoProductoLabel;

    @FXML
    private ImageView portadaProductoImageView;

    @FXML
    private Label precioProductoLabel;

    @FXML
    private Label tituloProductoLabel;

    @FXML
    private Button verProductoButton;
    
    public void rellenarDatosTransaccion(TransaccionVO transaccion, AnchorPane anchorRaiz) {
        ProductoDAO productoDAO = new ProductoDAOImpl();
        this.producto = productoDAO.obtenerProductoPorId(transaccion.getProducto().getId());
        this.anchorRaizGlobal = anchorRaiz;

        if (producto != null) {
            tituloProductoLabel.setText(producto.getTitulo());
            precioProductoLabel.setText("$" + producto.getPrecio());
            categoriaProductoLabel.setText(producto.getCategoria());

            if (producto.getPortada() != null && !producto.getPortada().isBlank()) {
                File archivo = new File(producto.getPortada());

                if (archivo.exists()) {
                    LogManager.debug("Portada encontrada correctamente: " + archivo.getAbsolutePath());
                    portadaProductoImageView.setImage(new Image(archivo.toURI().toString()));
                } else {
                    LogManager.error("No se encontró la portada del producto: " + archivo.getAbsolutePath());
                }
            }
        }

        fechaYHoraProductoLabel.setText("Fecha y hora de creación: " + transaccion.getFechaCreacion() + " " + transaccion.getHoraCreacion());
        metodoPagoProductoLabel.setText("Método de pago: " + transaccion.getMetodoPagoElegido());
        estadoProductoLabel.setText("Estado: " + transaccion.getEstado());
        calificacionProductoLabel.setText("Calificación: " + transaccion.getCalificacion() + "/5");
    }

    @FXML
    void verProducto(ActionEvent event) {
        LogManager.info("Producto abierto: " + producto.getTitulo());
        new CambiarVentana(anchorRaizGlobal, "/proyectobazarfei/ventanas/fxml/Producto.fxml", producto);
    }
}