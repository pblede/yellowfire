package za.co.yellowfire.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface DomainObject extends Serializable {
    Serializable getId();

    /**
     * Starts tracking changes to the object
     * @throws ChangeTrackingException if describing the current object caused an error
     */
    void track() throws ChangeTrackingException;

    /**
     * Gets the changes to the domain object since the last track() method was called on this object.
     * @return The list of change items
     * @throws ChangeTrackingException if the changes could not be computed
     */
    List<ChangeItem> changes() throws ChangeTrackingException;
}
