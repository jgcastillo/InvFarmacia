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
@Table(name = "medicamento_has_equivalente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MedicamentoHasEquivalente.findAll", query = "SELECT m FROM MedicamentoHasEquivalente m")
    , @NamedQuery(name = "MedicamentoHasEquivalente.findById", query = "SELECT m FROM MedicamentoHasEquivalente m WHERE m.id = :id")})
public class MedicamentoHasEquivalente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "equivalente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Equivalente equivalenteId;
    @JoinColumn(name = "medicamento_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Medicamento medicamentoId;

    public MedicamentoHasEquivalente() {
    }

    public MedicamentoHasEquivalente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Equivalente getEquivalenteId() {
        return equivalenteId;
    }

    public void setEquivalenteId(Equivalente equivalenteId) {
        this.equivalenteId = equivalenteId;
    }

    public Medicamento getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Medicamento medicamentoId) {
        this.medicamentoId = medicamentoId;
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
        if (!(object instanceof MedicamentoHasEquivalente)) {
            return false;
        }
        MedicamentoHasEquivalente other = (MedicamentoHasEquivalente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.farmacia.jpamodel.MedicamentoHasEquivalente[ id=" + id + " ]";
    }
    
}
