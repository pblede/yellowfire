package za.co.yellowfire.ui.model;

import za.co.yellowfire.domain.geocode.GeocodeException;
import za.co.yellowfire.domain.geocode.GeocodeManager;
import za.co.yellowfire.domain.geocode.GeocodeResponse;
import za.co.yellowfire.domain.geocode.GeocodeResult;

import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public abstract class GeocodeManagerDataTableSearchListener implements DataTableSearchListener<GeocodeResult> {

    public abstract GeocodeManager getManager();

    public void onSearchedRow(GeocodeResult result) {}

    /**
     * WARNING: Used by the DataTableModel.onSearch() method and should not be called directly
     * @param event The ActionEvent
     * @param searchText The search text
     * @return List<DataTableRow<GeocodeResult>>
     * @throws DataTableException If there was an error searching
     */
    @Override
    public List<DataTableRow<GeocodeResult>> onSearch(ActionEvent event, String searchText) throws DataTableException {
        try {
            List<DataTableRow<GeocodeResult>> rows = new ArrayList<DataTableRow<GeocodeResult>>();
            GeocodeResponse response = getManager().findAddress(searchText);
            switch (response.getStatus()) {
                case OK:
                    for (GeocodeResult result : response.getResults()) {
                        onSearchedRow(result);
                        rows.add(new DataTableRow(result));
                    }
                    break;
                case INVALID_REQUEST:
                    throw new GeocodeException("Invalid request");
                case REQUEST_DENIED:
                    throw new GeocodeException("Request denied");
                case OVER_QUERY_LIMIT:
                    throw new GeocodeException("Over query limit");
                case ZERO_RESULTS:
                    break;
            }
            return rows;
        } catch (Throwable e) {
            throw new DataTableException("Search failed", e);
        }
    }
}
