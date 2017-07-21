package org.farmacia.jpacontroller;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Medicamento;
import org.farmacia.utils.JpaUtil;

/**
 * Clase que permite realizar las operaciones contra la tabla Medicamento
 * de la base de datos
 * @author jgcastillo
 */
public class MedicamentoFacade extends AbstractFacade<Medicamento>{

    public MedicamentoFacade() {
        super(Medicamento.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }
    
    public Medicamento findByNombre(String nombre){
        Medicamento medicamento = null;
        try {
            String query = "FROM Medicamento m WHERE m.nombre = :nombre";
            TypedQuery<Medicamento> q = getEntityManager().createQuery(query, Medicamento.class);
            q.setParameter("nombre", nombre);
            medicamento = q.getSingleResult();
        } catch (Exception e) {
        }
        return medicamento;
    }
    
    public List<Medicamento> findAllByNombre(String nombre){
        List<Medicamento> medicamentos = null;
        try {
            String query = "FROM Medicamento m WHERE m.nombre = :nombre";
            Query q = getEntityManager().createQuery(query, Medicamento.class);
            q.setParameter("nombre", nombre);
            medicamentos = q.getResultList();
        } catch (Exception e) {
        }
        return medicamentos;
    }
}
