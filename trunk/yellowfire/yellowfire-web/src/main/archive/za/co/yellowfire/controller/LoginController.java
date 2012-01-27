package za.co.yellowfire.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.profile.*;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.ui.resources.MessageResources;
import za.co.yellowfire.ui.security.CurrentUserManager;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Mark Ashworth
 * @version 0.0.1
 */
@SessionScoped
@Named("loginController")
public class LoginController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

	public static final String NAME = "loginController";

    private Credentials credential = new Credentials();

	/* Login event */
	@Inject @Authenticated private Event<Profile> loginEventSrc;
    @Inject @AuthenticateFailure private Event<AuthenticationFailure> authenticateFailureEventSrc;
    /* Logout event */
    @Inject @Guest private Event<Profile> logoutEventSrc;
    /* User manager*/
	@EJB(name = "UserManager")
	private UserManager manager;

    /* Current User manager*/
    @Inject
    private CurrentUserManager currentUserManager;

    public Credentials getCredential() {
        return credential;
    }

	public void logout() {
		/* Fire the event that the user has logged out*/
        this.logoutEventSrc.fire(currentUserManager.getCurrentUser());
		invalidateSession();
	}

    /**
     * @deprecated Use the CurrentUserManager
     */
	public void login() {
        try {
            //Perform programmatic JAAS login
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.login(getCredential().getUsername(), getCredential().getPassword());
        } catch (Exception e) {
            /* Fire the event that the user authentication failed because of the system */
			addWarnMessage(MessageResources.WARNING_USER_NOT_FOUND, "The username or password is incorrect.");
            this.authenticateFailureEventSrc.fire(new AuthenticationFailure(credential, AuthenticationFailureType.Credentials));
            return;
        }

		try {
            //Perform a domain login
			Profile u = manager.login(getCredential().getUsername(), getCredential().getPassword());
			if (u != null) {
                /* Fire the event that the user has logged in*/
                this.loginEventSrc.fire(u);
			} else {
                /* Fire the event that the user authentication failed */
                this.authenticateFailureEventSrc.fire(new AuthenticationFailure(credential, AuthenticationFailureType.Credentials));
            }
		} catch (Exception e) {
            LOGGER.error(NAME + ".login() : ", e);
			addErrorMessage(MessageResources.ERROR_USER_LOGIN, e);

            /* Fire the event that the user authentication failed */
            this.authenticateFailureEventSrc.fire(new AuthenticationFailure(credential, AuthenticationFailureType.System));
		}
	}

	/**
	 * Called by the quick login bar
	 * @return String
	 */
	public String createProfile() {
		return VIEW_REGISTER;
	}
}
