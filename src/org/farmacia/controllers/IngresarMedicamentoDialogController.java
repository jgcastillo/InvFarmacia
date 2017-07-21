package org.farmacia.controllers;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import org.farmacia.MainController;
import org.farmacia.fxmodel.MedicamentoFx;
import org.farmacia.utils.MainViewData;

/**
 * Clase Controller del dialogo de ingreso de medicamentos a la base de datos
 *
 * @author jgcastillo
 */
public class IngresarMedicamentoDialogController implements Initializable {

    @FXML
    private TextField buscarNombreTextField;
    @FXML
    private Button oldBuscarButton;
    @FXML
    private TableView<MedicamentoFx> oldTableView;
    
    @FXML
    private Label idLabel;
    @FXML
    private TextField nombreComercialTextField;
    @FXML
    private TextField presentacionTextField;
    @FXML
    private TextField ubicacionTextField;
    @FXML
    private TextField cantidadTextField;
    @FXML
    private TextField componenteTextField;
    @FXML
    private ListView<String> componentesListView;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button agregarCompButton;
    @FXML
    private Button eliminarUsoButton;
    @FXML
    private Button agregarUsoButton;
    @FXML
    private Button eliminarEquivButton;
    @FXML
    private Button agregarEquivButton;
    @FXML
    private TextField usoTextField;
    @FXML
    private ListView<String> usosListView;
    @FXML
    private TextField equivalenteTextField;
    @FXML
    private ListView<String> equivalentesListView;

    private final MainViewData mainViewData;
    private final MiddlewareController middleware;
    //private ObservableList<String> componentsList;

    public IngresarMedicamentoDialogController() {
        this.mainViewData = new MainViewData();
        this.middleware = new MiddlewareController();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadInitialData();
        idLabel.setVisible(false);
        eliminarButton.setDisable(true);
        eliminarUsoButton.setDisable(true);
        eliminarEquivButton.setDisable(true);
        componentesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        usosListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        equivalentesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        oldTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> fillIngresoMedicamentoData(newValue));
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        (((Node) event.getSource()).getScene()).getWindow().hide();
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        ObservableList<String> componentsList = componentesListView.getItems();
        ObservableList<String> usosList = usosListView.getItems();
        ObservableList<String> equivsList = equivalentesListView.getItems();

        Optional<MedicamentoFx> optMedFx = Optional.empty();
        MedicamentoFx medicamentoFx;
        try {
             Integer medicamentoId = Integer.parseInt(idLabel.getText());
             optMedFx = middleware.findMedicamentoById(medicamentoId);
        } catch (NumberFormatException e) {
        }
        
        System.out.println("");
        
