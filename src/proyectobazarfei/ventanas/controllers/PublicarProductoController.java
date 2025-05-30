package proyectobazarfei.ventanas.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.CambiarVentana;
import proyectobazarfei.system.objects.dao.ProductoDAO;
import proyectobazarfei.system.objects.daoIMPL.ProductoDAOImpl;
import proyectobazarfei.system.objects.vo.ProductoVO;

public class PublicarProductoController {
    
    private File portadaSeleccionada;
    
    private final List<File> imagenesSeleccionadas = new ArrayList<>();
    
    private static final List<String> PALABRAS_PROHIBIDAS = List.of(
        // Obscenidades y variaciones
        "puta", "put4", "p4ta", "pu7a", "puto", "put0", "p4to", "pu7o", 
        "mierda", "mi3rda", "m1erda", "m13rd4", "mrd", "mierd@",
        "pendejo", "pndj", "p3ndej0", "pendej0", "pend3j0",
        "cabron", "c4bron", "c4br0n", "cabr0n", "c4br0n",
        "joder", "j0der", "jod3r", "jod3r",
        "cojones", "c0jones", "c0j0nes", "coj0nes",
        "chingar", "ching4r", "ch1ngar", "ch1ng4r",
        "verga", "v3rga", "v3rg4", "verg4", "vrga",
        "pito", "p1to", "p1t0", "pit0", "p170",
        "polla", "p0lla", "p0ll4", "poll4", "p0114",
        "concha", "c0ncha", "c0nch4", "conch4",
        "coño", "c0ño", "coñ0", "c0ñ0", "cñ", "c0n0",
        "rabo", "r4bo", "r4b0", "rab0",
        "culo", "cul0", "c4lo", "c4l0", "kulo", "cvlo", "klo",
        "cagar", "c4gar", "c4g4r", "cag4r", "kagar",
        "mear", "m3ar", "m34r",
        "follar", "f0llar", "f0ll4r", "foll4r",
        "chupar", "chup4r", "chup4", "mamada", "m4mada", "m4m4d4",
        "orto", "0rto", "0rt0", "ort0",
        "ano", "4no", "an0", "4n0",
        "carajo", "c4rajo", "c4r4jo", "caraj0", "c4raj0", "car4jo",
        "marica", "m4rica", "m4r1ca", "mar1ca", "marik", "m4rik",
        "maricon", "maric0n", "m4ricon", "m4ric0n", "m4r1c0n",
        "perra", "p3rra", "p3rr4", "perr4",
        "zorra", "z0rra", "z0rr4", "zorr4",
        "pajero", "pajer0", "p4jero", "p4jer0",

        // Incitación al odio
        "nazi", "n4zi", "naz1", "n4z1", "nazismo", "n4zismo",
        "hitler", "h1tler", "hitl3r", "h1tl3r",
        "fascista", "f4scista", "f4sc1sta", "fasc1sta", "fascism0",
        "racista", "r4cista", "r4c1sta", "rac1sta", "racistx",
        "nigger", "n1gger", "n1gg3r", "nigg3r",
        "negro", "negr0", "n3gro", "n3gr0", // En contexto racista
        "sudaca", "sud4ca", "sud4k4",
        "judiazos", "jud10s", "judi0s", // En contexto antisemita
        "panchito", "p4nchito", "panch1to", "p4nch1to", 
        "terrorista", "terr0rista", "t3rrorista", "t3rr0r1sta",
        "feminazi", "f3minazi", "f3m1nazi", "femin4zi",
        "machito", "m4chito", "mach1to", "m4ch1to",
        "homofobia", "hom0fobia", "homof0bia", "h0mofobia",
        "faggot", "f4ggot", "f4gg0t", "fagg0t",
        "maricón", "maric0n", "m4ricon", "m4ric0n",
        "mongólico", "mong0lico", "mong0l1co", "mongol1co",
        "retrasado", "retr4sado", "retr4s4do", "retrasad0",
        "tarado", "t4rado", "tarad0", "t4r4do",
        "muerte", "mu3rte", "mu3rt3", "muerte a", "mu3rte a", // En contexto de amenaza
        "inmigrante", "inmigr4nte", "1nmigrante", // En contexto xenófobo

        // Drogas
        "cocaina", "coca1na", "c0caina", "coc4ina", "c0c4ina", "coca", "perico", "peri", "polvo",
        "heroina", "her0ina", "h3roina", "hero1na", "h3ro1na", "her01na", "caballo",
        "marihuana", "marihuama", "marihu4na", "m4rihuana", "marihuan4", "mari", "mota", "hierba", "weed", "w33d",
        "porros", "porr0s", "p0rros", "p0rr0s", "joint", "j0int", "peta", "p3ta",
        "metanfetamina", "meth", "m3th", "cristal", "crist4l", "crispy", "ice",
        "lsd", "acido", "acid0", "ac1do", "4cido", "trip", "tr1p", "pepa",
        "mdma", "extasis", "ext4sis", "xtc", "molly", "m0lly",
        "ketamina", "keta", "k3ta", "ket4", "k3t4", "special k",
        "fentanilo", "fenta", "f3nta", "fent4", "china white",
        "crack", "cr4ck", "roca", "r0ca", "piedra", "p13dra",
        "opio", "0pio", "0p10", "op10",
        "morfina", "morf1na", "m0rfina", "m0rf1na",
        "benzos", "b3nzos", "benzodiacepinas", "xanax", "x4nax", "xan4x",
        "valium", "v4lium", "val1um", "v4l1um",
        "codeina", "code1na", "c0deina", "c0de1na",
        "popper", "p0pper", "popp3r", "p0pp3r",
        "ghb", "éxtasis líquido", "éxtasis líquid0",
        "narco", "n4rco", "narcotraficante", "narc0", "n4rc0",

        // Contenido para adultos
        "xxx", "pornhub", "xvideos", "youporn", "redtube", "xnxx",
        "porno", "p0rno", "porn0", "p0rn0", "pornografia", "porn0grafia",
        "hentai", "h3ntai", "hent4i", "h3nt4i",
        "escort", "esc0rt", "3scort", "3sc0rt", "putas", "put4s", "prost1tuta",
        "sexo", "s3xo", "sex0", "s3x0", "s3x", "sexy", "s3xy",
        "vagina", "vag1na", "v4gina", "v4g1na", "vajina", "vaj1na",
        "clitoris", "clít0ris", "clit0ris", "cl1toris", "cl1t0r1s",
        "pene", "p3ne", "pen3", "p3n3", "penis", "p3nis", "pen1s", "p3n1s",
        "tetas", "t3tas", "tet4s", "t3t4s", "pechos", "p3chos", "pech0s",
        "masturbacion", "masturb4cion", "m4sturbacion", "m4sturb4cion",
        "orgasmo", "0rgasmo", "org4smo", "0rg4smo",
        "semen", "sem3n", "s3men", "s3m3n", "esperma", "3sperma", "esp3rma",
        "corrida", "c0rrida", "corrid4", "c0rrid4", "cum", "venirse",
        "anal", "4nal", "an4l", "4n4l", "analsex", "4nalsex",
        "bukkake", "bukk4ke", "bukkak3", "bukk4k3",
        "gangbang", "g4ngbang", "gangb4ng", "g4ngb4ng",
        "sadismo", "s4dismo", "sad1smo", "s4d1smo",
        "masoquismo", "mas0quismo", "m4soquismo", "m4s0quismo",
        "fetiche", "f3tiche", "fetich3", "f3tich3", "fetish", "fet1sh", "f3t1sh",
        "zoofilia", "zo0filia", "z00filia", "z00f1l1a", "bestialidad",
        "incesto", "inc3sto", "1ncesto", "1nc3sto", "incest0", "1ncest0",
        "pedofilia", "ped0filia", "p3dofilia", "p3d0filia", "child porn",
        "cp", "nopor", "n0p0r", "necrofilia", "necr0filia", "n3crofilia",

        // Armas y violencia
        "pistola", "pist0la", "p1stola", "p1st0la", "glock", "gl0ck",
        "revolver", "rev0lver", "rev0lv3r", "revolv3r", "38", "9mm",
        "rifle", "r1fle", "rifl3", "r1fl3", "ak47", "ak-47", "ar15", "ar-15",
        "escopeta", "esc0peta", "3scopeta", "3sc0peta", "shotgun", "sh0tgun",
        "granada", "gran4da", "gr4nada", "gr4n4da", "grenade", "gren4de",
        "bomba", "b0mba", "b0mb4", "bomb4", "explosivo", "expl0sivo",
        "cuchillo", "cuch1llo", "cuchillada", "navaja", "nav4ja", "n4vaja",
        "puñal", "puñalada", "puñ4lada", "puñ4l4da", "apuñalar", "apuñ4lar",
        "asesinar", "4sesinar", "as3sinar", "4s3sinar", "matar", "m4tar",
        "violacion", "vi0lacion", "vi0l4cion", "v10lacion", "v10l4cion",
        "violar", "vi0lar", "vi0l4r", "v10lar", "v10l4r",
        "suicidio", "suicidi0", "su1cidio", "su1cidi0", "suicida", "su1cida",
        "tortura", "t0rtura", "t0rtur4", "tortur4", "masacre", "m4sacre",
        "extorsión", "ext0rsión", "ext0rsi0n", "extorsi0n",
        "sicario", "s1cario", "sicar10", "s1car10", "asesino", "4sesino",
        "tiroteo", "tir0teo", "tir0te0", "tirot3o",
        "acribillar", "4cribillar", "acrib1llar", "4crib1llar",
        "sniper", "sn1per", "francotirador", "franc0tirador"
    );

