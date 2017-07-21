package org.farmacia.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author jgcastillo
 */
public class JpaUtil {
    
    public static EntityManagerFactory getEMF(){
        return Persistence.createEntityManagerFactory("InvFarmaciaPU");
    }
}
