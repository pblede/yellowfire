package za.co.yellowfire.log;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Log;
import org.jboss.seam.solder.logging.MessageLogger;
import org.jboss.seam.solder.messages.Message;
import za.co.yellowfire.log.DomainLogger;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@MessageLogger
public interface EventLogger extends DomainLogger {

    static final int InitializeError = 2001;
    static final int CloseError = 2002;
    static final int SendError = 2003;

    @Log(level = Logger.Level.ERROR)
    @Message(id = InitializeError, value = "Unable to initialize JMS - %s")
    void initializationError(Throwable e);

    @Log(level = Logger.Level.ERROR)
    @Message(id = CloseError, value = "Unable to close JMS connection - %s")
    void closeError(Throwable e);

    @Log(level = Logger.Level.ERROR)
    @Message(id = SendError, value = "Unable to send JMS message - %s")
    void sendError(Throwable e);
}