    @FXML
    private Label ayudaLabel;

    @FXML
    private Pane barraLateralPane;

    @FXML
    private ComboBox<String> categoriaComboBox;

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
    private TextArea descripcionTextArea;

    @FXML
    private CheckBox efectivoCheckBox;

    @FXML
    private Button elegirImagenesButton;

    @FXML
    private Button elegirPortadaButton;

    @FXML
    private FlowPane imagenesProductoNuevoFlowPane;

    @FXML
    private ScrollPane imagenesProductoNuevoScrollPane;

    @FXML
    private Label inicioOpcionLabel;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Label metodosPagoAceptadosLabel;

    @FXML
    private Label perfilLabel;

    @FXML
    private ImageView portadaProductoNuevoImageView;

    @FXML
    private Pane portadaProductoNuevoPane;

    @FXML
    private TextField precioTextField;

    @FXML
    private Button publicarButton;

    @FXML
    private AnchorPane publicarProductoAnchorPane;

    @FXML
    private Label publicarProductoLabel;

    @FXML
    private Label publicarProductoLabel1;

    @FXML
    private Label subtituloLabel;

    @FXML
    private CheckBox tarjetaCheckBox;

    @FXML
    private Label tituloLabel;

    @FXML
    private TextField tituloTextField;

    @FXML
    private CheckBox transferenciaCheckBox;

