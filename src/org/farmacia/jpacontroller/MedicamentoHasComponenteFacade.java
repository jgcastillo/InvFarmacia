package org.farmacia.jpacontroller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Componente;
import org.farmacia.jpamodel.Medicamento;
import org.farmacia.jpamodel.MedicamentoHasComponente;
import org.farmacia.utils.JpaUtil;

/**
 *
 * @author jgcastillo
 */
public class MedicamentoHasComponenteFacade extends AbstractFacade<MedicamentoHasComponente>{

    private static final Logger LOG = Logger.getLogger(MedicamentoHasComponenteFacade
                                            .class.getName());
    
    public MedicamentoHasComponenteFacade() {
        super(MedicamentoHasComponente.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }
    
    public Optional<List<MedicamentoHasComponente>> findAllByComponente(Componente componente){
        Optional<List<MedicamentoHasComponente>> optList = Optional.empty();
        try {
            String query = "FROM MedicamentoHasComponente mhc "
                    + "WHERE mhc.componenteId = :componente";
            TypedQuery<MedicamentoHasComponente> q = getEntityManager()
                    .createQuery(query, MedicamentoHasComponente.class);
            q.setParameter("componente", componente);
            optList = Optional.ofNullable(q.getResultList());
        } catch (Exception e) {
        }
        return optList;
    }
    
    public Optional<List<Medicamento>> findAllByPatronComponente(String patron){
        Optional<List<Medicamento>> optList = Optional.empty();
        try {
            String query = "SELECT m FROM MedicamentoHasComponente mhc "
                    + "JOIN mhc.componenteId c JOIN mhc.medicamentoId m "
                    + "WHERE c.nombre LIKE CONCAT('%', ?1, '%')";
            Query q = getEntityManager().createQuery(query);
            q.setParameter(1, patron);
            optList = Optional.ofNullable(q.getResultList());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return optList;
    }
    
    public Optional<List<MedicamentoHasComponente>> findAllByMedicamento(Medicamento medicamento) {
        Optional<List<MedicamentoHasComponente>> optList = Optional.empty();
        try {
            String query = "FROM MedicamentoHasComponente mhc "
                    + "WHERE mhc.medicamentoId = :medicamento";
            TypedQuery<MedicamentoHasComponente> q = getEntityManager()
                    .createQuery(query, MedicamentoHasComponente.class);
            q.setParameter("medicamento", medicamento);
            optList = Optional.ofNullable(q.getResultList());
        } catch (Exception e) {
        }
        return optList;
    }
    
    public int deleteAllByMedicamento(Medicamento medicamento) {
        int result = 0;
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            String query = "DELETE FROM MedicamentoHasComponente mhc "
                    + "WHERE mhc.medicamentoId =:medicamento";
            Query q = em.createQuery(query);
            q.setParameter("medicamento", medicamento);
            result = q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
        } finally {
            if(em != null){
                em.close();
            }
        }
        return result;
    }
}
