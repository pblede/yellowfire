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
public interface ProfileLogger extends DomainLogger {

    static final int RegistrationNotificationError = 1001;
    static final int ProfileIsNull = 1002;

    @Log(level = Logger.Level.ERROR)
    @Message(id = RegistrationNotificationError, value = "Unable to send registration email - %s")
    void registrationNotificationError(Throwable e);

    @Log(level = Logger.Level.ERROR)
    @Message(id = ProfileIsNull, value = "No user profile specified")
    void profileIsNull();
}