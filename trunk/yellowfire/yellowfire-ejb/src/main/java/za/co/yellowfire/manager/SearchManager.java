package za.co.yellowfire.manager;

import za.co.yellowfire.domain.DomainObject;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local
public interface SearchManager extends Serializable {

    void onSearchableAdded(DomainObject object);

    void onSearchableRemoved(DomainObject object);
    
    /**
     * Searches for the value in the seatch engine
     * @param domainClass The domain class to search for
     * @param value The value to search
     * @return Unknown
     */
    Object search(Class domainClass, String value);
}
