package org.farmacia.fxmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UsuarioFx {
    
    private IntegerProperty id;
    private StringProperty nombre;
    private StringProperty apellido;
    private StringProperty clave;
    private StringProperty usuario;
    private StringProperty perfil;
    
    public final static String PERFIL_SUPER = "Super_Admin";
    public final static String PERFIL_ADMIN ="Administrador";
    public final static String PERFIL_USER = "Usuario";
    public final static String PERFIL_CONSULTA = "Consulta";

    public UsuarioFx() {
    }

    public UsuarioFx(int id, String nombre, String apellido, String usuario
            , String perfil) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.usuario = new SimpleStringProperty(usuario);
        this.perfil = new SimpleStringProperty(perfil);
    }
    
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        if(this.id == null){
            this.id = new SimpleIntegerProperty();
        }
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public String getNombre() {
        return this.nombre.get();
    }

    public void setNombre(String nombre) {
        if(this.nombre == null){
            this.nombre = new SimpleStringProperty();
        }
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }
    
    public String getApellido() {
        return apellido.get();
    }

    public void setApellido(String apellido) {
        if (this.apellido == null) {
            this.apellido = new SimpleStringProperty();
        }
        this.apellido.set(apellido);
    }

    public StringProperty apellidoProperty() {
        return apellido;
    }
    
    public String getUsuario() {
        return usuario.get();
    }

    public void setUsuario(String usuario) {
        if (this.usuario == null) {
            this.usuario = new SimpleStringProperty();
        }
        this.usuario.set(usuario);
    }

    public StringProperty usuarioProperty() {
        return usuario;
    }
    
    public String getClave() {
        return clave.get();
    }

    public void setClave(String clave) {
        if (this.clave == null) {
            this.clave = new SimpleStringProperty();
        }
        this.clave.set(clave);
    }

    public StringProperty claveProperty() {
        return clave;
    }
    
    public String getPerfil() {
        return perfil.get();
    }

    public void setPerfil(String perfil) {
        if (this.perfil == null) {
            this.perfil = new SimpleStringProperty();
        }
        this.perfil.set(perfil);
    }

    public StringProperty perfilProperty() {
        return perfil;
    }
}
