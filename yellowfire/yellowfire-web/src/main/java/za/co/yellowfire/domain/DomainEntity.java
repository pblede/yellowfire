package za.co.yellowfire.domain;

import javax.persistence.Column;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

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
}
