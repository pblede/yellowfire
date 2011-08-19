package za.co.yellowfire.ui.model;

import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.List;

/**
 * The data table search listener
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface DataTableSearchListener<T> extends Serializable {
    /**
     * Triggered when the user searches a data table
     * @param event The JSF event
     * @param searchText The search text
     * @return The list of rows that match the search
     * @throws DataTableException If there was an exception
     */
    List<DataTableRow<T>> onSearch(ActionEvent event, String searchText) throws DataTableException;
}
