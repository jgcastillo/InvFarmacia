package org.farmacia.jpacontroller;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Equivalente;
import org.farmacia.utils.JpaUtil;

/**
 * Clase que permite realizar las operaciones contra la tabla Equivalente de la
 * base de datos
 * 
 * @author jgcastillo
 */
public class EquivalenteFacade extends AbstractFacade<Equivalente> {

    public EquivalenteFacade() {
        super(Equivalente.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }
    
    public Optional<Equivalente> findByNombre(String nombre) {
        Optional<Equivalente> optEquivalente = Optional.empty();
        try {
            String query = "FROM Equivalente e WHERE e.nombre = :nombre";
            TypedQuery<Equivalente> q = getEntityManager()
                    .createQuery(query, Equivalente.class);
            q.setParameter("nombre", nombre);
            optEquivalente = Optional.ofNullable(q.getSingleResult());
        } catch (Exception e) {
        }
        return optEquivalente;
    }

    public Optional<List<Equivalente>> findByPatron(String patron) {
        Optional<List<Equivalente>> optLista = Optional.empty();
        try {
            String query = "FROM Equivalente e WHERE e.nombre LIKE CONCAT('%',?1,'%')";
            TypedQuery<Equivalente> q = getEntityManager()
                    .createQuery(query, Equivalente.class);
            q.setParameter(1, patron);
            optLista = Optional.ofNullable(q.getResultList());
        } catch (Exception e) {
        }
        return optLista;
    }
}
