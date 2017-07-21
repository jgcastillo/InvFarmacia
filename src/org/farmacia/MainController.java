package org.farmacia;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import org.farmacia.controllers.MiddlewareController;
import org.farmacia.fxmodel.MedicamentoFx;
import org.farmacia.fxmodel.UsuarioFx;
import org.farmacia.jpacontroller.ComponenteFacade;
import org.farmacia.jpacontroller.InventarioFacade;
import org.farmacia.jpacontroller.MedicamentoFacade;
import org.farmacia.jpacontroller.MedicamentoHasComponenteFacade;
import org.farmacia.jpacontroller.MedicamentoHasEquivalenteFacade;
import org.farmacia.jpacontroller.MedicamentoHasPresentacionFacade;
import org.farmacia.jpacontroller.MedicamentoHasUsoFacade;
import org.farmacia.jpamodel.Medicamento;
import org.farmacia.jpamodel.MedicamentoHasComponente;
import org.farmacia.jpamodel.MedicamentoHasEquivalente;
import org.farmacia.jpamodel.MedicamentoHasPresentacion;
import org.farmacia.jpamodel.MedicamentoHasUso;
import org.farmacia.jpamodel.Usuario;
import org.farmacia.utils.MainViewData;

/**
 * Clase controladora de la vista principal, da acceso a las opciones del menu y
 * los botones que en ella se encuentran.
 *
 * @author jgcastillo
 */
public class MainController implements Initializable {

    // items del menu
    @FXML
    private MenuItem ingresarMedicamentoMI;
    @FXML
    private MenuItem agregarMedicamentoMI;
    @FXML
    private MenuItem cargarDatosExcelMI;
    @FXML
    private TextField buscarNombreTextField;
    @FXML
    private TextField buscarComponenteTextField;
    @FXML
    private MenuItem administrarUsuariosMenu;
    @FXML
    private SeparatorMenuItem separador;
    @FXML
    private Menu accionesMenu;

    // controles de la forma
    @FXML
    private Button entregarButton;
    @FXML
    private Label cantidadSolicitadaLabel;
    @FXML
    private Button editarButton;

    // de la tabla de medicamentos
    @FXML
    private TableView<MedicamentoFx> medicamentosTable;
    @FXML
    private TableColumn<MedicamentoFx, String> medicamentoColumn;
    @FXML
    private TableColumn<MedicamentoFx, String> presentacionColumn;
    @FXML
    private TableColumn<MedicamentoFx, Integer> cantidadColumn;

    // del detalle del medicamento
    @FXML
    private Label productoLabel;
    @FXML
    private Label presentacionLabel;
    @FXML
    private Label componentesLabel;
    @FXML
    private Label usosLabel;
    @FXML
    private Label equivalentesLabel;
    @FXML
    private Label cantidadLabel;
    @FXML
    private Label ubicacionLabel;
    @FXML
    private TextField cantidadSolicitadaTextField;

    // del pane de edición del medicamento
    @FXML
    private Pane editPane;
    @FXML
    private TabPane editProductTabPane;
    @FXML
    private TextField editNombreTextField;
    @FXML
    private TextField editUbicacionTextField;
    @FXML
    private Label editPresentacionLabel;
    @FXML
    private TextField editNewPresentacionTextField;
    @FXML
    private TextField editUsoTextField;
    @FXML
    private ListView<String> editUsoListView;
    @FXML
    private TextField editCompTextField;
    @FXML
    private ListView<String> editCompListView;
    @FXML
    private TextField editEquiTextField;
    @FXML
    private ListView<String> editEquiListView;

    // para el manejo del perfil
    private Usuario usuario;
    private String perfil;
    private MedicamentoFx medGlobalFx;
    private String presentacionValue; // usada para crear una nueva presentacion
    //private String oldPresentacion = ""; // usada para actualizar la presentación 

