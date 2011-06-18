package za.co.yellowfire.log;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Log;
import org.jboss.seam.solder.logging.MessageLogger;
import org.jboss.seam.solder.messages.Message;
import za.co.yellowfire.domain.notification.Notification;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@MessageLogger
public interface SearchIndexLogger extends DomainLogger {

    static final int InitializeError = 2001;
    static final int CloseError = 2002;
    static final int SendError = 2003;
    static final int QueuedEventIsNull = 2004;
    static final int QueueEventError = 2005;

    static final int QueueEventInfo = 2106;


    @Log(level = Logger.Level.INFO)
    @Message(id = QueueEventInfo, value = "Queue event notification - %s")
    void queueEventInfo(Notification notification);

    @Log(level = Logger.Level.ERROR)
    @Message(id = InitializeError, value = "Unable to initialize JMS - %s")
    void initializationError(Throwable e);

    @Log(level = Logger.Level.ERROR)
    @Message(id = CloseError, value = "Unable to close JMS connection - %s")
    void closeError(Throwable e);

    @Log(level = Logger.Level.ERROR)
    @Message(id = SendError, value = "Unable to send JMS message - %s")
    void sendError(Throwable e);

    @Log(level = Logger.Level.ERROR)
    @Message(id = QueuedEventIsNull, value = "No notification event specified for queue operation")
    void queuedEventIsNull();

    @Log(level = Logger.Level.ERROR)
    @Message(id = QueueEventError, value = "Unable to queue notification event - %")
    void queueEventError(Throwable e);

}