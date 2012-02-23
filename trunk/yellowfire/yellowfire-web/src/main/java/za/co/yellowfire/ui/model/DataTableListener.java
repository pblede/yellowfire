package za.co.yellowfire.ui.model;

import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface DataTableListener<T> extends Serializable {
    /**
     * Creates an empty object which is used for new rows
     * @return T
     */
    T createEmpty();

    /**
     * Loads the objects from a persistent store
     * @param event The event that initiated the action
     * @return List<DataTableRow<T>>
     * @throws DataTableException If there was an error encountered
     */
    List<DataTableRow<T>> onLoad(ActionEvent event) throws DataTableException;

    /**
     * Saves the object to a persistent store
     * @param event The event that initiated the action
     * @param object The object to save
     * @return DataTableAction.Add indicates that the saved object should be added to the table, else it was modified
     * @throws DataTableException If there was an error encountered
     */
    DataTableAction onSave(ActionEvent event, T object) throws DataTableException;

    /**
     * Called by the onSave method when the object.id is null so that application specific logic can be applied
     * @param event The event that initiated the action
     * @param object The object to save
     * @throws DataTableException If there was an error encountered
     */
    T onSaveNew(ActionEvent event, T object) throws DataTableException;

    /**
     * Called by the onSave method when the object.id is not null so that application specific logic can be applied
     * @param event The event that initiated the action
     * @param object The object to save
     * @throws DataTableException If there was an error encountered
     */
    T onSaveExisting(ActionEvent event, T object) throws DataTableException;

    /**
     * Deletes the object from the persistent store
     * @param event The event that initiated the action
     * @param object The object to delete
     * @throws DataTableException If there was an error encountered
     */
    void onDelete(ActionEvent event, T object) throws DataTableException;

    /**
     * Called before the add function is intiated. The object returned will be used for the edit function.
     * @param event The event that initiated the action
     * @return The blank object that should be used as a template for the add
     * @throws DataTableException If there was an error encountered
     */
    T onAdd(ActionEvent event) throws DataTableException;

    /**
     * Called before the modify function is intiated. The object returned will be used for the edit function.
     * @param event The event that initiated the action
     * @param object The object that should be used as a template for the add
     * @throws DataTableException If there was an error encountered
     */
    void onModify(ActionEvent event, T object) throws DataTableException;

    /**
     * Called when a row in the table is selected
     * @param row The selected row
     * @throws DataTableException If there was an error
     */
    void onSelection(DataTableRow<T> row) throws DataTableException;
}