    // de las clases manejadoras de la base de datos
    private final MedicamentoFacade medicamentoFacade;// = new MedicamentoFacade();
    private final MedicamentoHasPresentacionFacade medHasPresFacade;
    private final MedicamentoHasUsoFacade medHasUsoFacade;
    private final ComponenteFacade componenteFacade;
    private final MedicamentoHasComponenteFacade medHasCompFacade;
    private final MedicamentoHasEquivalenteFacade medHasEquiFacade;
    private final MainViewData mainViewData;
    private final InventarioFacade inventarioFacade;
    private final MiddlewareController middleware;

    private Stage mainStage;

    public MainController() {
        this.medicamentoFacade = new MedicamentoFacade();
        this.medHasPresFacade = new MedicamentoHasPresentacionFacade();
        this.componenteFacade = new ComponenteFacade();
        this.medHasCompFacade = new MedicamentoHasComponenteFacade();
        this.medHasUsoFacade = new MedicamentoHasUsoFacade();
        this.medHasEquiFacade = new MedicamentoHasEquivalenteFacade();
        this.inventarioFacade = new InventarioFacade();
        this.mainViewData = new MainViewData();
        this.middleware = new MiddlewareController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // para auto llenar los campos Medicamentos y componentes
        loadInitialData();

        //fillMedicamentosTable();
        showMedicamentoDetails(null);
        enableMenus();
        medicamentosTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMedicamentoDetails(newValue));

        hideEditMedicamento();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private void enableMenus() {
        perfil = (String) Context.getInstance().getGlobal().get("perfil");

        // aqui se habilitan los menús de acuerdo al usuario
        switch (perfil) {
            case UsuarioFx.PERFIL_USER:
                separador.setVisible(false);
                administrarUsuariosMenu.setVisible(false);
                cargarDatosExcelMI.setVisible(false);
                break;
            case UsuarioFx.PERFIL_CONSULTA:
                accionesMenu.setVisible(false);
                cantidadSolicitadaLabel.setVisible(false);
                cantidadSolicitadaTextField.setVisible(false);
                entregarButton.setVisible(false);
                cargarDatosExcelMI.setVisible(false);
                break;
        }

    }

    private void hideEditMedicamento() {
        editarButton.setVisible(false);
        editPane.setVisible(false);
    }

    @FXML
    private void handleSalirMenuItem() {
        Platform.exit();
    }

    @FXML
    private void handleIngresarMedicamento() {
        openDialogWithData("view/IngresarMedicamentoDialog.fxml", "Ingresar medicamento");
    }

    @FXML
    private void handleManageUsers() {
        openDialog("view/ManageUsersDialog.fxml", "Administrar Usuarios");
    }

    @FXML
    private void handleLoadExcelData() {
        openDialog("view/CargarDatosExcelDialog.fxml", "Cargar Datos de Excel");
    }

    @FXML
    private void handleEntregar(ActionEvent event) {
        MedicamentoFx medFx = (MedicamentoFx) event.getSource();
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadSolicitadaTextField.getText());
            middleware.entregaInventario(medFx.getId(), cantidad);

