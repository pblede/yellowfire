package za.co.yellowfire.manager;

import za.co.yellowfire.domain.ChangeItem;
import za.co.yellowfire.domain.DomainObject;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local
public interface DomainManager extends Serializable {

    /**
     * Persists the object
     * @param object The object to persist
     */
    void persist(Object object);

    /**
     * Merges the object to the persistent store
     * @param object The object to merge
     */
    Object merge(Object object);

    /**
     * Merges the change items for a stored domain object to the persistent store
     * @param domainClass   The class of the domain object to merge
     * @param id            The id of the domain object in the persistent store
     * @param changeItems   The list of change items to apply to the object in the persistent store
     */
    Object merge(Class domainClass, Serializable id, Collection<ChangeItem> changeItems);

    /**
     * Removes the object from the persistent store
     * @param object The object to remove
     */
    void remove(DomainObject object);

        /**
     *
     * @param domainClass The domain class to find
     * @param id The id of the domain object
     * @return Object
     */
    Object find(Class domainClass, Serializable id);

    /**
     *
     * @param domainClass The domain class to find
     * @param id The id of the domain object
     * @param lockMode The lock mode
     * @return Object
     */
    Object find(Class domainClass, Serializable id, LockModeType lockMode);

    /**
     * Queries the manager.
     * @param namedQuery The named query
     * @param parameters The parameters for the query. Name-Value pair that matches the parameters in the named query
     * @param hints The query hints. For example QueryHints.REFRESH which refreshes the L2 cache
     * @return ScrollableCursor if the QueryHints.SCROLLABLE_CURSOR is used as a hint else a List<domainClass>
     */
    Object query(String namedQuery, Map<String, Object> parameters, DomainQueryHint...hints);
}
