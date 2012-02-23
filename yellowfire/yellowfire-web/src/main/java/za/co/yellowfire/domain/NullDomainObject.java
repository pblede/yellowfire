package za.co.yellowfire.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class NullDomainObject implements DomainObject {
    @Override
    public Serializable getId() {
        return -1L;
    }

    /**
     * Starts tracking changes to the object
     *
     * @throws za.co.yellowfire.domain.ChangeTrackingException
     *          if describing the current object caused an error
     */
    @Override
    public void track() throws ChangeTrackingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Gets the changes to the domain object since the last track() method was called on this object.
     *
     * @return The list of change items
     * @throws za.co.yellowfire.domain.ChangeTrackingException
     *          if the changes could not be computed
     */
    @Override
    public List<ChangeItem> changes() throws ChangeTrackingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomainObject that = (DomainObject) o;

        return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    public String toString() {
        return "Select...";
    }
}
