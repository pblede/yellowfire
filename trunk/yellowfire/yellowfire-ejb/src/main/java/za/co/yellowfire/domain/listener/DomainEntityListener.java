package za.co.yellowfire.domain.listener;

import za.co.yellowfire.domain.DomainEntity;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Calendar;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class DomainEntityListener {
    @PrePersist
    public void prePersist(DomainEntity entity) {
        Calendar now = Calendar.getInstance();
        entity.setCreated(now.getTime());
        entity.setUpdated(now.getTime());
    }

    @PreUpdate
    public void preUpdate(DomainEntity entity) {
        Calendar now = Calendar.getInstance();
        entity.setUpdated(now.getTime());
    }
}
