package org.farmacia.jpacontroller;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Inventario;
import org.farmacia.jpamodel.Medicamento;
import org.farmacia.utils.JpaUtil;

/**
 * Clase que permite realizar las operaciones contra la tabla Inventario de la
 * base de datos
 * 
 * @author jgcastillo
 */
public class InventarioFacade extends AbstractFacade<Inventario>{

    public InventarioFacade() {
        super(Inventario.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }

    public Optional<Inventario> findByMedicamento(Medicamento med) {
        Optional<Inventario> invt = Optional.empty();
        try {
            String query = "FROM Inventario inv WHERE inv.medicamentoId = :med";
            TypedQuery<Inventario> q = getEntityManager().createQuery(query, Inventario.class);
            q.setParameter("med", med);
            invt = Optional.ofNullable(q.getSingleResult());
        } catch (Exception e) {
        }
        
        return invt;
    }
}
