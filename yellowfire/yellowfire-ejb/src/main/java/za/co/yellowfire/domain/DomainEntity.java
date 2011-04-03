package za.co.yellowfire.domain;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public abstract class DomainEntity implements DomainObject {

    @Column(name = "create_ts")
    private Date created;

    @Column(name = "update_ts")
    private Date updated;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
