package za.co.yellowfire.domain.profile;

import org.jboss.seam.solder.logging.Category;
import za.co.yellowfire.domain.notification.EmailSender;
import za.co.yellowfire.domain.notification.Notification;
import za.co.yellowfire.domain.notification.NotificationType;
import za.co.yellowfire.event.InForeground;
import za.co.yellowfire.event.NotifyEvent;
import za.co.yellowfire.log.ProfileLogger;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ApplicationScoped
@Named("ProfileEventProcessor")
public class ProfileEventProcessor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject @Category("profileEventProcessor")
    private ProfileLogger logger;

    //TODO The plan is to use JMS when I can get to be configured correct in Glassfish.
    @Inject @InForeground @NotifyEvent
    private Event<Notification> notificationEvent;

    //TODO Deprecate the call here in time
    @EJB(name = "za.co.yellowfire.domain.notification.EmailSender")
    private EmailSender sender;

    /**
     * Event sink for user profile registration.
     * A notification email message is sent the user to verify the email address details and to activate the profile
     * within the system.
     * @param user The user that has registered
     */
    @SuppressWarnings("unused")
    public void onRegistration(@Observes @Registered User user) {
        /* The user has registered, send out a email verification email*/
        logger.enter("onRegistration", user != null ? user : "null");

        if (user == null) {
            logger.profileIsNull();
            return;
        }
        
        try {
            Notification notification = new Notification();
            notification.setType(NotificationType.Email);
            notification.setFrom("mp.ashworth@gmail.com");
            notification.setTo("mp.ashworth@gmail.com");
            notification.setSubject("Registration");
            notification.setBody("Please click on <a href='http://localhost:8080/bluefire/verify?key=" + user.getVerificationKey() + "'></a> to verify your email account.");
            sender.send(notification);
        } catch (Exception e) {
            logger.registrationNotificationError(e);
        }
	}

    /**
     * Event sink for the user profile verification
     * @param user The user profile that was verified
     */
    @SuppressWarnings("unused")
    public void onVerification(@Observes @Verified User user) {
        /* The user's email has been verified, enable the profile */
        logger.enter("onVerification", user != null ? user : "null");
    }

    /**
     * Event sink for user profile login
     * @param user The user that has logged on
     */
    @SuppressWarnings("unused")
    public void onLogin(@Observes @Authenticated User user) {
		logger.enter("onVerification", user != null ? user : "null");
	}

    /**
     * Event sink for user profile logout
     * @param user The user that has logged on
     */
    @SuppressWarnings("unused")
    public void onLogout(@Observes @Guest User user) {
		logger.enter("onVerification", user != null ? user : "null");
	}

    /**
     * Event sink for user profile authentication failure
     * @param failure The authentication failure
     */
    @SuppressWarnings("unused")
    public void onAuthenticationFailure(@Observes @AuthenticateFailure AuthenticationFailure failure) {
        logger.enter("onVerification", failure != null ? failure.getType() + ":" + failure.getUser() : "null");
    }
}
