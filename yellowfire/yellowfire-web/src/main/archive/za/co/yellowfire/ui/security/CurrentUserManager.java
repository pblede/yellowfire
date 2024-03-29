package za.co.yellowfire.ui.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.profile.*;
import za.co.yellowfire.domain.training.TrainingCourse;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.FacesUtil;
import za.co.yellowfire.ui.resources.MessageKey;
import za.co.yellowfire.ui.resources.MessageResources;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Stateful @SessionScoped
public class CurrentUserManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

	private Profile user = new Profile();
    
    private boolean loggedIn = false;

    /* Authentication succeeded */
	@Inject @Authenticated private Event<Profile> loginEventSrc;
    /* Authentication failed */
    @Inject @AuthenticateFailure private Event<AuthenticationFailure> authenticateFailureEventSrc;
    /* Logout event */
    @Inject @Guest private Event<Profile> logoutEventSrc;

    /*@deprecated: The view now uses Conversion scope*/
    private TrainingCourse course;

    /* Domain manager */
    @EJB(name = "DomainManager")
	private DomainManager domainManager;
    
    /* User manager*/
	@EJB(name = "UserManager")
	private UserManager userManager;

    /**
     * Produces the current user
     * @return The current user
     */
	@Produces @Authenticated @Named("currentUser")
	public Profile getCurrentUser() {
		return user;
	}

    /**
     * Produces the current course
     * @return Training course
     * @deprecated Should not be used since the conversation scope takes care of this
     */
    @Produces @Default @Named()
    public TrainingCourse getCurrentCourse() {
        return course;
    }

    /**
     * Produces whether the user is currently logged in
     * @return Whether the user is logged in
     */
    @Produces @Named("isLoggedIn")
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Produces the session timeout
     * @return Session timeout value
     */
    @Produces @Named("sessionTimeout")
    public int getSessionTimeout() {
        return 5000;
    }

    /**
     * Produces guest user
     * @return A blank user instance
     */
    @Produces @Guest
    @Named("guestUser")
	public Profile getGuestUser() {
		return new Profile();
	}

    /**
     * Triggered when the user authenticates
     * @param user The user authentication detail
     */
	public void onLogin(Profile user) {
		LOGGER.info("onLogin : " + user);
		this.user = user;
        this.loggedIn = true;
	}

    /**
     * Triggered when the user logs out
     * @param user The user authentication detail
     */
    protected void onLogout(Profile user) {
		LOGGER.info("onLogout : " + user);
		this.user = new Profile();
        this.loggedIn = false;
	}

    /**
     * Triggered when the user authentication fails
     * @param failure The user authentication failure detail
     */
    protected void onAuthenticationFailure(AuthenticationFailure failure) {
        LOGGER.info("onAuthenticationFailure : " + failure.getType() + ":" + failure.getCredential());
        this.user = new Profile();
        this.loggedIn = false;
    }

    /**
     * Triggered when the user edit a course
     * @param course The course detail
     * @deprecated Not being used
     */
    public void onEditTrainingCourse(@Observes TrainingCourse course) {
        LOGGER.info("onEditTrainingCourse : " + course);
        this.course = course;
    }

    public void login(Credentials credential) {
//        try {
//            //Perform programmatic JAAS login
//            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//            request.login(credential.getName(), credential.getPassword());
//        } catch (Exception e) {
//            /* Fire the event that the user authentication failed because of the system */
//            FacesUtil.addWarnUserNotFound();
//            this.authenticateFailureEventSrc.fire(
//                    new AuthenticationFailure(
//                            new User(credential.getName(), credential.getPassword()),
//                            AuthenticationFailureType.Credentials));
//            return;
//        }

		try {
            //Perform a domain login
			Profile u = userManager.login(credential.getUsername(), credential.getPassword());
			if (u != null) {
                /* Fire the event that the user has logged in*/
                this.loginEventSrc.fire(u);
                /* Manually register that the user has logged in*/
                onLogin(u);
			} else {
                /* Fire the event that the user authentication failed */
                this.authenticateFailureEventSrc.fire(new AuthenticationFailure(credential, AuthenticationFailureType.Credentials));
                /* Manually register that the user authentication failed */
                this.onAuthenticationFailure(new AuthenticationFailure(credential, AuthenticationFailureType.Credentials));
            }
		} catch (Exception e) {
            LOGGER.error("Login failure", e);
			FacesUtil.addErrorMessage(MessageResources.MESSAGE(MessageKey.errorUserLogin), e);

            /* Fire the event that the user authentication failed */
            this.authenticateFailureEventSrc.fire(new AuthenticationFailure(credential, AuthenticationFailureType.System));
            /* Manually regsiter that the user authentication failed */
            this.onAuthenticationFailure(new AuthenticationFailure(credential, AuthenticationFailureType.System));
		}
	}
}
