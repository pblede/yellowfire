package za.co.yellowfire.ui.model;

import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.common.log.LogType;

import java.util.Date;
import java.util.List;


/**
 * 
 * @author Mark Ashworth
 * @version 0.0.1
 */
public abstract class AbstractScheduleModel<T extends ScheduleEvent> extends LazyScheduleModel {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MODEL.getCategory());

    public abstract List<T> onLoadEvents(Date start, Date end);

	@Override public void loadEvents(Date start, Date end) {
		clear();

        List<T> events = onLoadEvents(start, end);
        for (T event : events) {
            addEvent(event);
        }
	}
}
