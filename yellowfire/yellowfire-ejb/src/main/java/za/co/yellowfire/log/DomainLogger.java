package za.co.yellowfire.log;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Log;
import org.jboss.seam.solder.logging.MessageLogger;
import org.jboss.seam.solder.messages.Message;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@MessageLogger
public interface DomainLogger {

    static final int PersistError = 0001;

    @Log(level = Logger.Level.INFO)
    @Message("%s : %s")
    void enter(String methodName, Object params);

    @Log(level = Logger.Level.INFO)
    @Message("%s")
    void exit(String methodName);

    @Log(level = Logger.Level.ERROR)
    @Message(id = PersistError, value = "Unable to persist - %s")
    void persistError(Throwable e);
}