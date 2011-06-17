package za.co.yellowfire.event;

import org.jboss.seam.solder.logging.Category;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.notification.Notification;
import za.co.yellowfire.log.EventLogger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Stateless
public class BackgroundEventSender {
    @Inject @Category("backgroundEventSender")
    private EventLogger logger;
    @Resource(mappedName="yellowfire.jms.cf")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName="yellowfire.jms.queue.notification")
    private Queue notifyEventQueue;
    @Resource(mappedName="yellowfire.jms.queue.solr")
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
            logger.initializationError(ex);
            throw new RuntimeException(ex);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (connection != null)
                connection.close();
        } catch (Exception e) {
            logger.closeError(e);
        }
    }

    public void event(@Observes @InForeground @NotifyEvent Notification event) {
        try {
            ObjectMessage msg = session.createObjectMessage();
            msg.setObject(event);
            notifyProducer.send(msg);
        } catch (JMSException ex) {
            logger.sendError(ex);
            throw new RuntimeException(ex);
        }
    }

    public void event(@Observes @InForeground @IndexEvent DomainEntity object) {
        try {
            ObjectMessage msg = session.createObjectMessage();
            msg.setObject(object);
            solrProducer.send(msg);
        } catch (JMSException ex) {
            logger.sendError(ex);
            throw new RuntimeException(ex);
        }
    }
}