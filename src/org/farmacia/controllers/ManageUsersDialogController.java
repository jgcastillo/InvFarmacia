package org.farmacia.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.farmacia.jpamodel.Usuario;
import org.farmacia.fxmodel.UsuarioFx;

public class ManageUsersDialogController implements Initializable {

    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField apellidoTextField;
    @FXML
    private TextField usuarioTextField;
    @FXML
    private TextField claveTextField;
    @FXML
    private ComboBox perfilCombo;
    @FXML
    private Button agregarButton;
    @FXML
    private Button editarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private TableView<UsuarioFx> dataTable;
    @FXML
    private TableColumn<UsuarioFx, String> apellidoColumn;
    @FXML
    private TableColumn<UsuarioFx, String> nombreColumn;
    @FXML
    private TableColumn<UsuarioFx, String> usuarioColumn;
    @FXML
    private TableColumn<UsuarioFx, String> perfilColumn;

    private UsuarioFx usuarioFx;
    private boolean nombreChange = false;
    private boolean apellidoChange = false;
    private boolean claveChange = false;
    private boolean usuarioChange = false;
    private boolean perfilChange = false;

    //acceso al middleware
    private final MiddlewareController middlewareController;

    public ManageUsersDialogController() {
        this.middlewareController = new MiddlewareController();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillPerfilCombo();
        fillDataTable();

        showUsuarioDetails(null);
        dataTable.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showUsuarioDetails(newValue)));
        
        agregarButton.setDisable(true);
        editarButton.setDisable(true);
        eliminarButton.setDisable(true);
        
        addTextFieldListeners();
    }

    private void fillPerfilCombo() {
        perfilCombo.setPromptText("Seleccione...");
        perfilCombo.getItems().addAll(
                "Consulta",
                "Usuario",
                "Administrador"
        );
    }

    private void clearFields() {
        nombreTextField.clear();
        apellidoTextField.clear();
        usuarioTextField.clear();
        claveTextField.clear();
        if (perfilCombo.getValue() != null
                && !perfilCombo.getValue().toString().isEmpty()) {
            perfilCombo.setValue(null);
        }
    }

    @FXML
    public void handleAgregar(ActionEvent event) {
        usuarioFx = new UsuarioFx();
        usuarioFx.setId(0);
        usuarioFx.setNombre(nombreTextField.getText());
        usuarioFx.setApellido(apellidoTextField.getText());
        usuarioFx.setUsuario(usuarioTextField.getText());
        usuarioFx.setClave(claveTextField.getText());
        usuarioFx.setPerfil(perfilCombo.getValue().toString());
  
        middlewareController.saveUser(usuarioFx);
        fillDataTable();
        clearFields();
        agregarButton.setDisable(true);
        usuarioFx = null;
    }

    @FXML
    public void handleEditar(ActionEvent event) {
        usuarioFx.setApellido(apellidoTextField.getText());
        usuarioFx.setNombre(nombreTextField.getText());
        usuarioFx.setUsuario(usuarioTextField.getText());
        if (claveTextField.getText() != null && !claveTextField.getText().isEmpty()) {
            usuarioFx.setClave(claveTextField.getText());
        } 
        usuarioFx.setPerfil(perfilCombo.getValue().toString());

        middlewareController.edit(usuarioFx);
        usuarioFx = null;
        fillDataTable();
        clearFields();
        editarButton.setDisable(true);
        eliminarButton.setDisable(true);
    }

    @FXML
    public void handleEliminar(ActionEvent event) {
        middlewareController.deleteUser(usuarioFx);
        usuarioFx = null;
        fillDataTable();
        clearFields();
        editarButton.setDisable(true);
        eliminarButton.setDisable(true);
    }

    @FXML
    public void handleSalir(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    public void showUsuarioDetails(UsuarioFx user) {
        if (user != null) {
            usuarioFx = user;

            nombreTextField.setText(user.getNombre());
            apellidoTextField.setText(user.getApellido());
            usuarioTextField.setText(user.getUsuario());
            perfilCombo.setValue(user.getPerfil());

            editarButton.setDisable(false);
            eliminarButton.setDisable(false);
            
        } else {
            clearFields();
        }
    }

    public void fillDataTable() {
        List<Usuario> usuarios = middlewareController.getUsuarios();

        ObservableList<UsuarioFx> usuariosFX = FXCollections.observableArrayList();
        usuarios.forEach(user -> {

            String perfil;
            switch (user.getNivel()) {
                case Usuario.SUPER_ADMIN:
                    perfil = "SuperAdmin";
                    break;
                case Usuario.ADMIN:
                    perfil = "Administrador";
                    break;
                case Usuario.USER:
                    perfil = "Usuario";
                    break;
                default:
                    perfil = "Consulta";
                    break;
            }

            UsuarioFx usr = new UsuarioFx(user.getId(), user.getNombre(),
                    user.getApellido(), user.getUsername(), perfil);
            usr.setClave(user.getPassword());
            if (user.getNivel() != 0) {
                usuariosFX.add(usr);
            }
        });

        dataTable.setItems(usuariosFX);
        apellidoColumn.setCellValueFactory(cellData
                -> cellData.getValue().apellidoProperty());
        nombreColumn.setCellValueFactory(cellData
                -> cellData.getValue().nombreProperty());
        usuarioColumn.setCellValueFactory(cellData
                -> cellData.getValue().usuarioProperty());
        perfilColumn.setCellValueFactory(cellData
                -> cellData.getValue().perfilProperty());
    }
    
    private void addTextFieldListeners(){
        nombreTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            nombreChange = true;
            checkDataFields();
        });
        
        apellidoTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            apellidoChange = true;
            checkDataFields();
        });
        
        usuarioTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            usuarioChange = true;
            checkDataFields();
        });
        
        claveTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            claveChange = true;
            checkDataFields();
        });
        
        perfilCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            perfilChange = true;
            checkDataFields();
        });
    }
    
    private void checkDataFields(){
        if(nombreChange && apellidoChange && usuarioChange && claveChange && perfilChange){
            if((nombreTextField.getText() != null && !nombreTextField.getText().isEmpty())
                    && (apellidoTextField.getText() != null && !apellidoTextField.getText().isEmpty())
                    && (usuarioTextField.getText() != null && !usuarioTextField.getText().isEmpty())
                    && (claveTextField.getText() != null && !claveTextField.getText().isEmpty())
                    && (perfilCombo.getValue() != null)){
                agregarButton.setDisable(false);
            } else {
                agregarButton.setDisable(true);
            }            
        } else {
            agregarButton.setDisable(true);
        }
    }
}
