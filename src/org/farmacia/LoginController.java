package org.farmacia;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.farmacia.fxmodel.UsuarioFx;
import org.farmacia.jpacontroller.UsuarioFacade;
import org.farmacia.jpamodel.Usuario;
import org.farmacia.utils.SecurityUtil;

/**
 *
 * @author jgcastillo
 */
public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;
    
    private Stage mainStage;
    private final UsuarioFacade usuarioFacade = new UsuarioFacade();
    private Usuario user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainStage = new Stage();
        mainStage.setTitle("Remedio Entre Todos");
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (checkUser(username, password)) {
            (((Node) event.getSource()).getScene()).getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/org/farmacia/view/Main.fxml"));
            
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.setMaximized(true);
            Image icon = new Image(LoginController.class.getResourceAsStream("/images/icon.png"));
            mainStage.getIcons().add(icon);
            mainStage.show();
        } else {
            messageLabel.setText("Usuario o clave incorrectas");
        }
    }

    @FXML
    private void handleCancel() {
        Platform.exit();
    }

    private boolean checkUser(String usuario, String pws) {
        String encripted = SecurityUtil.getSecurePassword(pws);
        
        Optional<Usuario> optUsuario = Optional.
                ofNullable(usuarioFacade.findByUserAndPsw(usuario, encripted));
        
        if(optUsuario.isPresent()){
            user = optUsuario.get();
            Context.getInstance().getGlobal().put("usuario", user);
            checkPerfil();
        } 

        return optUsuario.isPresent();
    }

    public Usuario getUsuario(){
        return this.user;
    }
    
    private void checkPerfil() {
        String perfil = "";
        
        switch (user.getNivel()) {
            case Usuario.SUPER_ADMIN:
                perfil = UsuarioFx.PERFIL_SUPER;
                break;
            case Usuario.ADMIN:
                perfil = UsuarioFx.PERFIL_ADMIN;
                break;
            case Usuario.USER:
                perfil = UsuarioFx.PERFIL_USER;
                break;
            case Usuario.CONSULTA:
                perfil = UsuarioFx.PERFIL_CONSULTA;
                break;    
        }
        Context.getInstance().getGlobal().put("perfil", perfil);
    }
}
