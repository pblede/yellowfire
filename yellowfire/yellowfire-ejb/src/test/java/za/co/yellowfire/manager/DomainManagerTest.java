package za.co.yellowfire.manager;

import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import za.co.yellowfire.domain.Venue;

import javax.annotation.sql.DataSourceDefinition;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.net.URI;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@DataSourceDefinition(name = "yellowfire.ds",
 className = "com.microsoft.sqlserver.jdbc.SQLServerDriver",
 portNumber = 1433,
 serverName = "localhost",
 databaseName = "race",
 user = "race",
 password = "race"/*,
 properties = {"createDatabaseIfNotExist=true"}*/ 
)
public class DomainManagerTest {
    private static GlassFishRuntime runtime;
    private static GlassFish glassfish;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        GlassFishProperties props = new GlassFishProperties();
        props.setConfigFileURI(new File("C:\\Shared\\Projects.Java\\yellowfire\\yellowfire-ear\\domain.xml").toURI().toString());

        // Define GlassFish and add target/classes as deploy folder
        runtime = GlassFishRuntime.bootstrap();
        glassfish = runtime.newGlassFish(props);
        glassfish.start();
        Deployer deployer = glassfish.getDeployer();
        URI uri = new File("target/classes").toURI();
        deployer.deploy(uri);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Stop GlassFish
        glassfish.stop();
        glassfish.dispose();
    }

    public static DomainManager resolveDomainManager() throws NamingException {
        InitialContext ic = new InitialContext();
        return (DomainManager) ic.lookup("java:global/classes/DomainManager!za.co.yellowfire.manager.DomainManager");
    }

    public static SearchManager resolveSearchManager() throws NamingException {
        InitialContext ic = new InitialContext();
        return (SearchManager) ic.lookup("java:global/classes/SearchManager!za.co.yellowfire.manager.SearchManager");
    }

    @Test
    public void testQueryVenues() {
        try {
            InitialContext ic = new InitialContext();
            DomainManager manager = (DomainManager) ic
                    .lookup("java:global/classes/DomainManager!za.co.yellowfire.manager.DomainManager");

            List<Venue> results = (List<Venue>) manager.query(Venue.QRY_VENUES, null);
            for (Venue venue : results) {
                System.out.println(venue);
            }
        } catch (NamingException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void testQueryVenuesInProximity() {
        try {
            InitialContext ic = new InitialContext();
            DomainManager manager = (DomainManager) ic
                    .lookup("java:global/classes/DomainManager!za.co.yellowfire.manager.DomainManager");

            List<Venue> results = (List<Venue>) manager.query(Venue.QRY_VENUES_IN_PROXIMITY, Venue.getProximityQueryParams(23, -10, 5), DomainQueryHint.REFRESH);
            for (Venue venue : results) {
                System.out.println(venue);
            }
        } catch (NamingException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void testVenueSearch() throws Exception {
//        Venue venue = new Venue("Mark's Test", "9 Test Ave", "23.000000,24.000000");
//
//        DomainManager domain = resolveDomainManager();
//        domain.persist(venue);
//        System.out.println("venue = " + venue);
//
//        SearchManager manager = resolveSearchManager();
//        CompassDetachedHits hits = (CompassDetachedHits) manager.search("Mark's Test");
//        for (CompassHit hit : hits) {
//            System.out.println("***************************");
//            System.out.println("hit = " + hit.getAlias());
//            System.out.println("hit = " + hit.data());
//            System.out.println("hit = " + hit.getScore());
////            System.out.println("hit = " + hit.getHighlightedText().getHighlightedText());
//        }
//
//        domain.remove(venue);
//
//        manager = resolveSearchManager();
//        hits = (CompassDetachedHits) manager.search("9 Mark's Test");
//        for (CompassHit hit : hits) {
//            System.out.println("***************************");
//            System.out.println("hit = " + hit.getAlias());
//            System.out.println("hit = " + hit.data());
//            System.out.println("hit = " + hit.getScore());
////            System.out.println("hit = " + hit.getHighlightedText().getHighlightedText());
//        }

        
    }
}
