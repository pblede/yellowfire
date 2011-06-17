package za.co.yellowfire.domain.notification;

import javax.ejb.*;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
/*
@MessageDriven(
        name = "NotificationProcessor",
        messageListenerInterface = MessageListener.class,
        mappedName = "yellowfire.jms.queue.Notification",
        activationConfig = {
            @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
        }
)
*/
public class NotificationProcessor implements MessageListener {

    @EJB(name = "za.co.yellowfire.domain.notification.EmailSender")
    private EmailSender sender;

    @Override
    @TransactionAttribute
    public void onMessage(Message message) {

        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage om = (ObjectMessage) message;
                Notification notification = (Notification) om.getObject();
                sender.send(notification);
            }
        } catch (Exception e) {
            throw new EJBException("Unable to process notification", e);
        }
    }
}
