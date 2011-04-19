package za.co.yellowfire.provider;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Provides the entity manager to enterprise beans
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class EntityManagerProvider {

    @PersistenceContext(unitName="yellowfire")
    private EntityManager em;

    @Produces
    public EntityManager getEntityManager() {
        return em;    
    }
}
