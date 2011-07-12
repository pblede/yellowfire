package za.co.yellowfire.ui.model;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private RequestResult result = new RequestResult();

    private transient ActionListener saveActionListener;
    private transient ActionListener deleteActionListener;

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

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public DataTable getTable() {
        return table;
    }

    public void setTable(DataTable table) {
        this.table = table;
    }
    
    public List<DataTableRow<T>> getRows() {

        if (this.rows == null && this.listener != null) {
            try {
                this.rows = this.listener.onLoad(null);
                if (this.rows != null) {
                    Collections.sort(rows);
                }
            } catch (DataTableException e) {
                e.printStackTrace();
            }
        }

        if (this.rows == null) {
            this.rows = new ArrayList<DataTableRow<T>>(0);
        }
        
        return rows;
    }

    public void setRows(List<DataTableRow<T>> rows) {
        this.rows = rows;
    }

    public DataTableRow<T> getSelected() {
        LOGGER.debug("getSelected() : {}", selected);
        return selected;
    }

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
        } catch (Throwable e) {
            result.failed(e.getMessage());

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (context != null) {
            context.addCallbackParam("result", result);
        } else {
            LOGGER.warn("Cannot set PrimeFaces result because context is null");
        }
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
        } catch (Throwable e) {
            result.failed(e.getMessage());

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("result", result);
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
        } catch (Throwable e) {
            result.failed(e.getMessage());

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("result", result);
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
        } catch (Throwable e) {
            this.selected.getResult().failed(e.getMessage());

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("result", this.selected.getResult());
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

        this.result.reset();
        try {
            this.rows = this.searchListener.onSearch(event, getSearchText());
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
            this.result.failed(e.getMessage());

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("result", this.result);
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
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
        }
    }

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

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("result", this.selected.getResult());
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
