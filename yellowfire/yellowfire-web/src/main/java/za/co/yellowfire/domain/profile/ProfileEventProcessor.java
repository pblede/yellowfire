package za.co.yellowfire.domain.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.notification.Notification;
import za.co.yellowfire.domain.notification.NotificationType;
import za.co.yellowfire.event.InForeground;
import za.co.yellowfire.event.NotifyEvent;
import za.co.yellowfire.log.LogType;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    @Inject @InForeground @NotifyEvent
    private Event<Notification> notificationEvent;

    /**
     * Event sink for user profile registration.
     * A notification email message is sent the user to verify the email address details and to activate the profile
     * within the system.
     * @param user The user that has registered
     */
    @SuppressWarnings("unused")
    public void onRegistration(@Observes @Registered User user) {
        /* The user has registered, send out a email verification email*/
        if (user == null) {
            LOGGER.warn("Profile is null");
            return;
        }
        
        try {
            Notification notification = new Notification();
            notification.setType(NotificationType.Email);
            notification.setFrom("mp.ashworth@gmail.com");
            notification.setTo(user.getEmail());
            notification.setSubject("Registration");
            notification.setBody("Please click on <a href='http://localhost:8080/yellowfire/verify?key=" + user.getVerificationKey() + "'></a> to verify your email account.");
            notificationEvent.fire(notification);
        } catch (Exception e) {
            LOGGER.error("Unable to send notification", e);
        }
	}

    /**
     * Event sink for the user profile verification
     * @param user The user profile that was verified
     */
    @SuppressWarnings("unused")
    public void onVerification(@Observes @Verified User user) {
        /* The user's email has been verified, enable the profile */

    }

    /**
     * Event sink for user profile login
     * @param user The user that has logged on
     */
    @SuppressWarnings("unused")
    public void onLogin(@Observes @Authenticated User user) {

	}

    /**
     * Event sink for user profile logout
     * @param user The user that has logged on
     */
    @SuppressWarnings("unused")
    public void onLogout(@Observes @Guest User user) {

	}

    /**
     * Event sink for user profile authentication failure
     * @param failure The authentication failure
     */
    @SuppressWarnings("unused")
    public void onAuthenticationFailure(@Observes @AuthenticateFailure AuthenticationFailure failure) {

    }
}
