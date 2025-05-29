package proyectobazarfei;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import proyectobazarfei.system.methods.SesionManager;
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.daoIMPL.UsuarioDAOImpl;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class Main extends Application{
    
    public Main() {
        
    }
    
    public void start(Stage primaryStage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("ventanas/fxml/Login.fxml"));

        if (SesionManager.haySesionRecordada()) {
            String correoGuardado = SesionManager.obtenerSesionGuardada();
            UsuarioVO usuario = SesionManager.obtenerUsuarioSesionActiva();
            if (usuario == null) {
                SesionManager.olvidarSesionGuardada();
            } else {
                root = FXMLLoader.load(getClass().getResource("ventanas/fxml/Menu.fxml"));
            }
        }
        
        Scene scene = new Scene(root);

        primaryStage.setTitle("Bazar FEI");
        primaryStage.initStyle(StageStyle.UNDECORATED);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main (String[] args){
         launch(args);
    }
}
