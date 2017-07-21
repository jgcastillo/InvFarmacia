package org.farmacia.jpacontroller;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Uso;
import org.farmacia.utils.JpaUtil;

/**
 * Clase que permite realizar las operaciones contra la tabla Uso de la
 * base de datos
 * 
 * @author jgcastillo
 */
public class UsoFacade extends AbstractFacade<Uso> {

    public UsoFacade() {
        super(Uso.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }
    
    public Optional<Uso> findByNombre(String nombre){
        Optional<Uso> optUso = Optional.empty();
        try {
            String query = "FROM Uso u WHERE u.nombreUso = :nombre";
            TypedQuery<Uso> q = getEntityManager().createQuery(query, Uso.class);
            q.setParameter("nombre", nombre);
            optUso = Optional.ofNullable(q.getSingleResult());
        } catch (Exception e) {
        }
        return optUso;
    }
}
