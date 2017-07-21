package org.farmacia.jpacontroller;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Medicamento;
import org.farmacia.jpamodel.MedicamentoHasUso;
import org.farmacia.utils.JpaUtil;

/**
 *
 * @author jgcastillo
 */
public class MedicamentoHasUsoFacade extends AbstractFacade<MedicamentoHasUso>{

    public MedicamentoHasUsoFacade() {
        super(MedicamentoHasUso.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }
    
    public Optional<List<MedicamentoHasUso>> findAllByMedicamento(Medicamento medicamento){
        Optional<List<MedicamentoHasUso>> lista = Optional.empty();
        try {
            String query = "FROM MedicamentoHasUso mhu WHERE mhu.medicamentoId = :medicamento";
            TypedQuery q = getEntityManager().createQuery(query, MedicamentoHasUso.class);
            q.setParameter("medicamento", medicamento);
            lista = Optional.ofNullable(q.getResultList());
        } catch (Exception e) {
        }
        return lista;
    }
    
    public int deleteAllByMedicamento(Medicamento medicamento){
        int result = 0;
        EntityManager em = null;
        try {
            
            System.out.println("LLegó a eliminar registros de uso");
            
            em = getEntityManager();
            em.getTransaction().begin();
            String query = "DELETE FROM MedicamentoHasUso mhu "
                    + "WHERE mhu.medicamentoId =:medicamento";
            Query q = em.createQuery(query);
            q.setParameter("medicamento", medicamento);
            result = q.executeUpdate();
            em.getTransaction().commit();
            
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        } finally {
            if(em != null){
                em.close();
            }
        }
        return result;
    }
}
