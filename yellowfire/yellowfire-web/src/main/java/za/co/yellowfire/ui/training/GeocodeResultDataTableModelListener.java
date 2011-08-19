package za.co.yellowfire.ui.training;

import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.domain.geocode.GeocodeGeometry;
import za.co.yellowfire.domain.geocode.GeocodeLocation;
import za.co.yellowfire.domain.geocode.GeocodeResult;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.model.AbstractDomainManagerDataTableListener;
import za.co.yellowfire.ui.model.DataTableException;
import za.co.yellowfire.ui.model.DataTableRow;

import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class GeocodeResultDataTableModelListener extends AbstractDomainManagerDataTableListener<GeocodeResult> {

    private DomainManager manager;
    private MapModel mapModel;
    
    public GeocodeResultDataTableModelListener(DomainManager domainManager, MapModel mapModel) {
        this.manager = domainManager;
        this.mapModel = mapModel;
    }

    @Override
    public DomainManager getManager() {
        return manager;
    }

    @Override
    public String getLoadQuery() {
        return null;
    }

    @Override
    public void onSelection(DataTableRow<GeocodeResult> row) throws DataTableException {
        loadProximityVenues(row);
    }

    @Override
    public GeocodeResult createEmpty() {
        GeocodeResult result = new GeocodeResult();
        result.setGeometry(new GeocodeGeometry(new GeocodeLocation("0", "0")));
        return result;
    }

    private void loadProximityVenues(DataTableRow<GeocodeResult> row) {

        if (row != null && row.getObject() != null) {

            GeocodeResult result = row.getObject();
            double lat = Double.parseDouble(result.getGeometry().getLocation().getLatitude());
            double lng = Double.parseDouble(result.getGeometry().getLocation().getLongitude());

            List<Venue> venues = (List<Venue>) manager.query(
                    Venue.QRY_VENUES_IN_PROXIMITY,
                    Venue.getProximityQueryParams(
                            lat,
                            lng,
                            0.000050));

            if (venues != null && venues.size() > 0) {
                for (Venue v : venues) {
                    mapModel.getMarkers().add(
                            new Marker(
                                    new LatLng(v.getGpsLatitude(), v.getGpsLongitude()),
                                    v.getName(),
                                    v));
                }
            }
        }
    }
}
