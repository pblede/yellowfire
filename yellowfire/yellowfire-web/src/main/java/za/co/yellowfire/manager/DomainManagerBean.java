package za.co.yellowfire.manager;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.config.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.ChangeItem;
import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.domain.profile.Profile;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Collection;
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
     * Merges the change items for a stored domain object to the persistent store
     * @param domainClass   The class of the domain object to merge
     * @param id            The id of the domain object in the persistent store
     * @param changeItems   The list of change items to apply to the object in the persistent store
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override public Object merge(Class domainClass, Serializable id, Collection<ChangeItem> changeItems) {
        Object object = em.find(domainClass, id);
        for (ChangeItem changeItem : changeItems) {
            try {
                BeanUtils.setProperty(object, changeItem.getProperty(), changeItem.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return merge(object);
    }
    
    /**
     * Removes the object from the persistent store
     * @param object The object to remove
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override public void remove(DomainObject object) {
        if (object != null) {
            Object o;
            /* Profile objects are handled differently because of the picketlink User interface that is implemented.  */
            if (object instanceof Profile) {
                o = em.find(object.getClass(), ((Profile) object).getUserId());
            } else {
                o = em.find(object.getClass(), object.getId());
            }
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
