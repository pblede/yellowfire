package za.co.yellowfire.domain.notification;

import za.co.yellowfire.Naming;
import za.co.yellowfire.manager.DomainManager;

import javax.ejb.*;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@MessageDriven(
        name = "NotificationProcessor",
        messageListenerInterface = MessageListener.class,
        mappedName = "yellowfire.jms.queue.notification",
        activationConfig = {
            @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
        }
)
public class NotificationProcessor implements MessageListener {

    protected EmailSender getEmailSender() throws NamingException {
		return (EmailSender) new InitialContext().lookup(Naming.EMAIL_SENDER);
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void onMessage(Message message) {
        System.out.println("message = " + message);
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage om = (ObjectMessage) message;
                Notification notification = (Notification) om.getObject();
                getEmailSender().send(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
