package za.co.yellowfire.domain.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local(EmailSender.class)
@Remote(EmailSenderRemote.class)
@Stateless(
        name = "EmailSender",
        mappedName = "yellowfire/session/EmailSender"
)
public class EmailSenderBean implements EmailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    //@Resource(mappedName = "mail/yellowfire.exchange.mail")
    private Session session;

    @EJB(name = "DomainManager")
    private DomainManager manager;

    /**
     * Queues the notification
     * @param notification The notification to queue
     */
    public void queue(Notification notification) {
        LOGGER.info("Queuing notification {} ", notification);

        if (notification == null) {
            LOGGER.warn("Notification is null");
            return;
        }
        try {
            notification.setSent(null);
            manager.persist(notification);
        } catch (Exception e) {
            LOGGER.error("Unable to persist notification", e);
        }
    }

    /**
     * Sends the notification
     * @param notification The notification
     * @throws MessagingException If there was an error
     */
    public void send(Notification notification) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(notification.getFrom()));

            for (String to : notification.getToRecipients()) {
                message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
            }
            for (String cc : notification.getCcRecipients()) {
                message.addRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(cc));
            }
            for (String bcc : notification.getBccRecipients()) {
                message.addRecipient(javax.mail.Message.RecipientType.BCC, new InternetAddress(bcc));
            }

            message.setSubject(notification.getSubject());
            message.setText(notification.getBody());
            Transport.send(message);

            notification.setSent(new Date());
            manager.persist(notification);
        } catch (MessagingException e) {
            LOGGER.error("Unable to send message", e);
            queue(notification);
        } catch (Exception e) {
            LOGGER.error("Unable to send message", e);
        }
    }
}