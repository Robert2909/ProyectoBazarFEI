package proyectobazarfei.ventanas.controllers;

import java.text.SimpleDateFormat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import proyectobazarfei.system.objects.vo.MensajeVO;

public class ChatMensajeEnviadoController {

    @FXML
    private AnchorPane chatMensajeEnviadoAnchorPane;

    @FXML
    private VBox chatMensajeVBox;

    @FXML
    private HBox fechaHoraHBox;

    @FXML
    private Label fechaHoraLabel;

    @FXML
    private Label mensajeLabel;
    
    public void setMensaje(MensajeVO mensaje) {
        mensajeLabel.setText(mensaje.getTexto());

        String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy").format(mensaje.getFechaEnvio());
        String horaFormateada = mensaje.getHoraEnvio();

        fechaHoraLabel.setText(horaFormateada + " - " + fechaFormateada);
    }


}
