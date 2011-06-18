package za.co.yellowfire.domain.listener;

import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.manager.SearchManager;
import za.co.yellowfire.manager.SearchManagerBean;
import za.co.yellowfire.manager.SearchableAdded;
import za.co.yellowfire.manager.SearchableRemoved;

import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class SearchIndexListener {

    private SearchManagerBean search = new SearchManagerBean();

//    @Inject
//    @SearchableAdded
//    private Event<DomainObject> searchableAddedEvent;
//
//    @Inject @SearchableRemoved
//    private Event<DomainObject> searchableRemovedEvent;

    @PostPersist
    @PostUpdate
    public void PostPersist(DomainEntity entity) {
        search.onSearchableAdded(entity);
    }

    @PostRemove
    public void PostRemove(DomainEntity entity) {
        search.onSearchableRemoved(entity);
    }
}
