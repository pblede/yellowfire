package za.co.yellowfire.training.service;

import za.co.yellowfire.Naming;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.domain.training.Venues;
import za.co.yellowfire.manager.DomainManager;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.rpc.ServiceException;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Path("/venue")
public class VenueResource {

    @EJB(name = "DomainManager")
    private DomainManager manager;

    protected DomainManager getManager() throws ServiceException {
        if (manager == null) {
            try {
                manager = (DomainManager) new InitialContext().lookup(Naming.MANAGER_DOMAIN);
            } catch (Exception e) {
                throw new ServiceException(e);
            }
        }
        return manager;
    }

    @GET @Path("list") @Produces("text/xml")
    public Venues getVenues() throws ServiceException {
        List<Venue> results = (List<Venue>) getManager().query(Venue.QRY_VENUES, null);
        if (results != null)
            return new Venues(results);

        return new Venues();
    }
}
