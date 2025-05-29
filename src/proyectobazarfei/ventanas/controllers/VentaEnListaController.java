package proyectobazarfei.ventanas.controllers;

import javafx.scene.paint.Color;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import proyectobazarfei.system.objects.dao.ProductoDAO;
import proyectobazarfei.system.objects.dao.TransaccionDAO;
import proyectobazarfei.system.objects.daoIMPL.ProductoDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.TransaccionDAOImpl;
import proyectobazarfei.system.objects.vo.ProductoVO;
import proyectobazarfei.system.objects.vo.TransaccionVO;

public class VentaEnListaController {
    
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
    
    public void rellenarDatosTransaccion(TransaccionVO transaccion) {
        ProductoDAO productoDAO = new ProductoDAOImpl();
        ProductoVO producto = productoDAO.obtenerProductoPorId(transaccion.getProducto().getId());

        if (producto != null) {
            tituloProductoLabel.setText(producto.getTitulo());
            precioProductoLabel.setText("$" + producto.getPrecio());
            categoriaProductoLabel.setText(producto.getCategoria());

            if (producto.getPortada() != null) {
                try (InputStream stream = getClass().getResourceAsStream(producto.getPortada())) {
                    if (stream != null) {
                        portadaProductoImageView.setImage(new Image(stream));
                    }
                } catch (Exception e) {
                    System.out.println("Error al cargar portada del producto: " + producto.getPortada());
                }
            }
        }

        fechaYHoraProductoLabel.setText("Fecha y hora de creación: " + transaccion.getFechaCreacion() + " " + transaccion.getHoraCreacion());
        metodoPagoProductoLabel.setText("Método de pago: " + transaccion.getMetodoPago());
        estadoProductoLabel.setText("Estado: " + transaccion.getEstado());
        calificacionProductoLabel.setText("Calificación: " + transaccion.getCalificacion() + "/5");
    }

    @FXML
    void verProducto(ActionEvent event) {

    }
}
