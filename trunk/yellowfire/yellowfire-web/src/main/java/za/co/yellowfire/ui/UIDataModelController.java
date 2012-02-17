package za.co.yellowfire.ui;

import za.co.yellowfire.domain.training.Outcome;
import za.co.yellowfire.ui.model.DataTableModel;

/**
 * Specifies the interface for controllers that have a data model
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface UIDataModelController<T> {
    /**
     * The data model of the controller
     * @return The data model
     */
    DataTableModel<T> getDataModel();
}