    @FXML
    private Label ventasLabel;
    
    @FXML
    void initialize() {
        categoriaComboBox.getItems().addAll("Alimentos", "Servicios", "Electrónica", "Accesorios", "Escolar");
    }
    
    @FXML
    private void elegirPortada(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar portada del producto");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(publicarProductoAnchorPane.getScene().getWindow());
        if (file != null) {
            portadaSeleccionada = file;
            Image image = new Image(file.toURI().toString());
            portadaProductoNuevoImageView.setImage(image);
        }
    }
    
    @FXML
    private void elegirImagenes(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imágenes del producto");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(publicarProductoAnchorPane.getScene().getWindow());
        if (files != null) {
            imagenesSeleccionadas.clear();
            imagenesProductoNuevoFlowPane.getChildren().clear();

            for (File file : files) {
                imagenesSeleccionadas.add(file);
                ImageView iv = new ImageView(new Image(file.toURI().toString()));
                iv.setFitWidth(100);
                iv.setFitHeight(100);
                iv.setPreserveRatio(true);
                imagenesProductoNuevoFlowPane.getChildren().add(iv);
            }
        }
    }

    private List<String> obtenerMetodosPagoSeleccionados() {
        List<String> metodos = new ArrayList<>();

        if (efectivoCheckBox.isSelected()) metodos.add("Efectivo");
        if (transferenciaCheckBox.isSelected()) metodos.add("Transferencia");
        if (tarjetaCheckBox.isSelected()) metodos.add("Tarjeta");

        return metodos;
    }

    private ProductoVO extraerProductoSinValidar() {
        // Título y descripción seguros
        String titulo = Optional.ofNullable(tituloTextField.getText()).orElse("").trim();
        String descripcion = Optional.ofNullable(descripcionTextArea.getText()).orElse("").trim();

        // Categoría segura
        String categoria = Optional.ofNullable(categoriaComboBox.getValue()).orElse("").trim();

        // Precio seguro
        double precio = -1; // valor imposible válido
        try {
            String precioStr = Optional.ofNullable(precioTextField.getText()).orElse("").trim();
            if (!precioStr.isEmpty()) {
                precio = Double.parseDouble(precioStr);
            }
        } catch (NumberFormatException e) {
            precio = -1; // marca como inválido
        }


        // Métodos de pago seleccionados
        List<String> metodos = obtenerMetodosPagoSeleccionados();

        // Imágenes adicionales
        List<String> imagenes = new ArrayList<>();
        if (imagenesSeleccionadas != null) {
            for (File img : imagenesSeleccionadas) {
                if (img != null) {
                    imagenes.add(img.getAbsolutePath());
                }
            }
        }

        // Portada segura
        String portada = (portadaSeleccionada != null) ? portadaSeleccionada.getAbsolutePath() : null;

        // Construir objeto VO
        return new ProductoVO(
            0, // ID lo asigna Oracle
            titulo,
            descripcion,
            precio,
            categoria,
            imagenes,
            portada,
            metodos
        );
    }


