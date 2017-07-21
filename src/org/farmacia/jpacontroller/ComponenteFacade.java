package org.farmacia.jpacontroller;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Componente;
import org.farmacia.utils.JpaUtil;

/**
 * Clase que permite realizar las operaciones contra la tabla Componente de la
 * base de datos
 * 
 * @author jgcastillo
 */
public class ComponenteFacade extends AbstractFacade<Componente> {

    public ComponenteFacade() {
        super(Componente.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }
    
    public Optional<Componente> findByNombre(String nombre){
        Optional<Componente> optComponente = Optional.empty();
        try {
            String query = "FROM Componente c WHERE c.nombre = :nombre";
            TypedQuery<Componente> q = getEntityManager()
                    .createQuery(query, Componente.class);
            q.setParameter("nombre", nombre);
            optComponente = Optional.ofNullable(q.getSingleResult());
        } catch (Exception e) {
        }
        return optComponente;
    }
    
    public Optional<List<Componente>> findByPatron(String patron){
        Optional<List<Componente>> optLista = Optional.empty();
        try {
            String query = "FROM Componente c WHERE c.nombre LIKE '%:patron%'";
            TypedQuery<Componente> q = getEntityManager()
                    .createQuery(query, Componente.class);
            q.setParameter("patron", patron);
            optLista = Optional.ofNullable(q.getResultList());
        } catch (Exception e) {
        }
        return optLista;
    }
}
