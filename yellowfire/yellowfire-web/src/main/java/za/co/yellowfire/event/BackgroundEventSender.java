package za.co.yellowfire.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.Naming;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.notification.Notification;
import za.co.yellowfire.log.LogType;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.jms.*;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Stateless
public class BackgroundEventSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    @Resource(mappedName= Naming.CF_JNDI)
    private ConnectionFactory connectionFactory;
    @Resource(mappedName=Naming.QUEUE_NOTIFICATION_JNDI)
    private Queue notifyEventQueue;
    @Resource(mappedName=Naming.QUEUE_SOLR_JNDI)
    private Queue solrEventQueue;

    private Connection connection;
    private Session session;

    private MessageProducer notifyProducer;
    private MessageProducer solrProducer;

    @PostConstruct
    public void init() {
        try {
            connection = connectionFactory.createConnection();
            session=connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            notifyProducer=session.createProducer(notifyEventQueue);
            solrProducer=session.createProducer(solrEventQueue);
        } catch (JMSException ex) {
            LOGGER.error("Error initializing", ex);
            throw new RuntimeException(ex);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (connection != null)
                connection.close();
        } catch (Exception e) {
            LOGGER.error("Error closing connection", e);
        }
    }

    public void event(@Observes @InForeground @NotifyEvent Notification event) {
        try {
            ObjectMessage msg = session.createObjectMessage();
            msg.setObject(event);
            notifyProducer.send(msg);
        } catch (JMSException ex) {
            LOGGER.error("Error producing JMS message", ex);
            throw new RuntimeException(ex);
        }
    }

    public void event(@Observes @InForeground @IndexEvent DomainEntity object) {
        try {
            ObjectMessage msg = session.createObjectMessage();
            msg.setObject(object);
            solrProducer.send(msg);
        } catch (JMSException ex) {
            LOGGER.error("Error producing JMS message", ex);
            throw new RuntimeException(ex);
        }
    }
}