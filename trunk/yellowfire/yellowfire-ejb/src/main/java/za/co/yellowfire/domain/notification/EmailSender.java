package za.co.yellowfire.domain.notification;

import javax.ejb.Local;
import javax.mail.MessagingException;
import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local
public interface EmailSender extends Serializable {
    /**
     * Queues the notification
     * @param notification The notification to queue
     */
    void queue(Notification notification);

    /**
     * Sends the notification
     * @param notification The notification
     * @throws MessagingException If there was an error
     */
    void send(Notification notification) throws MessagingException;
}
