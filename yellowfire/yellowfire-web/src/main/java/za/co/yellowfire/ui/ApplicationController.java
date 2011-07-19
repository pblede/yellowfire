package za.co.yellowfire.ui;

import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.solarflare.SearchManager;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ApplicationScoped @Named("ApplicationController")
public class ApplicationController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private SearchManager searchManager;

    @EJB(name = "DomainManager")
    private DomainManager domainManager;

    @PostConstruct
    public void init() {

        List<Venue> results = (List<Venue>) domainManager.query(Venue.QRY_VENUES, null, null);
        for (Venue result : results) {
            searchManager.onSearchableAdded(result);
        }
    }
}
