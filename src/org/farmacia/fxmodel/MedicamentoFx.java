package org.farmacia.fxmodel;

import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author jgcastillo
 */
public class MedicamentoFx {
    private IntegerProperty id;
    private StringProperty nombre;
    private StringProperty ubicacion;
    private StringProperty presentacion;
    private ListProperty<String> usos;
    private ListProperty<String> componentes;
    private ListProperty<String> equivalentes;
    private ObjectProperty<Integer> cantidad;
    
    public MedicamentoFx(){
    }

    public MedicamentoFx(int id, String nombre, String ubicacion, String presentacion, 
            List<String> componentes) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.ubicacion = new SimpleStringProperty(ubicacion);
        this.presentacion = new SimpleStringProperty(presentacion);
        ObservableList<String> observList = FXCollections.observableArrayList(componentes);
        this.componentes = new SimpleListProperty<>(observList);
    }
    
    public MedicamentoFx(int id, String nombre, String ubicacion, String presentacion, 
            List<String> componentes, List<String> usos){
        this(id, nombre, ubicacion, presentacion, componentes);
        ObservableList<String> usosList = FXCollections.observableArrayList(usos);
        this.usos = new SimpleListProperty<>(usosList);
    }
    
    public MedicamentoFx(int id, String nombre, String ubicacion, String presentacion,
            List<String> componentes, List<String> usos, List<String> equivalentes){
        this(id, nombre, ubicacion, presentacion, componentes, usos);
        ObservableList<String> equivalentesList = FXCollections.observableArrayList(equivalentes);
        this.equivalentes = new SimpleListProperty<>(equivalentesList);
    }

    public int getId() {
        return id.get();
    }
    
    public void setId(int id){
        if(this.id == null){
            this.id = new SimpleIntegerProperty();
        }
        this.id.set(id);
    }
    
    public IntegerProperty idProperty(){
        return id;
    }

    public String getNombre() {
        return nombre.get();
    }
    
    public void setNombre(String nombre){
        if(this.nombre == null){
            this.nombre = new SimpleStringProperty();
        }
        this.nombre.set(nombre);
    }
    
    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getUbicacion() {
        if(ubicacion == null){
            this.ubicacion = new SimpleStringProperty();
            ubicacion.set("");
        }
        return ubicacion.get();
    }

    public void setUbicacion(String ubicacion) {
        if(this.ubicacion == null){
            this.ubicacion = new SimpleStringProperty();
        }
        this.ubicacion.set(ubicacion);
    }
    
    public StringProperty ubicacionProperty() {
        return ubicacion;
    }
    
    public String getPresentacion() {
        return presentacion.get();
    }

    public void setPresentacion(String presentacion) {
        if (this.presentacion == null) {
            this.presentacion = new SimpleStringProperty();
        }
        this.presentacion.set(presentacion);
    }

    public StringProperty presentacionProperty() {
        return presentacion;
    }

    public List<String> getUsos() {
        if(this.usos == null){
            this.usos = new SimpleListProperty<>();
        }
        return usos.get();
    }
    
    public void setUsos(ObservableList<String> usos) {
        if (this.usos == null) {
            this.usos = new SimpleListProperty();
        }
        this.usos.set(usos);
    }
    
    public ListProperty<String> usosProperty() {
        return usos;
    }
    
    public Integer getCantidad(){
        if(this.cantidad == null){
            this.cantidad.setValue(0);
        }
        return cantidad.get();
    }
    
    public void setCantidad(Integer cantidad){
        if(this.cantidad == null){
            this.cantidad = new SimpleObjectProperty<>();
        }
        this.cantidad.set(cantidad);
    }
    
    public ObjectProperty<Integer> cantidadProperty(){
        return cantidad;
    }

    public List<String> getComponentes() {
        if (this.componentes == null) {
            this.componentes = new SimpleListProperty<>();
        }
        return componentes.get();
    }
    
    public void setComponentes(ObservableList<String> componentes){
        if(this.componentes == null){
            this.componentes = new SimpleListProperty<>();
        }
        this.componentes.set(componentes);
    }
    
    public ListProperty<String> componentesProperty() {
        return componentes;
    }

    public List<String> getEquivalentes() {
        if (this.equivalentes == null) {
            this.equivalentes = new SimpleListProperty<>();
        }
        return equivalentes.get();
    }

    public void setEquivalentes(ObservableList<String> equivalentes) {
        if (this.equivalentes == null) {
            this.equivalentes = new SimpleListProperty<>();
        }
        this.equivalentes.set(equivalentes);
    }

    public ListProperty<String> equivalentesProperty() {
        return equivalentes;
    }
}

