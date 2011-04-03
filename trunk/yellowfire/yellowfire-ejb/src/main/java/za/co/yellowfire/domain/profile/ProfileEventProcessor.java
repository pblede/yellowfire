package za.co.yellowfire.domain.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.EmailSender;
import za.co.yellowfire.log.LogType;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
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

    @EJB
    private EmailSender sender;

    public void onRegistration(@Observes @Registered User user) {
        /* The user has registered, send out a email verification email*/
        LOGGER.info("onRegistration : " + user);
        try {
            sender.send("Please click on <a href='http://localhost:8080/bluefire/verify?key=" + user.getVerificationKey() + "'></a> to verify your email account.");
        } catch (Exception e) {
            LOGGER.error("Unable to send registration email", e);
        }
	}

    public void onVerification(@Observes @Verified User user) {
        /* The user's email has been verified, enable the profile */
        LOGGER.info("onVerification : " + user);
    }

    public void onLogin(@Observes @Authenticated User user) {
		LOGGER.info("onLogin : " + user);
	}

    public void onLogout(@Observes @Guest User user) {
		LOGGER.info("onLogout : " + user);
	}

    public void onAuthenticationFailure(@Observes @AuthenticateFailure AuthenticationFailure failure) {
        LOGGER.info("onAuthenticationFailure : " + failure.getType() + ":" + failure.getUser());
    }
}
