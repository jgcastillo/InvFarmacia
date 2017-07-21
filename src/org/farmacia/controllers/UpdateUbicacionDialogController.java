package org.farmacia.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;
import org.farmacia.utils.MainViewData;

/**
 * FXML Controller class
 *
 * @author jgcastillo
 */
public class UpdateUbicacionDialogController implements Initializable {

    @FXML
    private TextField medicamentoTextField;
    @FXML
    private TextField ubicacionTextField;
    
    private final MiddlewareController middleware;
    private final MainViewData mainViewData;
    
    public UpdateUbicacionDialogController(){
        this.middleware = new MiddlewareController();
        this.mainViewData = new MainViewData();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadInitialData();
    }    
    
    @FXML
    private void handleCancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }
    
    @FXML
    private void handleGuardar(ActionEvent event){
        System.out.println("Va a guardar la ubicación");
    }
    
    private void loadInitialData() {
        String[] mediNames = mainViewData.loadInitialMedicaments();
//        String[] presentacionNames = mainViewData.loadInitialPresentaciones();
//        String[] usoNames = mainViewData.loadInitialUsos();
//        String[] componentesNames = mainViewData.loadInitialComponentes();
        
        TextFields.bindAutoCompletion(medicamentoTextField, mediNames);
//        TextFields.bindAutoCompletion(presentacionTextField, presentacionNames);
    }
    
}
