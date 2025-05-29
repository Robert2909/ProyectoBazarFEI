package proyectobazarfei.ventanas.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.methods.SesionManager;
import proyectobazarfei.system.objects.dao.PerfilUsuarioDAO;
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.PerfilUsuarioDAOImpl;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class ConfiguracionController {
    
    private String rutaImagenSeleccionada = null;

    @FXML
    private Label ayudaLabel;

    @FXML
    private Pane barraLateralPane;

    @FXML
    private Button cambiarApodoButton;

    @FXML
    private TextField cambiarApodoTextField;

    @FXML
    private Button cambiarContrasenaButton;

    @FXML
    private TextField cambiarContrasenaRepeticionTextField;

    @FXML
    private TextField cambiarContrasenaTextField;

    @FXML
    private Button cambiarCorreoButton;

    @FXML
    private TextField cambiarCorreoTextField;

    @FXML
    private Button cambiarDescripcionButton;

    @FXML
    private TextArea cambiarDescripcionTextArea;

    @FXML
    private Button cambiarFotoButton;

    @FXML
    private Button cambiarNombreButton;

    @FXML
    private TextField cambiarNombreTextField;

    @FXML
    private Button cerrarProgramaButton;

    @FXML
    private Label cerrarSesionLabel;

    @FXML
    private Label chatsLabel;

    @FXML
    private Label comprasLabel;

    @FXML
    private AnchorPane configuracionAnchorPane;

    @FXML
    private Label configuracionCuentaLabel;

    @FXML
    private Line configuracionCuentaLine;

    @FXML
    private Label configuracionLabel;

    @FXML
    private Label configuracionPerfilLabel;

    @FXML
    private Line configuracionPerfilLine;

    @FXML
    private Pane contenidoPanel;

    @FXML
    private ImageView fotoPerfilImageView;

    @FXML
    private Pane fotoPerfilPane;

    @FXML
    private Button guardarCambiosButton;

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
    public void initialize() {
        fotoPerfilImageView.setClip(new Circle(50, 50, 50));
        bloquearCampos();

        try {
            UsuarioVO usuario = SesionManager.obtenerUsuarioSesionActiva();
            if (usuario == null) {
                AlertaSistema.error("No hay sesión activa.");
                return;
            }

            cambiarCorreoTextField.setPromptText(usuario.getCorreo());
            cambiarNombreTextField.setPromptText(usuario.getNombre());
            cambiarApodoTextField.setPromptText(usuario.getApodo());

            PerfilUsuarioVO perfil = new PerfilUsuarioDAOImpl().obtenerPerfilPorUsuarioId(usuario.getId());
            if (perfil != null) {
                cambiarDescripcionTextArea.setPromptText(perfil.getDescripcion());
                if (perfil.getFotoPerfil() != null) {
                    try (InputStream stream = getClass().getResourceAsStream(perfil.getFotoPerfil())) {
                        if (stream != null) {
                            fotoPerfilImageView.setImage(new Image(stream));
                        }
                    }
                }
            }

        } catch (Exception e) {
            LogManager.error("Error al cargar datos del perfil: " + e.getMessage());
            AlertaSistema.error("Error al cargar los datos del perfil.");
        }
    }

    private void bloquearCampos() {
        cambiarApodoTextField.setEditable(false);
        cambiarNombreTextField.setEditable(false);
        cambiarDescripcionTextArea.setEditable(false);
        cambiarCorreoTextField.setEditable(false);
        cambiarContrasenaTextField.setEditable(false);
        cambiarContrasenaRepeticionTextField.setEditable(false);
    }

    @FXML
    void cambiarFoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de perfil");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));

        File archivo = fileChooser.showOpenDialog(fotoPerfilImageView.getScene().getWindow());
        if (archivo != null) {
            File destino = new File("src/main/resources/proyectobazarfei/resources/temp/perfiles/" + archivo.getName());
            try {
                java.nio.file.Files.copy(
                    archivo.toPath(),
                    destino.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                rutaImagenSeleccionada = "/proyectobazarfei/resources/temp/perfiles/" + archivo.getName();
                fotoPerfilImageView.setImage(new Image(destino.toURI().toString()));
            } catch (IOException e) {
                LogManager.error("Error al copiar imagen: " + e.getMessage());
                AlertaSistema.error("No se pudo copiar la imagen.");
            }
        }
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        try {
            UsuarioVO usuario = SesionManager.obtenerUsuarioSesionActiva();
            if (usuario == null) {
                AlertaSistema.error("No hay sesión activa.");
                return;
            }

            PerfilUsuarioDAOImpl perfilDAO = new PerfilUsuarioDAOImpl();
            PerfilUsuarioVO perfil = perfilDAO.obtenerPerfilPorUsuarioId(usuario.getId());
            if (perfil == null) {
                AlertaSistema.error("Perfil no encontrado.");
                return;
            }

            String nuevoNombre = obtenerTexto(cambiarNombreTextField);
            String nuevoApodo = obtenerTexto(cambiarApodoTextField);
            String nuevoCorreo = obtenerTexto(cambiarCorreoTextField);
            String nuevaDescripcion = obtenerTexto(cambiarDescripcionTextArea);
            String nuevaContrasena = cambiarContrasenaTextField.getText();
            String repetirContrasena = cambiarContrasenaRepeticionTextField.getText();
            
            if (!nuevoCorreo.endsWith("@estudiantes.uv.mx")) {
                AlertaSistema.error("El correo debe terminar con '@estudiantes.uv.mx'.");
                return;
            }

            if (!nuevaContrasena.isEmpty() && !nuevaContrasena.equals(repetirContrasena)) {
                AlertaSistema.error("Las contraseñas no coinciden.");
                return;
            }

            usuario.setNombre(nuevoNombre);
            usuario.setApodo(nuevoApodo);
            usuario.setCorreo(nuevoCorreo);
            if (!nuevaContrasena.isEmpty()) {
                usuario.setContrasena(nuevaContrasena);
            }

            perfil.setDescripcion(nuevaDescripcion);
            if (rutaImagenSeleccionada != null) {
                perfil.setFotoPerfil(rutaImagenSeleccionada);
            }

            new UsuarioDAOImpl().actualizarUsuario(usuario);
            perfilDAO.actualizarPerfil(perfil);

            LogManager.info("Cambios guardados para usuario ID: " + usuario.getId());
            AlertaSistema.info("Cambios guardados correctamente.");
            bloquearCampos();
            reiniciarCampos();
            new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");

        } catch (Exception e) {
            LogManager.error("Error al guardar cambios: " + e.getMessage());
            AlertaSistema.error("Error inesperado al guardar cambios.");
        }
    }

    private String obtenerTexto(TextInputControl campo) {
        return campo.getText().isBlank() ? campo.getPromptText() : campo.getText();
    }

    private void reiniciarCampos() {
        cambiarNombreTextField.setText("");
        cambiarApodoTextField.setText("");
        cambiarCorreoTextField.setText("");
        cambiarContrasenaTextField.setText("");
        cambiarContrasenaRepeticionTextField.setText("");
        cambiarDescripcionTextArea.setText("");
    }

    @FXML void cambiarNombre(ActionEvent e) {
        habilitarEdicion(cambiarNombreTextField);
    }
    
    @FXML void cambiarApodo(ActionEvent e) {
        habilitarEdicion(cambiarApodoTextField);
    }
    
    @FXML void cambiarCorreo(ActionEvent e) {
        habilitarEdicion(cambiarCorreoTextField);
    }
    
    @FXML void cambiarDescripcion(ActionEvent e) {
        cambiarDescripcionTextArea.setEditable(true);
        cambiarDescripcionTextArea.requestFocus();
    }
    
    @FXML void cambiarContrasena(ActionEvent e) {
        cambiarContrasenaTextField.setEditable(true);
        cambiarContrasenaRepeticionTextField.setEditable(true);
        cambiarContrasenaTextField.requestFocus();
    }

    private void habilitarEdicion(TextField campo) {
        campo.setEditable(true);
        campo.requestFocus();
    }

    @FXML
    void perfil(MouseEvent event) throws IOException {
        new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");
    }

    @FXML
    void menu(MouseEvent event) throws IOException {
        new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }
    
    @FXML
    void publicarProducto(MouseEvent event) throws IOException {
        new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProducto.fxml");
    }

    @FXML
    void productosActivos(MouseEvent event) throws IOException {
        new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/ProductosActivos.fxml");
    }

    @FXML
    void chats(MouseEvent event) throws IOException {
        new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/Chats.fxml");
    }

    @FXML
    void compras(MouseEvent event) throws IOException {
        new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/Compras.fxml");
    }

    @FXML
    void ventas(MouseEvent event) throws IOException {
        new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/Ventas.fxml");
    }

    @FXML
    void configuracion(MouseEvent event) throws IOException {
        new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/Configuracion.fxml");
    }

    @FXML
    void ayuda(MouseEvent event) throws IOException {
        new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/Ayuda.fxml");
    }

    @FXML
    void cerrarSesion(MouseEvent event) throws IOException {
        new CambiarVentana(configuracionAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
