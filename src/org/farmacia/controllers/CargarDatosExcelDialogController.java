/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.farmacia.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jgcastillo
 */
public class CargarDatosExcelDialogController implements Initializable {

    @FXML
    private TextField archivoTextField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label updateLabel;

    private Stage mainStage;
    private final FileChooser fileChooser = new FileChooser();
    //acceso al middleware
    private final MiddlewareController middlewareController;
    private static final Logger LOG = Logger.getLogger(CargarDatosExcelDialogController.class.getName());
    private File file;

    public CargarDatosExcelDialogController() {
        this.middlewareController = new MiddlewareController();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progressBar.setVisible(false);
        updateLabel.setText("");
    }

    @FXML
    private void handleBuscarArchivo(ActionEvent event) {
        findMainStage();
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(mainStage);
        archivoTextField.setText(file.getAbsolutePath());
    }

    @FXML
    private void handleCargarArchivo(ActionEvent event) {
        progressBar.setVisible(true);
        updateLabel.setText("");

        Task<Void> task = new InnerTask();
        //progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        progressBar.visibleProperty().bind(task.runningProperty());
        updateLabel.textProperty().bind(task.messageProperty());
        new Thread(task).start();

    }

    private void findMainStage() {
        mainStage = (Stage) archivoTextField.getScene().getWindow();
    }

    private void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("Buscar Excel");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX", "*.xlsx"),
                new FileChooser.ExtensionFilter("XLS", "*.xls")
        );
    }

    private class InnerTask extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            try {
                boolean result = middlewareController.saveExcelData(file);
                if(result){
                    updateMessage("Listo!");
                } else {
                    updateMessage("Terminó con error");
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error al procesar el archivo, error: {0}", e);
            }
            return null;
        }

    }
}
