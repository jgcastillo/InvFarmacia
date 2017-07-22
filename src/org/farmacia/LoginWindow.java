package org.farmacia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.farmacia.initialdata.InitialData;
import org.farmacia.jpacontroller.UsuarioFacade;
import org.farmacia.jpamodel.Usuario;

/**
 * Clase de ingreso a la aplicación, valida los usuarios y su perfil
 * @author jgcastillo
 */
public class LoginWindow extends Application {
    
    private Scene scene;
    private Stage loginStage;
    private Stage mainStage;
    
    private final UsuarioFacade usuarioFacade = new UsuarioFacade();
    private final InitialData initialData = new InitialData();
    
    @Override
    public void init() {
        if(!checkInitialData()){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "No existe conexión a la base de datos, verfique",
                    ButtonType.CLOSE);
            alert.showAndWait();
        }
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        this.loginStage = stage;
        
        Parent root = FXMLLoader.load(getClass().getResource("view/Login.fxml"));
        
        scene = new Scene(root);
        
        loginStage.initStyle(StageStyle.UNDECORATED);
        Image icon = new Image(LoginWindow.class.getResourceAsStream("/images/icon.png"));
        loginStage.getIcons().add(icon);
        loginStage.setScene(scene);
        loginStage.show();
    }
    
    public Stage getMainStage(){
        return this.mainStage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private boolean checkInitialData() {
        boolean result = false;
        Optional<List<Usuario>> optUsuarios = Optional.ofNullable(usuarioFacade.findAll());
        if (optUsuarios.isPresent()){
            if(optUsuarios.get().isEmpty()){ 
                initialData.loadInitialData();
            }
            result = true;
        }
        return result;
    }
    
}
