package za.co.yellowfire.manager;

import org.eclipse.persistence.config.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.log.LogType;

import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local(DomainManager.class)
@Remote(DomainManagerRemote.class)
@Stateless(name = "DomainManager", mappedName = "yellowfire/session/DomainManager")
public class DomainManagerBean implements DomainManager, DomainManagerRemote {
    private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    @PersistenceContext(unitName="yellowfire")
    private EntityManager em;

    @EJB(name = "SearchManager")
    private SearchManager search;

    @Inject @SearchableAdded
    private Event<DomainObject> searchableAddedEvent;

    @Inject @SearchableRemoved
    private Event<DomainObject> searchableRemovedEvent;

    /**
     * Persists the object
     * @param object The object to persist
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override public void persist(Object object) {
        em.persist(object);
        //searchableAddedEvent.fire((DomainObject) object);
    }

    /**
     * Merges the object to the persistent store
     * @param object The object to merge
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override public Object merge(Object object) {
        Object o = em.merge(object);
        //searchableAddedEvent.fire((DomainObject) object);
        return o;
    }

    /**
     * Removes the object from the persistent store
     * @param object The object to remove
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override public void remove(DomainObject object) {
        if (object != null) {
            Object o = em.find(object.getClass(), object.getId());
            if (o != null) {
                em.remove(o);
                //searchableRemovedEvent.fire((DomainObject) object);
            }
        }
    }


    /**
     *
     * @param domainClass The domain class to find
     * @param id The id of the domain object
     * @return Object
     */
    @Override
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Object find(Class domainClass, Serializable id) {
        LOGGER.debug("lookup() : domainClass = " + domainClass.getCanonicalName() + " : id = " + id);
        return em.find(domainClass, id);
    }

    /**
     *
     * @param domainClass The domain class to find
     * @param id The id of the domain object
     * @param lockMode The lock mode
     * @return Object
     */
    @Override
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Object find(Class domainClass, Serializable id, LockModeType lockMode) {
        LOGGER.debug("lookup() : domainClass = " + domainClass.getCanonicalName() + " : id = " + id  + " : lockMode = " + lockMode);
        return em.find(domainClass, id, lockMode);
    }

    /**
     * Queries the manager.
     * @param namedQuery The named query
     * @param parameters The parameters for the query. Name-Value pair that matches the parameters in the named query
     * @param hints The query hints. For example QueryHints.REFRESH which refreshes the L2 cache
     * @return ScrollableCursor if the QueryHints.SCROLLABLE_CURSOR is used as a hint else a List<domainClass>
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @Override public Object query(String namedQuery, Map<String, Object> parameters, DomainQueryHint...hints) {
        LOGGER.debug("query() : namedQuery = " + namedQuery + " : parameters = " + parameters);
        
        Query q = em.createNamedQuery(namedQuery);

        if (parameters != null) {
            Set<String> names = parameters.keySet();
            for(String name : names) {
                Object value = parameters.get(name);
                q.setParameter(name, value);
            }
        }

        /* Set the query hints */
        if (hints != null) {
            for (DomainQueryHint hint : hints) {
                q.setHint(hint.getHint(), hint.getValue());
            }
        }

        /* Determine from the hints if a Scrollable cursor must be returned*/
        Map<String, Object> h = q.getHints();
        if (h != null) {
            if (h.containsKey(QueryHints.SCROLLABLE_CURSOR)) {
                return q.getSingleResult();
            }
        }

        return q.getResultList();
    }
}
