package org.farmacia.jpacontroller;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Presentacion;
import org.farmacia.utils.JpaUtil;

/**
 * Clase que permite realizar las operaciones contra la tabla Presentacion de la
 * base de datos
 * 
 * @author jgcastillo
 */
public class PresentacionFacade extends AbstractFacade<Presentacion> {

    public PresentacionFacade() {
        super(Presentacion.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }
    
    public Optional<Presentacion> findByNombre(String nombre){
        Optional<Presentacion> optPresentacion = Optional.empty();
        try {
            String query = "FROM Presentacion p WHERE p.nombre = :nombre";
            TypedQuery<Presentacion> q = getEntityManager()
                    .createQuery(query, Presentacion.class);
            q.setParameter("nombre", nombre);
            optPresentacion = Optional.ofNullable(q.getSingleResult());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return optPresentacion;
    }
    
}
