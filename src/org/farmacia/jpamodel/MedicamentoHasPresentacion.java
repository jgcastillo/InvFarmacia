/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.farmacia.jpamodel;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "medicamento_has_presentacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MedicamentoHasPresentacion.findAll", query = "SELECT m FROM MedicamentoHasPresentacion m")
    , @NamedQuery(name = "MedicamentoHasPresentacion.findById", query = "SELECT m FROM MedicamentoHasPresentacion m WHERE m.id = :id")})
public class MedicamentoHasPresentacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "medicamento_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Medicamento medicamentoId;
    @JoinColumn(name = "presentacion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Presentacion presentacionId;

    public MedicamentoHasPresentacion() {
    }

    public MedicamentoHasPresentacion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Medicamento getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Medicamento medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public Presentacion getPresentacionId() {
        return presentacionId;
    }

    public void setPresentacionId(Presentacion presentacionId) {
        this.presentacionId = presentacionId;
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
        if (!(object instanceof MedicamentoHasPresentacion)) {
            return false;
        }
        MedicamentoHasPresentacion other = (MedicamentoHasPresentacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.farmacia.jpamodel.MedicamentoHasPresentacion[ id=" + id + " ]";
    }
    
}
