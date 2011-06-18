package za.co.yellowfire.domain.notification;

import org.jboss.seam.solder.logging.Category;
import za.co.yellowfire.log.EventLogger;
import za.co.yellowfire.manager.DomainManager;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
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

    @Inject @Category("emailSender")
    private EventLogger logger;

    @Resource(mappedName = "mail/yellowfire.mail")
    private Session session;

    @EJB(name = "DomainManager")
    private DomainManager manager;

    /**
     * Queues the notification
     * @param notification The notification to queue
     */
    public void queue(Notification notification) {
        logger.queueEventInfo(notification);

        if (notification == null) {
            logger.queuedEventIsNull();
            return;
        }
        try {
            notification.setSent(null);
            manager.persist(notification);
        } catch (Exception e) {
            logger.queueEventError(e);
        }
    }

    /**
     * Sends the notification
     * @param notification The notification
     * @throws MessagingException If there was an error
     */
    public void send(Notification notification) {
        logger.enter("send", notification);

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
            logger.sendError(e);
            queue(notification);
        } catch (Exception e) {
            logger.sendError(e);
        }
    }
}