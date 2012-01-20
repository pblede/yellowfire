package za.co.yellowfire.ui.model;

import za.co.yellowfire.solarflare.SearchManager;
import za.co.yellowfire.solarflare.SearchQuery;
import za.co.yellowfire.solarflare.SearchResult;

import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class SearchManagerDataTableSearchListener<T> implements DataTableSearchListener<T> {

    /*Search manager */
    private SearchManager searchManager;
    /*Model class*/
    private Class modelClass;
    
    /**
     * Default constructor
     */
    public SearchManagerDataTableSearchListener() {}

    public SearchManagerDataTableSearchListener(SearchManager searchManager, Class modelClass) {
        this.searchManager = searchManager;
        this.modelClass = modelClass;
    }

    /**
     * Returns the search manager that willl be used to fulfill search requests
     * @return SearchManager
     */
    public SearchManager getManager() {
        return this.searchManager;
    }

    /**
     * Returns the domain model class that the search should be returning
     * @return Class
     */
    public Class getModelClass() {
        return this.modelClass;
    }

    /**
     * Performs the search
     * @param event The JSF event The JSE action event
     * @param searchText The search text The search text
     * @return List of data table model rows that satisfy the search
     * @throws DataTableException If there was an exception
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<DataTableRow<T>> onSearch(ActionEvent event, String searchText) throws DataTableException {

        List<DataTableRow<T>> rows = new ArrayList<DataTableRow<T>>(0);

        SearchResult results = getManager().search(new SearchQuery(getModelClass(), searchText));
        if (results != null) {
            for (Object result : results.getResults()) {
                rows.add(new DataTableRow<T>((T) result, 1.0f));
            }
        }
        return rows;
    }
}
