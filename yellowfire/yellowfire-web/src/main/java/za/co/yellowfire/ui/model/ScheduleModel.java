package za.co.yellowfire.ui.model;

import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class ScheduleModel<T extends ScheduleEvent> implements Serializable {
    private static final long serialVersionUID = 1L;

    private LazyScheduleModel model;

}
