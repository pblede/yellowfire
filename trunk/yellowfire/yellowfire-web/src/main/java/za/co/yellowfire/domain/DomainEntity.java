package za.co.yellowfire.domain;

import org.apache.commons.beanutils.*;

import javax.persistence.Column;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * Base entity class
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public abstract class DomainEntity implements DomainObject {
    private static final long serialVersionUID = 1L;
    
    @Column(name = "create_ts")
    private Date created;

    @Column(name = "update_ts")
    private Date updated;

    @Transient
    private Map<String, Object> original;

    /**
     * The timestamp when the entity was created
     * @return Created time
     */
    public Date getCreated() {
        return created;
    }

    /**
     * The timestamp when the entity was created
     * @param created Created time
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * The timestamp when the entity was update
     * @return Updated time
     */
    @SuppressWarnings("unused")
    public Date getUpdated() {
        return updated;
    }

    /**
     * The timestamp when the entity was updated
     * @param updated Updated time
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Map<String, Object> getSearchable() {
        return null;
    }

    /**
     * Sets the created / updated timestamps when the entity is persistened
     */
    @PrePersist
    private void prePersist() {
        Calendar now = Calendar.getInstance();
        setCreated(now.getTime());
        setUpdated(now.getTime());
    }

    /**
     * Sets the created / updated timestamps when the entity is persistened
     */
    @PreUpdate
    private void preUpdate() {
        Calendar now = Calendar.getInstance();
        setUpdated(now.getTime());
    }

    public List<DomainObject> getDomainObjectsInTree() throws ChangeTrackingException {
        try {
            List<DomainObject> objects = new ArrayList<DomainObject>();
            PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(this);
            for (PropertyDescriptor descriptor : descriptors) {
                if (DomainObject.class.isAssignableFrom(descriptor.getPropertyType())) {
                    DomainObject domainObject = (DomainObject) new WrapDynaBean(this).get(descriptor.getName());
                    if (domainObject != null) {
                        objects.add(domainObject);
                    }
                }
            }
            return objects;
        } catch (Exception e) {
            throw new ChangeTrackingException("Unable to request properties of type DomainObject", e);
        }
    }
    
    public String[] getTrackedProperties() {
        return null;
    }
    
    /**
     * Starts tracking changes to the object
     * @throws ChangeTrackingException if describing the current object caused an error
     */
    public void track() throws ChangeTrackingException {
        try {
            if (this.original == null) {
                this.original = new HashMap<String, Object>();
            } else {
                this.original.clear();
            }

            /* Remove properties that should not be tracked */
            Collection<String> ignore = new HashSet<String>();
            ignore.add("id");
            ignore.add("class");
            ignore.add("updated");
            ignore.add("created");

            PropertyUtilsBean util = new PropertyUtilsBean();
            String[] tracked = getTrackedProperties();
            if (tracked != null) {
                for (String property : tracked) {
                    original.put(property, util.getProperty(this, property));
                }
            }

            List<DomainObject> objects = getDomainObjectsInTree();
            for (DomainObject object : objects) {
                if (object != null) {
                    System.out.println("Switching tracking on for object : " + object);
                    object.track();
                }
            }
        } catch (Exception e) {
            throw new ChangeTrackingException("Unable to describe object", e);
        }
    }

    /**
     * Gets the changes to the domain object since the last track() method was called on this object.
     * @return The list of change items
     * @throws ChangeTrackingException if the changes could not be computed
     */
    public List<ChangeItem> changes() throws ChangeTrackingException  {
        try {
            List<ChangeItem> changes = new ArrayList<ChangeItem>();
            PropertyUtilsBean util = new PropertyUtilsBean();
            if (original != null && original.size() > 0) {
                for (String property : original.keySet()) {
                    Object current = util.getProperty(this, property);
                    Object previous = this.original.containsKey(property) ? this.original.get(property) : null;

                    if (((previous != null && !previous.equals(current)) || (current != null && !current.equals(previous)))) {
                        changes.add(new ChangeItem(property, current));
                    }
                }
            }
            return changes;
        } catch (Exception e) {
            throw new ChangeTrackingException("Unable to compute object changes", e);
        }
    }
}
