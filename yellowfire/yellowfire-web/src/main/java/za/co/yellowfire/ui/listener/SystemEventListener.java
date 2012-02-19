package za.co.yellowfire.ui.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.common.log.LogType;

import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.SystemEvent;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class SystemEventListener implements javax.faces.event.SystemEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.LISTENER.getCategory());

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {


        ExceptionQueuedEvent e = (ExceptionQueuedEvent) event;
        UIComponent component = e.getContext().getComponent();
        Throwable exception = e.getContext().getException();

        LOGGER.info("#################################");
        LOGGER.info("SystemEventListener.processEvent : {}", component);
        LOGGER.info("SystemEventListener.processEvent : {}", exception);
        LOGGER.info("#################################");
    }

    @Override
    public boolean isListenerForSource(Object o) {
        return (o instanceof ExceptionQueuedEvent);
    }
}
