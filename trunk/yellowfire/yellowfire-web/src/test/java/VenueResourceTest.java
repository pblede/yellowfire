import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly.GrizzlyTestContainerFactory;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.domain.training.Venues;
import za.co.yellowfire.training.service.VenueResource;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class VenueResourceTest extends JerseyTest {

    public VenueResourceTest() {
        super(new LowLevelAppDescriptor.Builder(VenueResource.class).
                contextPath("context").
                build());

    }

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyTestContainerFactory();
    }

    //@Test
    public void testGet() {
        WebResource r = resource().path("venue/list");

        Venues venues = r.get(Venues.class);
        for (Venue venue : venues.getVenues()) {
            System.out.println("venue = " + venue);
        }
    }
}
