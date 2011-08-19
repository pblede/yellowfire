package za.co.yellowfire.ui.model;

import za.co.yellowfire.solarflare.SearchManager;

import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public abstract class AbstractSearchManagerDataTableSearchListener<T> implements DataTableSearchListener<T> {

    public abstract SearchManager getManager();
    public abstract Class getModelClass();

    @Override
    @SuppressWarnings("unchecked")
    public List<DataTableRow<T>> onSearch(ActionEvent event, String searchText) throws DataTableException {

        List<DataTableRow<T>> rows = new ArrayList<DataTableRow<T>>(0);

        List<Object> results = getManager().search(getModelClass(), searchText);
        for (Object result : results) {
            rows.add(new DataTableRow<T>((T) result, 1.0f));
        }

//        if (searchText == null) {
//            rows = new ArrayList<DataTableRow<T>>(0);
//        } else {
//            CompassDetachedHits hits = (CompassDetachedHits) getManager().search(searchText);
//            if (hits != null) {
//                rows = new ArrayList<DataTableRow<T>>(hits.getLength());
//                for (CompassHit hit : hits) {
//                    onSearchedRow(hit);
//                    //Add the domain object to the. I know it is not the best to program
//                    //for this exception but unsure how to get the generic that this class is using
//                    try {
//                        rows.add(new DataTableRow<T>((T) hit.getData(), hit.getScore()));
//                    } catch (Exception e) { /*Ignore*/ }
//                }
//            } else {
//                rows = new ArrayList<DataTableRow<T>>(0);
//            }
//        }
        return rows;
    }
}
