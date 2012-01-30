package za.co.yellowfire.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.setup.Manifest;
import za.co.yellowfire.setup.SetupManager;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ApplicationScoped @Named("applicationManager") @Startup @Singleton
public class ApplicationManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    @EJB
    private SetupManager setupManager;

    private Manifest manifest;
    private boolean setupRequired;

//    @Inject
//    private SearchManager searchManager;
//
//    @EJB
//    private DomainManager domainManager;
    
    /**
     * Indicates that setup of the application required by the administrator. The user interface
     * should redirect the user to the setup screens
     * @return true if setup is required else false
     */
    public boolean isSetupRequired() {
        return setupRequired;
    }

    public String getVersion() {
        return this.manifest.getVersion();
    }
    
    @PostConstruct
    public void init() {
        try {
            this.manifest = setupManager.getManifest();
            this.setupRequired = setupManager.isSetupRequired(this.manifest);
        } catch (SQLException e) {
            LOGGER.warn("Unable to read whether setup is required", e);
        }
    }

    //@PostConstruct
    //public void init() {
    //    LOGGER.info("Indexing venues");
    //    index(Venue.QRY_VENUES);
    //    LOGGER.info("Indexing notifications");
    //    index(Notification.QRY_NOTIFICATIONS);
    //}

//    @Asynchronous
//    public void index(String query) {
//        searchManager.onSearchablesAdded ((List<Object>) domainManager.query(query, null, null));
//    }
}
