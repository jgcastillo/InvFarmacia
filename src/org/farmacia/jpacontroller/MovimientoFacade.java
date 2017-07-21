package org.farmacia.jpacontroller;

import javax.persistence.EntityManager;
import org.farmacia.jpamodel.Movimiento;
import org.farmacia.utils.JpaUtil;

/**
 * Clase que permite realizar las operaciones contra la tabla Movimiento de la
 * base de datos
 *
 * @author jgcastillo
 */
public class MovimientoFacade extends AbstractFacade<Movimiento> {

    public MovimientoFacade() {
        super(Movimiento.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }

}
