package za.co.yellowfire.domain.notification;

import javax.ejb.Remote;
import javax.mail.MessagingException;
import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Remote
public interface EmailSenderRemote extends EmailSender {
}
