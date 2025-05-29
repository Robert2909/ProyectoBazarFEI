package proyectobazarfei.ventanas.controllers;

import java.io.IOException;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.methods.SesionManager;
import proyectobazarfei.system.objects.dao.PerfilUsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.PerfilUsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;

public class PerfilFavoritoListaController {
    
    private PerfilUsuarioVO perfil;
    
    public AnchorPane anchorRaiz;

    @FXML
    private Label apodoLabel;

    @FXML
    private Label calificacionLabel;

    @FXML
    private ImageView imagenPerfilImageView;

    @FXML
    private Pane imagenPerfilPane;

    @FXML
    private Label nombreLabel;

    @FXML
    private AnchorPane perfilFavoritoListaAnchorPane;

    @FXML
    private Button verPerfilButton;
    
    public void rellenarDatos(PerfilUsuarioVO perfil, AnchorPane anchorRaiz) {

        this.perfil = perfil;
        this.anchorRaiz = anchorRaiz;

        if (perfil == null || perfil.getDatosUsuario() == null) {
            LogManager.error("No se pudo rellenar el perfil favorito: datos incompletos.");
            return;
        }

        nombreLabel.setText(perfil.getDatosUsuario().getNombre());
        apodoLabel.setText(perfil.getDatosUsuario().getApodo());
        calificacionLabel.setText("Calificación: " + perfil.getCalificacionPromedio() + "/5");

        if (perfil.getFotoPerfil() != null && !perfil.getFotoPerfil().isEmpty()) {
            try (InputStream imagenStream = getClass().getResourceAsStream(perfil.getFotoPerfil())) {
                if (imagenStream != null) {
                    Image imagen = new Image(imagenStream);
                    imagenPerfilImageView.setClip(new Circle(150, 150, 150));
                    imagenPerfilImageView.setImage(imagen);
                }
            } catch (Exception e) {
                LogManager.error("No se pudo cargar la imagen de perfil: " + perfil.getFotoPerfil());
            }
        }
    }
    
    @FXML
    void verPerfil(ActionEvent event) {
        new CambiarVentana(anchorRaiz, "/proyectobazarfei/ventanas/fxml/PerfilAjeno.fxml", this.perfil);
    }

    @FXML
    void eliminarDeFavoritos(ActionEvent event) {
        LogManager.debug("Eliminando perfil de la lista de favoritos: " + perfil.getId());
        PerfilUsuarioDAO perfilUsuarioDAO = new PerfilUsuarioDAOImpl();
        PerfilUsuarioVO perfilActual = perfilUsuarioDAO.obtenerPerfilPorUsuarioId(SesionManager.obtenerUsuarioSesionActiva().getId());
        perfilUsuarioDAO.eliminarVendedorFavorito(perfilActual.getId(), perfil.getId());
        new CambiarVentana(anchorRaiz, "/proyectobazarfei/ventanas/fxml/PerfilesFavoritos.fxml");
    }
}
