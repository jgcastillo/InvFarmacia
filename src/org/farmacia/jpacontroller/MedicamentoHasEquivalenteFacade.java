package org.farmacia.jpacontroller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Equivalente;
import org.farmacia.jpamodel.Medicamento;
import org.farmacia.jpamodel.MedicamentoHasEquivalente;
import org.farmacia.utils.JpaUtil;

/**
 *
 * @author jgcastillo
 */
public class MedicamentoHasEquivalenteFacade extends AbstractFacade<MedicamentoHasEquivalente>{
    private static final Logger LOG = Logger.getLogger(MedicamentoHasEquivalenteFacade.class.getName());

    public MedicamentoHasEquivalenteFacade() {
        super(MedicamentoHasEquivalente.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }
    
    public Optional<List<MedicamentoHasEquivalente>> findAllByEquivalente(Equivalente equivalente){
        Optional<List<MedicamentoHasEquivalente>> optList = Optional.empty();
        try {
            String query = "FROM MedicamentoHasEquivalente mhe "
                    + "WHERE mhe.equivalenteId = :equivalente";
            TypedQuery<MedicamentoHasEquivalente> q = getEntityManager()
                    .createQuery(query, MedicamentoHasEquivalente.class);
            q.setParameter("equivalente", equivalente);
            optList = Optional.ofNullable(q.getResultList());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return optList;
    }
    
    public Optional<List<Medicamento>> findAllByPatronComponente(String patron){
        Optional<List<Medicamento>> optList = Optional.empty();
        try {
            String query = "SELECT m FROM MedicamentoHasEquivalente mhe "
                    + "JOIN mhe.componenteId e JOIN mhe.medicamentoId m "
                    + "WHERE e.nombre LIKE CONCAT('%', ?1, '%')";
            Query q = getEntityManager().createQuery(query);
            q.setParameter(1, patron);
            optList = Optional.ofNullable(q.getResultList());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return optList;
    }
    
    public Optional<List<MedicamentoHasEquivalente>> findAllByMedicamento(Medicamento medicamento) {
        Optional<List<MedicamentoHasEquivalente>> optList = Optional.empty();
        try {
            String query = "FROM MedicamentoHasEquivalente mhe "
                    + "WHERE mhe.medicamentoId = :medicamento";
            TypedQuery<MedicamentoHasEquivalente> q = getEntityManager()
                    .createQuery(query, MedicamentoHasEquivalente.class);
            q.setParameter("medicamento", medicamento);
            optList = Optional.ofNullable(q.getResultList());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return optList;
    }
    
    public int deleteAllByMedicamento(Medicamento medicamento) {
        int result = 0;
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            String query = "DELETE FROM MedicamentoHasEquivalente mhe "
                    + "WHERE mhe.medicamentoId =:medicamento";
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
