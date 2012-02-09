package za.co.yellowfire.manager;

import liquibase.exception.LiquibaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.setup.Manifest;
import za.co.yellowfire.setup.SetupManager;
import za.co.yellowfire.setup.Version;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Named
@ApplicationScoped
public class ApplicationManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    @Inject
    private SetupManager setupManager;

    private Manifest manifest;
    private boolean setupRequired;
    private static final Object lock = new Object();

    @PostConstruct
    public void init() {
        try {
            this.manifest = setupManager.getManifest();
            this.setupRequired = setupManager.isSetupRequired(this.manifest);
            
            if (this.manifest.getDatabaseVersion() == null || this.manifest.getDatabaseVersion().equals(new Version(0,0,0))) {
                setupManager.executeInitial();
            }
        } catch (LiquibaseException e) {
            LOGGER.error("Unable to setup database to the initial version", e);
        } catch (SQLException e) {
            LOGGER.warn("Unable to read whether setup is required", e);
        }
    }

    /**
     * Indicates that setup of the application required by the administrator. The user interface
     * should redirect the user to the setup screens
     * @return true if setup is required else false
     */
    public boolean isSetupRequired() {
        return setupRequired;
    }

    public String getVersion() {
        if (this.manifest == null) {
            synchronized (lock) {
                if (this.manifest ==null) {
                    init();
                }
            }
        }
        return this.manifest.getVersion();
    }
}
