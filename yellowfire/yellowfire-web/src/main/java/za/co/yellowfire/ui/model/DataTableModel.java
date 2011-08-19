package za.co.yellowfire.ui.model;

import org.primefaces.component.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.ui.FacesUtil;
import za.co.yellowfire.ui.PrimeFacesUtil;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A model of entities within a data table.
 * 
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class DataTableModel<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MODEL.getCategory());

    private String searchText;
    private List<DataTableRow<T>> rows;
    private DataTableRow<T> selected;
    private transient DataTable table;
    private DataTableSearchListener<T> searchListener;
    private DataTableListener<T> listener;

    private transient ActionListener saveActionListener;
    private transient ActionListener deleteActionListener;

    /**
     * Constructs the data table model
     * @param listener The listener for events initiated by the user
     * @param searchListener The listener for search events
     */
    public DataTableModel(DataTableListener<T> listener, DataTableSearchListener<T> searchListener) {
        this.searchListener = searchListener;
        this.listener = listener;

        if (this.listener != null) {
            this.selected = new DataTableRow<T>(this.listener.createEmpty());
        } else {
            this.selected = new DataTableRow<T>(null);
        }

        this.saveActionListener = new SaveActionListener(this);
        this.deleteActionListener = new DeleteActionListener(this);
    }

    /**
     * The search text
     * @return String
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * Search text
     * @param searchText The new search text
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * Returns the PrimeFaces DataTable if it is bound on the page
     * @return DataTable
     */
    public DataTable getTable() {
        return table;
    }

    /**
     * Sets the PrimeFaces DataTable
     * @param table The new value
     */
    public void setTable(DataTable table) {
        this.table = table;
    }

    /**
     * Returns the rows in the data table
     * @return A list of DataTableRow objects that wrap the actual objects
     */
    public List<DataTableRow<T>> getRows() {

        if (this.rows == null && this.listener != null) {
            try {
                this.rows = this.listener.onLoad(null);
                if (this.rows != null) {
                    Collections.sort(rows);
                }
            } catch (DataTableException e) {
                LOGGER.error("Unable to load the rows from the listener into the data table", e);
            }
        }

        if (this.rows == null) {
            LOGGER.debug("The rows returned from the listener is null, defaulting to an empty list");
            this.rows = new ArrayList<DataTableRow<T>>(0);
        }
        
        return rows;
    }

    /**
     * Sets the rows of the data table
     * @param rows The new list of DataTableRow
     */
    public void setRows(List<DataTableRow<T>> rows) {
        this.rows = rows;
    }

    /**
     * Returns the selected DataTableRow
     * @return DataTableRow
     */
    public DataTableRow<T> getSelected() {
        LOGGER.debug("getSelected() : {}", selected);
        return selected;
    }

    /**
     * Sets the selected DataTableRow
     * @param selected The new selected DataTableRow
     */
    public void setSelected(DataTableRow<T> selected) {
        LOGGER.debug("setSelected() : {}", selected);
        if (selected == null) {
            this.selected = new DataTableRow<T>(this.listener.createEmpty());
        } else {
            this.selected = selected;
            onSelection();
        }
    }

    public ActionListener getSaveActionListener() {
        return saveActionListener;
    }

    public ActionListener getDeleteActionListener() {
        return deleteActionListener;
    }

    /**
     * Triggered when a user selects an item within the data table
     */
    public void onSelection() {
        LOGGER.debug("onSelection()");

        if (this.listener == null) {
            LOGGER.debug("onSelection() : DataTableListener is null, not adding");
            return;
        }

        RequestResult result = new RequestResult();
        try {
            this.listener.onSelection(getSelected());
        } catch (Exception e) {
            result.failed(e.getMessage());
            FacesUtil.addErrorMessage(e.getMessage());
        }
        PrimeFacesUtil.addCallbackParam(result);
    }

    /**
     * Triggered when the user selects an action to add an entity to the data table. This action usually occurs before
     * an entity is displayed to the user for modification.
     * @param event The JSF action event
     */
    public void onAdd(ActionEvent event) {
        LOGGER.debug("onAdd() : {}", event);

        if (this.listener == null) {
            LOGGER.debug("onAdd() : DataTableListener is null, not adding");
            return;
        }

        RequestResult result = new RequestResult();
        try {
            T o = this.listener.onAdd(event);
            setSelected(new DataTableRow<T>(o));
        } catch (Exception e) {
            result.failed(e.getMessage());
            FacesUtil.addErrorMessage(e.getMessage());
        }
        PrimeFacesUtil.addCallbackParam(result);
    }

    /**
     * Triggered when the user selects an action to modify an entity. This action usually occurs before the entity is
     * displayed to the user for modification.
     * @param event The JSF action event
     */
    public void onModify(ActionEvent event) {
        LOGGER.debug("onModify() : " + event);

        if (this.listener == null) {
            LOGGER.debug("onModify() : DataTableListener is null, not modifying");
            return;
        }

        RequestResult result = new RequestResult();
        try {
            T o = this.selected.getObject();
            this.listener.onModify(event, o);
            this.selected.setObject(o);
        } catch (Exception e) {
            result.failed(e.getMessage());
            FacesUtil.addErrorMessage(e.getMessage());
        }
        PrimeFacesUtil.addCallbackParam(result);
    }

    /**
     * Triggered when the user requests the entity to be saved.
     * @param event The JSF action event
     */
    public void onSave(ActionEvent event) {
        LOGGER.debug("onSave() : {}", event);

        if (this.listener == null) {
            LOGGER.debug("onSave() : DataTableListener is null, not saving");
            return;
        }

        try {
            this.selected.getResult().reset();

            T o = this.selected.getObject();
            this.listener.onSave(event, o);

            //Set the rows to null so it'll be refreshed on the next get
            this.rows = null;

            //Reset selection
            this.table.setSelection(null);
            this.table.setRowIndex(-1);
            this.table.reset();
        } catch (Exception e) {
            this.selected.getResult().failed(e.getMessage());
            FacesUtil.addErrorMessage(e.getMessage());
        }
        PrimeFacesUtil.addCallbackParam(this.selected.getResult());
    }

    /**
     * Triggered when the user selects that the changes should be discarded.
     * @param event The JSF action event
     */
    public void onDiscard(ActionEvent event) {
         LOGGER.debug("onDiscard() : {}", event);
    }

    /**
     * Triggered when the user selects to search for an entity
     * @param event The JSF action event
     */
    public void onSearch(ActionEvent event) {
        LOGGER.debug("onSearch() : {}", event);

        if (this.searchListener == null) {
            LOGGER.debug("onSearch() : DataTableSearchListener is null, not searching");
            return;
        }

        RequestResult result = new RequestResult();
        try {
            this.rows = this.searchListener.onSearch(event, getSearchText());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            result.failed(e.getMessage());
            FacesUtil.addErrorMessage(e.getMessage());
        }
        PrimeFacesUtil.addCallbackParam(result);
    }

    /**
     * Triggered by the system when the data table is loaded for the first time or when the user triggers a reload
     * @param event The JSF action event
     */
    public void onLoad(ActionEvent event) {
        LOGGER.debug("onLoad() : {}", event);

        if (this.listener == null) {
            LOGGER.debug("onLoad() : DataTableListener is null, not loading");
            return;
        }

        /* Deselected row */
        this.selected = new DataTableRow<T>(this.listener.createEmpty());

        if (table != null) {
            this.table.setSelection(null);
            this.table.setRowIndex(-1);
            this.table.reset();
        }

        try {
            this.rows = this.listener.onLoad(event);
        } catch (Exception e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
    }

    /**
     * Triggered when the user requests the entity to be deleted.
     * @param event The JSF action event
     */
    public void onDelete(ActionEvent event) {
        LOGGER.debug("onDelete() : {}", event);

        if (this.listener == null) {
            LOGGER.debug("onDelete() : DataTableListener is null, not deleting");
            return;
        }

        try {
            DataTableRow<T> row = getSelected();
            if (row != null && row.getObject() != null) {
                this.listener.onDelete(event, row.getObject());
                this.rows.remove(row);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            this.selected.getResult().failed(e.getMessage());
            FacesUtil.addErrorMessage(e.getMessage());
        }
        PrimeFacesUtil.addCallbackParam(this.selected.getResult());
    }

    private static class SaveActionListener implements ActionListener {
        private DataTableModel model;
        public SaveActionListener(DataTableModel model) {this.model = model;}
        @Override public void processAction(ActionEvent event) throws AbortProcessingException {
            model.onSave(event);
        }
    }

    private static class DeleteActionListener implements ActionListener {
        private DataTableModel model;
        public DeleteActionListener(DataTableModel model) {this.model = model;}
        @Override public void processAction(ActionEvent event) throws AbortProcessingException {
            model.onDelete(event);
        }
    }
}
