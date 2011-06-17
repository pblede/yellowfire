package za.co.yellowfire.ui;

import org.jboss.seam.solder.logging.Log;
import org.jboss.seam.solder.logging.MessageLogger;
import org.jboss.seam.solder.messages.Message;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@MessageLogger
public interface UILogger {
    @Log @Message("Testing seam logging %s")
    void logTest(String value);
}
