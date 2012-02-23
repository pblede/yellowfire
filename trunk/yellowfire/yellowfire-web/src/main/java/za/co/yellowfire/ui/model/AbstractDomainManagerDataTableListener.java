package za.co.yellowfire.ui.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.manager.DomainManager;

import javax.ejb.EJBException;
import javax.faces.event.ActionEvent;
import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The AbstractDomainManagerDataTableListener is an implementation of DataTableListener that uses the DomainManager
 * to manage the entities within a DataTableModel.
 *
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public abstract class AbstractDomainManagerDataTableListener<T extends DomainObject> implements DataTableListener<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.LISTENER.getCategory());
    private static final long serialVersionUID = 1L;


    /**
     * The domain manager used for the model
     * @return DomainManager
     */
    public abstract DomainManager getManager();

    /**
     * Retrieves the named query that is used to load the model with domain objects.
     * @return String
     */
    public abstract String getLoadQuery();

    /**
     * Retrieves the parameters for the load query.<br />
     * For example: <strong>select r from Race where where r.date >= :date</strong>, then <strong>{"date", new Date()}</strong> should be returned.
     * @return An empty map or null if there are no parameters, else a named value pair of map parameters that match the named query specification.
     */
    public Map<String, Object> getLoadQueryParameters() {
        return null;
    }

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
            /*NOTE: EclipseLink 2.3.x refresh hint causing a rollback of the transaction possible 2012-01-25 */
            Collection<T> values = (List<T>) getManager().query(query, getLoadQueryParameters()/*, DomainQueryHint.REFRESH*/);
            if (values != null) {
                rows = new ArrayList<DataTableRow<T>>(values.size());
                for (T value : values) {
                    //Initialise @Embedded classes
                    onLoadRow(value);
                    //Add the domain object to the
                    rows.add(new DataTableRow<T>(value.getId(), value));
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
                /* Give the application the opportunity to apply business logic and/or save nested objects*/
                object = onSaveNew(event, object);
                /* Persist the root object*/
                getManager().persist(object);
                action = DataTableAction.Add;
            } else if (object.getId() instanceof Long) {
                Long id = (Long) object.getId();
                if (id > 0) {
                    /* Give the application the opportunity to apply business logic and/or save nested objects*/
                    object = onSaveExisting(event, object);
                    /* Merge the root object*/
                    object = (T) getManager().merge(object);
                    action = DataTableAction.Modify;
                } else {
                    /* Give the application the opportunity to apply business logic and/or save nested objects*/
                    object = onSaveNew(event, object);
                    /* Persist the root object*/
                    getManager().persist(object);
                    action = DataTableAction.Add;
                }
            } else {
                /* Give the application the opportunity to apply business logic and/or save nested objects*/
                object = onSaveExisting(event, object);
                /* Merge the root object*/
                object = (T) getManager().merge(object);
                action = DataTableAction.Modify;
            }

            return action;
        } catch (RuntimeException e) {
            if (!(e instanceof EJBException)) LOGGER.error(e.getMessage());

            String error;
            if (e.getCause() != null & e.getCause() instanceof OptimisticLockException) {
                error = "Cannot save the value because it has changed or been deleted since it was last read.";
                throw new DataTableException(error, e.getCause());
            }

            throw new DataTableException("Unable to save object", e);
        } catch (Exception e) {
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
     * Called by the onSave method when the object.id is null so that application specific logic can be applied
     * @param event  The event that initiated the action
     * @param object The object to save
     * @throws za.co.yellowfire.ui.model.DataTableException
     *          If there was an error encountered
     */
    @Override
    public T onSaveNew(ActionEvent event, T object) throws DataTableException {
        return object;
    }

    /**
     * Called by the onSave method when the object.id is not null so that application specific logic can be applied
     * @param event  The event that initiated the action
     * @param object The object to save
     * @throws za.co.yellowfire.ui.model.DataTableException
     *          If there was an error encountered
     */
    @Override
    public T onSaveExisting(ActionEvent event, T object) throws DataTableException {
        return object;
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
        } catch (Exception e) {
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
