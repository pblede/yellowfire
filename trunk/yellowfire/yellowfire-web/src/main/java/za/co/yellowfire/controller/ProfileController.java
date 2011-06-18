package za.co.yellowfire.controller;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.profile.Registered;
import za.co.yellowfire.domain.profile.User;
import za.co.yellowfire.domain.profile.UserManager;
import za.co.yellowfire.domain.profile.UserRegistrationException;
import za.co.yellowfire.domain.racing.Club;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.model.RequestResult;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
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

    @EJB(name = "UserManager")
	private UserManager manager;

    @EJB(name = "DomainManager")
    private DomainManager domainManager;

    private List<Club> clubs;
    
    public User getUser() {
        return user;
    }

    public List<Club> getClubs() {
        if (clubs == null) {
            clubs = (List<Club>) domainManager.query(Club.QRY_CLUBS, null);
        }
        return clubs;
    }

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

    public void onRegister() {
    	LOGGER.info("register() called");

        RequestResult result = new RequestResult();
    	try {
    		/* Register the user */
    		manager.register(user);

            /* Send a registration event */
            registrationEventSrc.fire(user);

    		/* Welcome and forward to races */
    		addInfoMessage("Welcome " + user.getFirstName() + user.getLastName(), "An email will be sent to " + user.getEmail() + " for verification.");
    	} catch (UserRegistrationException e) {
            result.failed(e.getMessage());
    		LOGGER.error("Registration error", e);
    		addErrorMessage(ERROR_USER_REGISTER, e.getMessage());
    	} catch (Exception e) {
            result.failed(e.getMessage());
    		LOGGER.error(ERROR_USER_REGISTER, e);
    		addErrorMessage("Error", e);
    	}

        RequestContext context = RequestContext.getCurrentInstance();
        if (context != null) {
            LOGGER.info("Setting PrimeFaces result %s", result);
            context.addCallbackParam("result", result);
        } else {
            LOGGER.warn("Cannot set PrimeFaces result because context is null");
        }
    }

    public String onCompleteConversation() {
        return "index";
    }
}
