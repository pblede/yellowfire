package za.co.yellowfire.ui.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.manager.DomainQueryHint;

import javax.ejb.EJBException;
import javax.faces.event.ActionEvent;
import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public abstract class AbstractDomainManagerDataTableListener<T extends DomainObject> implements DataTableListener<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.LISTENER.getCategory());
    private static final long serialVersionUID = 1L;


    public abstract DomainManager getManager();

    public abstract String getLoadQuery();

    /**
     * Used to initialize the domain object while the loading from the persistent store
     * @param object The domain object
     */
    @SuppressWarnings("unused")
    public void onLoadRow(T object) { }

    /**
     * Used by the datatable model to load
     * @param event The action event that initiated the load
     * @return List
     * @throws za.co.yellowfire.ui.model.DataTableException
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<DataTableRow<T>> onLoad(ActionEvent event) throws DataTableException {
        //if (getLoadQuery() == null) throw new DataTableException("The load query is null");
        //if (getLoadQuery().trim().equals("")) throw new DataTableException("The load query is empty");

        List<DataTableRow<T>> rows;

        String query = getLoadQuery();
        if (query == null) {
            rows = new ArrayList<DataTableRow<T>>(0);
        } else {
            List<T> values = (List<T>) getManager().query(query, null, DomainQueryHint.REFRESH);
            if (values != null) {
                rows = new ArrayList<DataTableRow<T>>(values.size());
                for (T value : values) {
                    //Initialise @Embedded classes
                    onLoadRow(value);
                    //Add the domain object to the
                    rows.add(new DataTableRow<T>(value));
                }
            } else {
                rows = new ArrayList<DataTableRow<T>>(0);
            }
        }
        return rows;
    }

    /**
     * Called when a row in the table is selected
     *
     * @param row The selected row
     * @throws za.co.yellowfire.ui.model.DataTableException
     *          If there was an error
     */
    @Override
    public void onSelection(DataTableRow<T> row) throws DataTableException {}

    /**
     * Called before the add function is intiated. The object returned will be used for the edit function.
     *
     * @param event The event that initiated the action
     * @return The blank object that should be used as a template for the add
     * @throws za.co.yellowfire.ui.model.DataTableException
     *          If there was an error encountered
     */
    @Override
    public T onAdd(ActionEvent event) throws DataTableException {
        return createEmpty();
    }

    /**
     * Called before the modify function is intiated. The object returned will be used for the edit function.
     *
     * @param event  The event that initiated the action
     * @param object The object that should be used as a template for the add
     * @throws za.co.yellowfire.ui.model.DataTableException
     *          If there was an error encountered
     */
    @Override
    public void onModify(ActionEvent event, T object) throws DataTableException {
    }

    /**
     * Saves the object to a persistent store
     * @param event The event that initiated the action
     * @param object The object to save
     * @return DataTableAction.Add indicates that the saved object should be added to the table, else it was modified
     * @throws DataTableException If there was an error encountered
     */
    @Override
    public DataTableAction onSave(ActionEvent event, T object) throws DataTableException {
        LOGGER.debug("onSave() : " + event + ":" + object);

        try {
            DataTableAction action;
            if (object.getId() == null) {
                getManager().persist(object);
                action = DataTableAction.Add;
            } else if (object.getId() instanceof Long) {
                Long id = (Long) object.getId();
                if (id > 0) {
                    getManager().merge(object);
                    action = DataTableAction.Modify;
                } else {
                    getManager().persist(object);
                    action = DataTableAction.Add;
                }
            } else {
                getManager().merge(object);
                action = DataTableAction.Modify;
            }

            return action;
        } catch (Throwable e) {
            if (!(e instanceof EJBException)) LOGGER.error(e.getMessage());

            String error;
            if (e.getCause() != null & e.getCause() instanceof OptimisticLockException) {
                error = "Cannot save the value because it has changed or been deleted since it was last read.";
                throw new DataTableException(error, e.getCause());
            }

            throw new DataTableException("Unable to save object", e);
        }
    }

    /**
     * Deletes the object from the persistent store
     *
     * @param event  The event that initiated the action
     * @param object The object to delete
     * @throws za.co.yellowfire.ui.model.DataTableException
     *          If there was an error encountered
     */
    @Override
    public void onDelete(ActionEvent event, T object) throws DataTableException {
        try {
            getManager().remove(object);
        } catch (Throwable e) {
            if (!(e instanceof EJBException)) LOGGER.error(e.getMessage());

            String error;
            if (e.getCause() != null & e.getCause() instanceof OptimisticLockException) {
                error = "Cannot delete the value because it has changed or been deleted since it was last read.";
                throw new DataTableException(error, e.getCause());
            }

            throw new DataTableException("Unable to delete object", e);
        }
    }
}
