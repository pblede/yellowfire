package za.co.yellowfire.ui;

import org.jboss.seam.solder.logging.Log;
import org.jboss.seam.solder.logging.MessageLogger;
import org.jboss.seam.solder.messages.Message;

/**
 * Created by IntelliJ IDEA.
 * User: MarkA
 * Date: 2011/06/12
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
@MessageLogger
public interface UILogger {
    @Log @Message("Testing seam logging %s")
    void logTest(String value);
}
