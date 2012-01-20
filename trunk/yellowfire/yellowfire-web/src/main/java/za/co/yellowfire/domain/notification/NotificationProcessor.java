package za.co.yellowfire.domain.notification;

import za.co.yellowfire.Naming;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
        mappedName = "java:comp/env/yellowfire/queue/notification",
        activationConfig = {
            @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
            @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:comp/env/yellowfire/queue/notification"),
            @ActivationConfigProperty(propertyName = "clientID", propertyValue = "notification")
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
