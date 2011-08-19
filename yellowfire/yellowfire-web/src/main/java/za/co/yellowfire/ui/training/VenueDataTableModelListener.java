package za.co.yellowfire.ui.training;

import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.model.*;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class VenueDataTableModelListener extends AbstractDomainManagerDataTableListener<Venue> {

    private DomainManager manager;
    
    public VenueDataTableModelListener(DomainManager domainManager) {
        this.manager = domainManager;
    }

    @Override
    public DomainManager getManager() {
        return manager;
    }

    @Override
    public String getLoadQuery() {
        return Venue.QRY_VENUES;
    }

    @Override
    public void onSelection(DataTableRow< Venue > row) throws DataTableException {
        //
    }

    @Override
    public Venue createEmpty() {
        return new Venue();
    }
}
