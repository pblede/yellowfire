package za.co.yellowfire.manager;

import org.compass.core.Compass;
import org.compass.core.CompassHits;
import org.compass.core.CompassSearchSession;
import org.compass.core.CompassSession;
import org.eclipse.persistence.jpa.JpaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.log.LogType;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local
@Singleton(name = "SearchManager")
public class SearchManagerBean implements SearchManager {
    private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    @PersistenceContext(unitName="yellowfire")
    private EntityManager em;

    @EJB(name="DomainManager")
    private DomainManager manager;

    @PostConstruct
    public void init() {

        CompassSession session = null;

        try {
            session = getCompass().openSession();

            List<DomainObject> values = (List <DomainObject>) manager.query(Venue.QRY_VENUES, null);
            for (DomainObject value : values) {
                session.save(value);
            }
        } catch (Exception e) {
            try { if (session != null) session.close(); } catch (Exception x) { /*Ignore*/ }

            LOGGER.error("Unable to index existing data in the database", e);
        }
    }

    private Compass getCompass() {
        return org.compass.gps.device.jpa.embedded.eclipselink.EclipseLinkHelper.getCompass(JpaHelper.getEntityManager(em));
    }

    public void onSearchableAdded(@Observes @SearchableAdded DomainObject object) {
		LOGGER.info("onSearchableAdded : {}", object);
        CompassSession session = null;
        try {
            session = getCompass().openSession();
		    session.save(object);
        } finally {
            try {if (session != null) session.close();} catch (Exception e) { /*Ignore*/ }
        }
	}


    public void onSearchableRemoved(@Observes @SearchableRemoved DomainObject object) {
		LOGGER.info("onSearchableRemoved : {}", object);

        CompassSession session = null;
        try {
            session = getCompass().openSession();
		    session.delete(object);
        } finally {
            try {if (session != null)session.close();} catch (Exception e) { /*Ignore*/ }
        }
	}

    @Override
    public Object search(String value) {
        System.out.println("3 = " + 3);
        CompassSearchSession session = null;
        try {
            session = getCompass().openSearchSession();
            System.out.println("4 = " + 4);
            CompassHits hits = session.find(value);
            return hits.detach();
        } finally {
            if (session != null) {
                try {session.close();} catch (Exception e) { /*Ignore*/ }
            }
        }
    }


}
