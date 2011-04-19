package za.co.yellowfire.ui.model;

import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface DataTableSearchListener<T> extends Serializable {
    List<DataTableRow<T>> onSearch(ActionEvent event, String searchText) throws DataTableException;
}
