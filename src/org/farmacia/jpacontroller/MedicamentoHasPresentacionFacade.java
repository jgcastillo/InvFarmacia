package org.farmacia.jpacontroller;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Medicamento;
import org.farmacia.jpamodel.MedicamentoHasPresentacion;
import org.farmacia.jpamodel.Presentacion;
import org.farmacia.utils.JpaUtil;

/**
 *
 * @author jgcastillo
 */
public class MedicamentoHasPresentacionFacade 
        extends AbstractFacade<MedicamentoHasPresentacion>{

    public MedicamentoHasPresentacionFacade() {
        super(MedicamentoHasPresentacion.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }
    
    public Optional<MedicamentoHasPresentacion> findByMedicamento(
            Medicamento medicamento){
        Optional<MedicamentoHasPresentacion> mhp = Optional.empty();
        try {
            String query = "FROM MedicamentoHasPresentacion m "
                    + "WHERE m.medicamentoId = :medicamento";
            TypedQuery<MedicamentoHasPresentacion> q = 
                    getEntityManager().createQuery(query, 
                            MedicamentoHasPresentacion.class);
            q.setParameter("medicamento", medicamento);
            mhp = Optional.ofNullable(q.getSingleResult());
        } catch (Exception e) {
        }
        return mhp;
    }
    
    public Optional<MedicamentoHasPresentacion> findByMedicamentoAndPresentacion(
            Medicamento medicamento, Presentacion presentacion){
        Optional<MedicamentoHasPresentacion> mhp = Optional.empty();
        try {
            String query = "FROM MedicamentoHasPresentacion m "
                    + "WHERE m.medicamentoId = :medicamento AND "
                    + "m.presentacionId = :presentacion";
            TypedQuery<MedicamentoHasPresentacion> q = 
                    getEntityManager().createQuery(query, 
                            MedicamentoHasPresentacion.class);
            q.setParameter("medicamento", medicamento);
            q.setParameter("presentacion", presentacion);
            mhp = Optional.ofNullable(q.getSingleResult());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return mhp;
    }
    
    public Optional<List<MedicamentoHasPresentacion>> findAllByMedicamento(
            Medicamento medicamento) {
        Optional<List<MedicamentoHasPresentacion>> mhpList = Optional.empty();
        try {
            String query = "FROM MedicamentoHasPresentacion m "
                    + "WHERE m.medicamentoId = :medicamento";
            TypedQuery<MedicamentoHasPresentacion> 
                    q = getEntityManager().createQuery(query,
                            MedicamentoHasPresentacion.class);
            q.setParameter("medicamento", medicamento);
            mhpList = Optional.ofNullable(q.getResultList());
        } catch (Exception e) {
        }
        return mhpList;
    }
}