    private boolean validarProducto(ProductoVO producto) {
        if (producto == null) {
            AlertaSistema.error("Error interno: el producto es nulo.");
            return false;
        }

        // Título
        String titulo = "";
        try {
            titulo = Optional.ofNullable(producto.getTitulo()).orElse("").trim();
            if (titulo.isEmpty()) {
                AlertaSistema.advertencia("El título no puede estar vacío.");
                return false;
            }
        } catch (Exception e) {
            AlertaSistema.error("Error al leer el título del producto.");
            return false;
        }

        // Descripción
        String descripcion = "";
        try {
            descripcion = Optional.ofNullable(producto.getDescripcion()).orElse("").trim();
            if (descripcion.isEmpty()) {
                AlertaSistema.advertencia("La descripción no puede estar vacía.");
                return false;
            }
        } catch (Exception e) {
            AlertaSistema.error("Error al leer la descripción del producto.");
            return false;
        }

        // Categoría
        String categoria = "";
        try {
            categoria = Optional.ofNullable(producto.getCategoria()).orElse("").trim();
            if (categoria.isEmpty() || categoria.equalsIgnoreCase("categoría")) {
                AlertaSistema.advertencia("Debes seleccionar una categoría válida.");
                return false;
            }
        } catch (Exception e) {
            AlertaSistema.error("Error al leer la categoría del producto.");
            return false;
        }

        // Portada
        String portada = "";
        try {
            portada = Optional.ofNullable(producto.getPortada()).orElse("").trim();
            if (portada.isEmpty()) {
                AlertaSistema.advertencia("Debes seleccionar una imagen de portada.");
                return false;
            }
        } catch (Exception e) {
            AlertaSistema.error("Error al leer la imagen de portada.");
            return false;
        }

        // Métodos de pago
        List<String> metodos = null;
        try {
            metodos = producto.getMetodosPagoAceptados();
            if (metodos == null || metodos.isEmpty()) {
                AlertaSistema.advertencia("Debes seleccionar al menos un método de pago.");
                return false;
            }
        } catch (Exception e) {
            AlertaSistema.error("Error al leer los métodos de pago aceptados.");
            return false;
        }

        // Precio
        try {
            double precio = producto.getPrecio();
            if (!Double.isFinite(precio) || precio <= 0) {
                AlertaSistema.advertencia("El precio debe ser un número positivo válido.");
                return false;
            }
        } catch (Exception e) {
            AlertaSistema.error("Error al procesar el precio del producto.");
            return false;
        }

        // Palabras prohibidas (normalización del texto completo)
        try {
            String textoTotal = (titulo + " " + descripcion).toLowerCase().replaceAll("[^\\p{L}\\p{Nd}\\s]", "");
            for (String palabra : PALABRAS_PROHIBIDAS) {
                if (textoTotal.contains(palabra.toLowerCase().trim())) {
                    AlertaSistema.advertencia("El contenido contiene palabras inapropiadas: " + palabra);
                    return false;
                }
            }
        } catch (Exception e) {
            AlertaSistema.error("Error durante la validación del lenguaje del producto.");
            return false;
        }

        return true;
    }

    @FXML
    private void publicar() {
        ProductoVO producto = extraerProductoSinValidar();

        if (producto == null) {
            return;
        }

        if (!validarProducto(producto)) {
            return;
        }

        try {
            ProductoDAO productoDAO = new ProductoDAOImpl();
            boolean creadoCorrectamente = productoDAO.crearProducto(producto);

            if (creadoCorrectamente) {
                AlertaSistema.info("Producto publicado con éxito.");
                new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProductoSegundoPaso.fxml");
            } else {
                AlertaSistema.error("No se pudo publicar el producto. Revisa los datos ingresados.");
            }

        } catch (Exception e) {
            AlertaSistema.error("Ocurrió un error al intentar publicar el producto. Intenta nuevamente.");
            e.printStackTrace();
        }
    }

    @FXML
    void perfil(MouseEvent event) throws IOException {
        new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/PerfilPropio.fxml");
    }

    @FXML
    void menu(MouseEvent event) throws IOException {
        new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/Menu.fxml");
    }
    
    @FXML
    void publicarProducto(MouseEvent event) throws IOException {
        new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/PublicarProducto.fxml");
    }

    @FXML
    void productosActivos(MouseEvent event) throws IOException {
        new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/ProductosActivos.fxml");
    }

    @FXML
    void chats(MouseEvent event) throws IOException {
        new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/Chats.fxml");
    }

    @FXML
    void compras(MouseEvent event) throws IOException {
        new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/Compras.fxml");
    }

    @FXML
    void ventas(MouseEvent event) throws IOException {
        new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/Ventas.fxml");
    }

    @FXML
    void configuracion(MouseEvent event) throws IOException {
        new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/Configuracion.fxml");
    }

    @FXML
    void ayuda(MouseEvent event) throws IOException {
        new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/Ayuda.fxml");
    }

    @FXML
    void cerrarSesion(MouseEvent event) throws IOException {
        new CambiarVentana(publicarProductoAnchorPane, "/proyectobazarfei/ventanas/fxml/Login.fxml");
    }

    @FXML
    void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }
}
