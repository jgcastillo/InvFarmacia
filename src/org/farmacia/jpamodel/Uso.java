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
@Table(name = "uso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Uso.findAll", query = "SELECT u FROM Uso u")
    , @NamedQuery(name = "Uso.findById", query = "SELECT u FROM Uso u WHERE u.id = :id")
    , @NamedQuery(name = "Uso.findByNombreUso", query = "SELECT u FROM Uso u WHERE u.nombreUso = :nombreUso")})
public class Uso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre_uso")
    private String nombreUso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usoId")
    private Collection<MedicamentoHasUso> medicamentoHasUsoCollection;

    public Uso() {
    }

    public Uso(String nombreUso) {
        this.nombreUso = nombreUso;
    }
    
    public Uso(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreUso() {
        return nombreUso;
    }

    public void setNombreUso(String nombreUso) {
        this.nombreUso = nombreUso;
    }

    @XmlTransient
    public Collection<MedicamentoHasUso> getMedicamentoHasUsoCollection() {
        return medicamentoHasUsoCollection;
    }

    public void setMedicamentoHasUsoCollection(Collection<MedicamentoHasUso> medicamentoHasUsoCollection) {
        this.medicamentoHasUsoCollection = medicamentoHasUsoCollection;
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
        if (!(object instanceof Uso)) {
            return false;
        }
        Uso other = (Uso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.farmacia.jpamodel.Uso[ id=" + id + " ]";
    }
    
}
