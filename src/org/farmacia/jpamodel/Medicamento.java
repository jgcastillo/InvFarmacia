/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.farmacia.jpamodel;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "medicamento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medicamento.findAll", query = "SELECT m FROM Medicamento m")
    , @NamedQuery(name = "Medicamento.findById", query = "SELECT m FROM Medicamento m WHERE m.id = :id")
    , @NamedQuery(name = "Medicamento.findByNombre", query = "SELECT m FROM Medicamento m WHERE m.nombre = :nombre")
    , @NamedQuery(name = "Medicamento.findByUbicacion", query = "SELECT m FROM Medicamento m WHERE m.ubicacion = :ubicacion")})
public class Medicamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "ubicacion")
    private String ubicacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicamentoId")
    private Collection<MedicamentoHasUso> medicamentoHasUsoCollection;
    @OneToMany(mappedBy = "medicamentoId")
    private Collection<Movimiento> movimientoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicamentoId")
    private Collection<MedicamentoHasEquivalente> medicamentoHasEquivalenteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicamentoId")
    private Collection<MedicamentoHasPresentacion> medicamentoHasPresentacionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicamentoId")
    private Collection<MedicamentoHasComponente> medicamentoHasComponenteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicamentoId")
    private Collection<Inventario> inventarioCollection;

    public Medicamento() {
    }
    
    public Medicamento(String nombre) {
        this.nombre = nombre;
    }

    public Medicamento(Integer id) {
        this.id = id;
    }

    public Medicamento(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @XmlTransient
    public Collection<MedicamentoHasUso> getMedicamentoHasUsoCollection() {
        return medicamentoHasUsoCollection;
    }

    public void setMedicamentoHasUsoCollection(Collection<MedicamentoHasUso> medicamentoHasUsoCollection) {
        this.medicamentoHasUsoCollection = medicamentoHasUsoCollection;
    }

    @XmlTransient
    public Collection<Movimiento> getMovimientoCollection() {
        return movimientoCollection;
    }

    public void setMovimientoCollection(Collection<Movimiento> movimientoCollection) {
        this.movimientoCollection = movimientoCollection;
    }

    @XmlTransient
    public Collection<MedicamentoHasEquivalente> getMedicamentoHasEquivalenteCollection() {
        return medicamentoHasEquivalenteCollection;
    }

    public void setMedicamentoHasEquivalenteCollection(Collection<MedicamentoHasEquivalente> medicamentoHasEquivalenteCollection) {
        this.medicamentoHasEquivalenteCollection = medicamentoHasEquivalenteCollection;
    }

    @XmlTransient
    public Collection<MedicamentoHasPresentacion> getMedicamentoHasPresentacionCollection() {
        return medicamentoHasPresentacionCollection;
    }

    public void setMedicamentoHasPresentacionCollection(Collection<MedicamentoHasPresentacion> medicamentoHasPresentacionCollection) {
        this.medicamentoHasPresentacionCollection = medicamentoHasPresentacionCollection;
    }

    @XmlTransient
    public Collection<MedicamentoHasComponente> getMedicamentoHasComponenteCollection() {
        return medicamentoHasComponenteCollection;
    }

    public void setMedicamentoHasComponenteCollection(Collection<MedicamentoHasComponente> medicamentoHasComponenteCollection) {
        this.medicamentoHasComponenteCollection = medicamentoHasComponenteCollection;
    }

    @XmlTransient
    public Collection<Inventario> getInventarioCollection() {
        return inventarioCollection;
    }

    public void setInventarioCollection(Collection<Inventario> inventarioCollection) {
        this.inventarioCollection = inventarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicamento)) {
            return false;
        }
        Medicamento other = (Medicamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.farmacia.jpamodel.Medicamento[ id=" + id + " ]";
    }
    
}
