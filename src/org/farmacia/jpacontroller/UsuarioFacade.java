package org.farmacia.jpacontroller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.farmacia.jpamodel.Usuario;
import org.farmacia.utils.JpaUtil;

/**
 * Clase que permite realizar las operaciones contra la tabla Usuario de la
 * base de datos
 * @author jgcastillo
 */
public class UsuarioFacade extends AbstractFacade<Usuario>{

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return JpaUtil.getEMF().createEntityManager();
    }
    
    public Usuario findByUserAndPsw(String username, String password){
        Usuario usuario = null;
        try {
            String query = "FROM Usuario u WHERE u.username = :username "
                    + "AND u.password = :password";
            TypedQuery<Usuario> q = getEntityManager().createQuery(query, Usuario.class);
            q.setParameter("username", username);
            q.setParameter("password", password);
            usuario = q.getSingleResult();
        } catch (Exception e) {
        }
        return usuario;
    }
}
