package za.co.yellowfire;

/**
 *
 * @author Mark P Ashworth
 * @version 2.0.0
 */
public class Naming {

    public static final String PERSISTENCE_UNIT_BLUEFIRE = "yellowfire.persistence";
    public static final String PERSISTENCE_SESSION_BLUEFIRE = "yellowfire";

    public static final String APP_DATA_SOURCE_JNDI = "java:comp/env/yellowfire/app/ds";
    public static final String SETUP_DATA_SOURCE_JNDI = "java:comp/env/yellowfire/setup/ds";

    public static final String MANAGER_DOMAIN = "yellowfire/session/DomainManager";
    public static final String MANAGER_DOMAIN_JNDI_ENC = "java:comp/env/" + MANAGER_DOMAIN;

    public static final String EMAIL_SENDER = "yellowfire/session/EmailSender";

    public static final String MANAGER_RACE = "yellowfire/session/RaceManager";

    public static final String MANAGER_USER = "yellowfire/session/UserManager";
    
    public static final String MANAGER_RESULT = "yellowfire/session/ResultManager";

    public static final String MAIL_JNDI = "mail/yellowfire.exchange.mail";

//    public static final String CF_JNDI = "yellowfire.jms.cf";
    public static final String CF_JNDI = "ConnectionFactory";
    public static final String CF_JNDI_ENC = "java:comp/env/" + CF_JNDI;

    public static final String QUEUE_NOTIFICATION_JNDI = "yellowfire/queue/notification";
    public static final String QUEUE_NOTIFICATION_JNDI_JBOSS = "java:/" + QUEUE_NOTIFICATION_JNDI;
    public static final String QUEUE_NOTIFICATION_JNDI_ENC = "java:comp/env/" + QUEUE_NOTIFICATION_JNDI;

    public static final String QUEUE_SOLR_JNDI = "yellowfire/queue/solr";
    public static final String QUEUE_SOLR_JNDI_JBOSS = "java:/" + QUEUE_SOLR_JNDI;
    public static final String QUEUE_SOLR_JNDI_ENC = "java:comp/env/" + QUEUE_SOLR_JNDI;
}
