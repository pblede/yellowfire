package za.co.yellowfire.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.domain.notification.Notification;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.solarflare.SearchManager;
import za.co.yellowfire.solarflare.annotation.Solr;
import za.co.yellowfire.solarflare.annotation.Url;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ApplicationScoped @Named("ApplicationManager") @Startup @Singleton
public class ApplicationManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());
    
    @Inject
    private SearchManager searchManager;

    @EJB
    private DomainManager domainManager;

    @PostConstruct
    public void init() {
        LOGGER.info("Indexing venues");
        index(Venue.QRY_VENUES);
        LOGGER.info("Indexing notifications");
        index(Notification.QRY_NOTIFICATIONS);
    }

    @Asynchronous
    public void index(String query) {
        searchManager.onSearchablesAdded ((List<Object>) domainManager.query(query, null, null));
    }
}
