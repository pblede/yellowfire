package za.co.yellowfire.domain.notification;

import org.jboss.seam.solder.logging.Category;
import za.co.yellowfire.log.DomainLogger;
import za.co.yellowfire.manager.DomainManager;

import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Future;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Stateless(name = "za.co.yellowfire.domain.notification.EmailSender")
public class EmailSender {

    @Inject @Category("emailSender")
    private DomainLogger logger;

    @Resource(mappedName = "mail/yellowfire.mail")
    private Session session;

    @EJB(name = "DomainManager")
    private DomainManager manager;

    public void queueNotification(Notification notification) {
        
    }

    @Asynchronous
    public Future<Boolean> send(Notification notification) throws MessagingException {
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

        } catch (Exception e) {
            e.printStackTrace();
            return new AsyncResult<Boolean>(false);
        }

        try {
            manager.persist(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new AsyncResult<Boolean>(true);
    }
}