            showDialogInformation(AlertType.INFORMATION, "Información",
                    "Operación exitosa", "Se ha entregado " + cantidad
                    + " unidades del medicamento " + medFx.getNombre());
            clearFields(event);
        } catch (NumberFormatException e) {
            showDialogInformation(AlertType.ERROR, "Error", "Error de formato",
                    "Debe incluir un número en el campo 'Cantidad Solicitada'");
        }
    }

    @FXML
    private void handleAbout() {
        openDialog("view/AboutDialog.fxml", "Acerca de...");
    }

    @FXML
    private void handleEditCancelarButton(ActionEvent event) {
        clearEditFields();
        editPane.setVisible(false);
    }

    @FXML
    private void handleEditGuardarButton(ActionEvent event) {
        if (!editNombreTextField.getText().isEmpty()) {
            editNombreUbicacion(medGlobalFx);
            String oldPresentacion = medGlobalFx.getPresentacion().trim();
            
            if(!editNewPresentacionTextField.getText().isEmpty()
                    && !oldPresentacion.equals(editNewPresentacionTextField.getText())){
                medGlobalFx.setPresentacion(editNewPresentacionTextField.getText());
            }
            
            ObservableList<String> newUsos = editUsoListView.getItems();
            medGlobalFx.setUsos(newUsos);
            
            ObservableList<String> newComps = editCompListView.getItems();
            medGlobalFx.setComponentes(newComps);

            ObservableList<String> newEquivs = editEquiListView.getItems();
            medGlobalFx.setEquivalentes(newEquivs);
            
            medGlobalFx = middleware.updateMedicamento(medGlobalFx, oldPresentacion);
            showMedicamentoDetails(medGlobalFx);

            clearEditFields();
            editPane.setVisible(false);
        } else {
            showDialogInformation(AlertType.ERROR, "Error",
                    "Nombre del medicamento vacío",
                    "No puede dejar sin colocar el nombre del medicamento");
        }
    }

    private void editNombreUbicacion(MedicamentoFx medFx) {
        medFx.setNombre(editNombreTextField.getText().trim());
        Optional<String> optUbicacion = Optional.ofNullable(editUbicacionTextField.getText());
        if(optUbicacion.isPresent()){
            medFx.setUbicacion(optUbicacion.get().trim());
        }
    }

    

    private void findMainStage() {
        mainStage = (Stage) buscarNombreTextField.getScene().getWindow();
    }

    private void openDialog(String resource, String title) {
        try {
            findMainStage();

            Parent root = FXMLLoader.load(getClass().getResource(resource));
            Stage aboutDialogStage = new Stage();
            showDialog(aboutDialogStage, title, root);
        } catch (IOException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void openDialogWithData(String resource, String title) {
        try {
            findMainStage();

            Parent root = FXMLLoader.load(getClass().getResource(resource));
            Stage aboutDialogStage = new Stage();
            showDialog(aboutDialogStage, title, root);
            loadInitialData();
        } catch (IOException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void showMedicamentoDetails(MedicamentoFx medFx) {
        if (medFx != null) {

            editUsoListView.getItems().clear();
            editCompListView.getItems().clear();
            editEquiListView.getItems().clear();
            
            if (perfil.equals(UsuarioFx.PERFIL_ADMIN) || perfil.equals(UsuarioFx.PERFIL_SUPER)) {
                editarButton.setVisible(true);
            }

            medGlobalFx = medFx;
            // referente al nombre del producto
            productoLabel.setText(medFx.getNombre());
            editNombreTextField.setText(medFx.getNombre());

            // referente a la presentación del producto
            presentacionLabel.setText(medFx.getPresentacion());
            editPresentacionLabel.setText(medFx.getPresentacion());

            // referente al uso
            List<String> usos = medFx.getUsos();
            if (!usos.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                usos.forEach(us -> {
                    sb.append(us).append(" ");
                });
                usosLabel.setText(sb.toString());
                ObservableList<String> usosObs = FXCollections.observableArrayList(usos);
                editUsoListView.getItems().addAll(usosObs);
            } else {
                usosLabel.setText("");
            }

            // referente a los componentes
            List<String> componentes = medFx.getComponentes();
            if (!componentes.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                componentes.forEach(comp -> {
                    sb.append(comp).append(" ");
                });
                componentesLabel.setText(sb.toString());
                ObservableList<String> compObs = FXCollections.observableArrayList(componentes);
                editCompListView.getItems().addAll(compObs);
            } else {
                componentesLabel.setText("");
            }

            // referente a los equivalentes
            List<String> equivalentes = medFx.getEquivalentes();

            if (!equivalentes.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                equivalentes.forEach(equi -> {
                    sb.append(equi).append(" ");
                });
                equivalentesLabel.setText(sb.toString());
                ObservableList<String> equiObs = FXCollections.observableArrayList(equivalentes);
                editEquiListView.getItems().addAll(equiObs);
            } else {
                equivalentesLabel.setText("");
            }

            //Referente a la cantidad
            cantidadLabel.setText(String.valueOf(medFx.getCantidad()));

            // referente a la ubicación
            ubicacionLabel.setText(medFx.getUbicacion());
            editUbicacionTextField.setText(medFx.getUbicacion());

        } else {
            productoLabel.setText("");
            presentacionLabel.setText("");
            componentesLabel.setText("");
            usosLabel.setText("");
            cantidadLabel.setText("");
            ubicacionLabel.setText("");
            equivalentesLabel.setText("");
            editarButton.setVisible(false);
            editPane.setVisible(false);

            editNombreTextField.clear();
            editUbicacionTextField.clear();
            editCompTextField.clear();
            editUsoTextField.clear();
            editEquiTextField.clear();
        }
    }

    @FXML
    private void buscarAction(ActionEvent event) {
        String nombreMed = buscarNombreTextField.getText();
        String componentMed = buscarComponenteTextField.getText();

        ObservableList<MedicamentoFx> medicamentosList = null;
        if ((nombreMed.isEmpty()) && (componentMed.isEmpty())) {
            medicamentosList = buscarAll();
        } else if (!nombreMed.isEmpty()) {
            medicamentosList = buscarByNombre(nombreMed);
        } else if (!componentMed.isEmpty()) {
            medicamentosList = buscarByComponent(componentMed);
        }

        fillMedicamentosTable(medicamentosList);
        buscarNombreTextField.clear();
        buscarComponenteTextField.clear();
        cantidadSolicitadaTextField.clear();
    }

    @FXML
    private void handleEditarButton(ActionEvent event) {
        // mostrar el panel
        editProductTabPane.getSelectionModel().select(0);
        editPane.setVisible(true);
    }

    @FXML
    private void handleEditAgregarUsoButton(ActionEvent event) {
        editUsoListView.getItems().add(editUsoTextField.getText().trim());
        editUsoTextField.clear();
    }

    @FXML
    private void handleEditEliminarUsoButton(ActionEvent event) {
        int selected = editUsoListView.getSelectionModel().getSelectedIndex();
        if (selected != -1) {
            editUsoListView.getItems().remove(selected);
        }
    }

    @FXML
    private void handleEditAgregarCompButton(ActionEvent event) {
        editCompListView.getItems().add(editCompTextField.getText().trim());
        editCompTextField.clear();
    }

    @FXML
    private void handleEditEliminarCompButton(ActionEvent event) {
        int selected = editCompListView.getSelectionModel().getSelectedIndex();
        if (selected != -1) {
            editCompListView.getItems().remove(selected);
        }
    }

    @FXML
    private void handleEditAgregarEquiButton(ActionEvent event) {
        editEquiListView.getItems().add(editEquiTextField.getText().trim());
        editEquiTextField.clear();
    }

    @FXML
    private void handleEditEliminarEquiButton(ActionEvent event) {
        int selected = editEquiListView.getSelectionModel().getSelectedIndex();
        if (selected != -1) {
            editEquiListView.getItems().remove(selected);
        }
    }

    @FXML
    private void clearFields(ActionEvent event) {
        buscarNombreTextField.clear();
        buscarComponenteTextField.clear();
        cantidadSolicitadaTextField.clear();
        medicamentosTable.getItems().clear();
    }

    private void clearEditFields() {
        editUsoTextField.clear();
        editUsoListView.getItems().clear();
        editCompTextField.clear();
        editCompListView.getItems().clear();
        editEquiTextField.clear();
        editEquiListView.getItems().clear();
    }

    private void loadInitialData() {
        String[] mediNames = mainViewData.loadInitialMedicaments();
        String[] presentacionNames = mainViewData.loadInitialPresentaciones();
        String[] usoNames = mainViewData.loadInitialUsos();
        String[] compNames = mainViewData.loadInitialComponentes();
        String[] equiNames = mainViewData.loadInitialEquivalentes();
        TextFields.bindAutoCompletion(buscarNombreTextField, mediNames);
        TextFields.bindAutoCompletion(editNewPresentacionTextField, presentacionNames);
        TextFields.bindAutoCompletion(editUsoTextField, usoNames);
        TextFields.bindAutoCompletion(editCompTextField, compNames);
        TextFields.bindAutoCompletion(editEquiTextField, equiNames);
    }

    private ObservableList<MedicamentoFx> buscarAll() {
        List<Medicamento> medicamentos = medicamentoFacade.findAll();
        medicamentos.sort((med1, med2) -> {
            return med1.getNombre().compareTo(med2.getNombre());
        });

        return parseListToObservableList(medicamentos);
    }

    private ObservableList<MedicamentoFx> buscarByNombre(String nombreMed) {
        List<Medicamento> medicamentos = medicamentoFacade.findAllByNombre(nombreMed);
        return parseListToObservableList(medicamentos);
    }

    private ObservableList<MedicamentoFx> parseListToObservableList(List<Medicamento> medicamentos) {
        ObservableList<MedicamentoFx> medicamentosList = FXCollections.observableArrayList();

        // busca las presentaciones
        medicamentos.forEach(med -> {
            Optional<List<MedicamentoHasPresentacion>> optMedHasPres = medHasPresFacade
                    .findAllByMedicamento(med);
            String presentacion;
            if (optMedHasPres.isPresent()) {
                List<MedicamentoHasPresentacion> lista = optMedHasPres.get();
                for (MedicamentoHasPresentacion mhp : lista) {
                    presentacion = mhp.getPresentacionId().getNombre();

                    // busca los componentes
                    Optional<List<MedicamentoHasComponente>> optMhcList
                            = medHasCompFacade.findAllByMedicamento(med);
                    List<String> componentes = new ArrayList<>();
                    if (optMhcList.isPresent()) {
                        List<MedicamentoHasComponente> mhcList = optMhcList.get();
                        mhcList.forEach((mch) -> {
                            componentes.add(mch.getComponenteId().getNombre());
                        });
                    }

                    // busca los usos
                    Optional<List<MedicamentoHasUso>> optMhuList
                            = medHasUsoFacade.findAllByMedicamento(med);
                    List<String> usos = new ArrayList<>();
                    if (optMhuList.isPresent()) {
                        List<MedicamentoHasUso> mhuList = optMhuList.get();
                        mhuList.forEach(mhu -> {
                            usos.add(mhu.getUsoId().getNombreUso());
                        });
                    }

                    // busca los equivalentes
                    Optional<List<MedicamentoHasEquivalente>> optMheList
                            = medHasEquiFacade.findAllByMedicamento(med);
                    List<String> equivalentes = new ArrayList<>();
                    if (optMheList.isPresent()) {
                        List<MedicamentoHasEquivalente> mheList = optMheList.get();
                        mheList.forEach(mhe -> {
                            equivalentes.add(mhe.getEquivalenteId().getNombre());
                        });
                    }

                    MedicamentoFx medfx = new MedicamentoFx(med.getId(), med.getNombre(),
                            med.getUbicacion(), presentacion, componentes, usos,
                            equivalentes);

                    int cantidad = middleware.findCantidadByMedicamento(med);
                    medfx.setCantidad(cantidad);

                    medicamentosList.add(medfx);
                }
            }

        });
        return medicamentosList;
    }

    private ObservableList<MedicamentoFx> buscarByComponent(String component) {
        Optional<List<Medicamento>> optMedicamentos = medHasCompFacade
                .findAllByPatronComponente(component);

        ObservableList<MedicamentoFx> medicamentosList = FXCollections
                .observableArrayList();

        if (optMedicamentos.isPresent()) {
            medicamentosList = parseListToObservableList(optMedicamentos.get());
        }
        return medicamentosList;
    }

    private void fillMedicamentosTable(ObservableList<MedicamentoFx> medicamentosList) {
        if (medicamentosList != null) {

            medicamentosList.sort((MedicamentoFx med1, MedicamentoFx med2) -> {
                return med1.getNombre().compareTo(med2.getNombre());
            });
            medicamentosTable.setItems(medicamentosList);
            medicamentoColumn.setCellValueFactory(cellData
                    -> cellData.getValue().nombreProperty());
            presentacionColumn.setCellValueFactory(cellData
                    -> cellData.getValue().presentacionProperty());
            cantidadColumn.setCellValueFactory(cellData
                    -> cellData.getValue().cantidadProperty());
        }
    }

    public static void showDialogInformation(AlertType alertType, String title,
            String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void showDialog(Stage stage, String title, Parent root) {
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

}