        if(optMedFx.isPresent()){
            // ya existe, sólo agrega al inventario
            medicamentoFx = optMedFx.get();
            medicamentoFx.setCantidad(getCantidad());
            medicamentoFx.setUbicacion(getUbicacion());
            middleware.updateInventario(medicamentoFx);
            
            MainController.showDialogInformation(Alert.AlertType.INFORMATION,
                    "Ingreso a Inventario", "Ingreso a Inventario",
                    "El inventario del medicamento " + medicamentoFx.getNombre()
                    + " se ha actualizado con éxito.");
            disableAgregarButtons(false);
        } else {
            // no existe hay que crearlo
            medicamentoFx = new MedicamentoFx();
            medicamentoFx.setNombre(nombreComercialTextField.getText());
            medicamentoFx.setUbicacion(ubicacionTextField.getText());
            medicamentoFx.setPresentacion(presentacionTextField.getText());
            medicamentoFx.setCantidad(getCantidad());
            medicamentoFx.setUbicacion(getUbicacion());
            medicamentoFx.setComponentes(componentsList);
            medicamentoFx.setEquivalentes(equivsList);
            medicamentoFx.setUsos(usosList);
            
            middleware.saveMedicamento(medicamentoFx);
            MainController.showDialogInformation(Alert.AlertType.INFORMATION,
                    "Ingreso a Inventario", "Ingreso a Inventario",
                    "El medicamento " + medicamentoFx.getNombre()
                    + " ha ingresado con éxito al inventario.");
        }
        clearFields();
    }

    private Integer getCantidad() {
        Integer cantidad = 0;
        try {
            if (cantidadTextField.getText() != null && !cantidadTextField.getText().isEmpty()) {
                cantidad = Integer.valueOf(cantidadTextField.getText());
            }
        } catch (NumberFormatException e) {
            MainController.showDialogInformation(Alert.AlertType.WARNING, "Advertencia", null,
                    "Debe colocar sólo números en este campo");
        }
        return cantidad;
    }
    
    private String getUbicacion(){
        String ubicacion = "";
        if(ubicacionTextField.getText() != null && !ubicacionTextField.getText().isEmpty()){
            ubicacion = ubicacionTextField.getText();
        }
        return ubicacion;
    }

    @FXML
    private void handleAgregarComponente(ActionEvent event) {
        componentesListView.getItems().add(componenteTextField.getText());
        componenteTextField.clear();
        if (eliminarButton.isDisable()) {
            eliminarButton.setDisable(false);
        }
    }
    
    @FXML
    private void handleEliminarComponente(ActionEvent event) {
        ObservableList<String> selectedComponents;
        selectedComponents = componentesListView.getSelectionModel().getSelectedItems();
        selectedComponents.forEach((sel) -> {
            componentesListView.getItems().remove(sel);
        });
        if (componentesListView.getItems().isEmpty()) {
            eliminarButton.setDisable(true);
        }

    }

    @FXML
    private void handleAgregarUso(ActionEvent event) {
        usosListView.getItems().add(usoTextField.getText());
        usoTextField.clear();
        if (eliminarUsoButton.isDisable()) {
            eliminarUsoButton.setDisable(false);
        }
    }

    @FXML
    private void handleEliminarUso(ActionEvent event) {
        ObservableList<String> selectedUsos;
        selectedUsos = usosListView.getSelectionModel().getSelectedItems();
        selectedUsos.forEach((sel) -> {
            usosListView.getItems().remove(sel);
        });
        if (usosListView.getItems().isEmpty()) {
            eliminarUsoButton.setDisable(true);
        }
    }
    
    @FXML
    private void handleAgregarEquivalente(ActionEvent event) {
        equivalentesListView.getItems().add(equivalenteTextField.getText());
        equivalenteTextField.clear();
        if (eliminarEquivButton.isDisable()) {
            eliminarEquivButton.setDisable(false);
        }
    }

    @FXML
    private void handleEliminarEquivalente(ActionEvent event) {
        ObservableList<String> selectedEquiv;
        selectedEquiv = equivalentesListView.getSelectionModel().getSelectedItems();
        selectedEquiv.forEach((sel) -> {
            equivalentesListView.getItems().remove(sel);
        });
        if (equivalentesListView.getItems().isEmpty()) {
            eliminarEquivButton.setDisable(true);
        }
    }

    private void loadInitialData() {
        String[] mediNames = mainViewData.loadInitialMedicaments();
        String[] presentacionNames = mainViewData.loadInitialPresentaciones();
        String[] usoNames = mainViewData.loadInitialUsos();
        String[] componentesNames = mainViewData.loadInitialComponentes();
        String[] equivsNames = mainViewData.loadInitialEquivalentes();
        TextFields.bindAutoCompletion(buscarNombreTextField, mediNames);
        TextFields.bindAutoCompletion(presentacionTextField, presentacionNames);
        TextFields.bindAutoCompletion(usoTextField, usoNames);
        TextFields.bindAutoCompletion(componenteTextField, componentesNames);
        TextFields.bindAutoCompletion(equivalenteTextField, equivsNames);
    }

    @FXML
    private void handleBuscarButton(ActionEvent event) {
        String nombreMed = buscarNombreTextField.getText();
        ObservableList<MedicamentoFx> medicamentosList = middleware.buscarByNombre(nombreMed);
        medicamentosList.sort((MedicamentoFx m1, MedicamentoFx m2) -> {
            return m1.getNombre().compareTo(m2.getNombre());
        });
        
        oldTableView.setItems(medicamentosList);
        TableColumn idCol = new TableColumn("id");
        idCol.setPrefWidth(0);
        idCol.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn nombreCol = new TableColumn("Medicamento");
        nombreCol.setPrefWidth(127);
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn componentesCol = new TableColumn("Componentes");
        componentesCol.setPrefWidth(344);
        componentesCol.setCellValueFactory(new PropertyValueFactory<>("componentes"));
        TableColumn cantCol = new TableColumn("Cant");
        cantCol.setPrefWidth(51);
        cantCol.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        oldTableView.getColumns().addAll(nombreCol, componentesCol, cantCol);
    }
    
    private void fillIngresoMedicamentoData(MedicamentoFx medFx){
        idLabel.setText(String.valueOf(medFx.getId()));
        nombreComercialTextField.setText(medFx.getNombre());
        presentacionTextField.setText(medFx.getPresentacion());
        ubicacionTextField.setText(medFx.getUbicacion());
        cantidadTextField.setText(String.valueOf(0));
        Optional<List<String>> optUsos = Optional.ofNullable(medFx.getUsos());
        if(optUsos.isPresent()){
            medFx.getUsos().forEach(uso -> usosListView.getItems().add(uso));
        }
        Optional<List<String>> optComps = Optional.ofNullable(medFx.getComponentes());
        if(optComps.isPresent()){
            medFx.getComponentes().forEach(comp -> componentesListView.getItems().add(comp));
        }
        Optional<List<String>> optEquivs = Optional.ofNullable(medFx.getEquivalentes());
        if (optEquivs.isPresent()) {
            medFx.getEquivalentes().forEach(equiv -> equivalentesListView.getItems().add(equiv));
        }
        disableAgregarButtons(true);
        cantidadTextField.requestFocus();
    }

    private void clearFields() {
        nombreComercialTextField.clear();
        presentacionTextField.clear();
        usoTextField.clear();
        cantidadTextField.clear();
        ubicacionTextField.clear();
        buscarNombreTextField.clear();
        oldTableView.getItems().clear();
        idLabel.setText("");
        componenteTextField.clear();
        componentesListView.getItems().clear();
        usoTextField.clear();
        usosListView.getItems().clear();
        equivalenteTextField.clear();
        equivalentesListView.getItems().clear();
    }

    private void disableAgregarButtons(Boolean disable){
        agregarCompButton.setDisable(disable);
        agregarEquivButton.setDisable(disable);
        agregarUsoButton.setDisable(disable);
    }
}
