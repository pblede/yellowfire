package za.co.yellowfire.controller;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import za.co.yellowfire.log.LogType;
import za.co.yellowfire.domain.racing.Club;
import za.co.yellowfire.domain.profile.*;

import java.util.List;

/**
 * 
 * @author Mark Ashworth
 * @version 0.0.1
 */
@RequestScoped
@Named("profileController")
public class ProfileController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    private static final String ERROR_USER_PERSIST = "controller.user.persist.error";
    private static final String INFO_USER_PERSISTED = "controller.user.persisted";
    /** Error code: User registration failed */
    private static final String ERROR_USER_REGISTER = "controller.register.error";

    @Inject @Registered
    private Event<User> registrationEventSrc;

    @Inject @Named("currentUser")
    private User user;

    @Inject @Named("runningClubs")
    private List<Club> clubs;

    @EJB(name = "UserManager")
	private UserManager manager;

    public User getUser() {
        return user;
    }

    public List<Club> getClubs() { return clubs; }

    public boolean getUserNameAvailable(String value) throws NamingException {
    	LOGGER.info("getUserNameAvailable() called");
    	return manager.isUsernameAvailable(value);
    }

    public boolean userNameAvailable(FacesContext context, UIComponent component, Object value) {
    	LOGGER.info("userNameAvailable(FacesContext, UIComponent, Object) called");
    	try {
        	return getUserNameAvailable((String) value);
	    } catch (NamingException e) {
			addErrorMessage("Error", e);
			return false;
		}
    }

    public void persist(ActionEvent event) {
		try {
			final String c = user.getPasswordConfirmation();
			this.user = manager.persist(user);
			this.user.setPasswordConfirmation(c);
			addInfoMessage(INFO_USER_PERSISTED, "Update to profile saved");
		} catch (Exception e) {
			addErrorMessage(ERROR_USER_PERSIST, e);
		}
	}

    public String register() {
    	LOGGER.info("register() called");

    	try {
            

    		/* Register the user */
    		manager.register(user);

            /* Send a registration event */
            registrationEventSrc.fire(user);

    		/* Welcome and forward to races */
    		addInfoMessage("Welcome " + user.getFirstName() + user.getLastName(), "An email will be sent to " + user.getEmail() + " for verification.");
    		return VIEW_RACES;
    	} catch (UserRegistrationException e) {
    		LOGGER.error("Registration error", e);
    		addErrorMessage(ERROR_USER_REGISTER, e.getMessage());
    	} catch (Exception e) {
    		LOGGER.error(ERROR_USER_REGISTER, e);
    		addErrorMessage("Error", e);
    	}
    	return null;
    }
}
