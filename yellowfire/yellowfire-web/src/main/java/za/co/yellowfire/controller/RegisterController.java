package za.co.yellowfire.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.Naming;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.domain.profile.UserManager;
import za.co.yellowfire.domain.profile.UserRegistrationException;
import za.co.yellowfire.log.LogType;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@RequestScoped
@Named("registerController")
public class RegisterController extends AbstractController {
    private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());
    
    /** Error code: User registration failed */
    private static final String ERROR_USER_REGISTER = "controller.register.error";

    private Profile user = new Profile();
    
    
	public Profile getUser() {
		return user;
	}


	public UserManager getManager() throws NamingException {
		return (UserManager) new InitialContext().lookup(Naming.MANAGER_USER);
	}
	
	public boolean getUserNameAvailable(String value) throws NamingException {
    	LOGGER.info("getUserNameAvailable() called");
    	return getManager().isUsernameAvailable(value);
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
    
    public String register() {
    	LOGGER.info("register() called");
    	
    	try {
    		/* Register the user */
    		this.user = getManager().register(user);
    		
    		/* Log the user on */
    		SessionController session = getSessionController();
    		session.setUser(this.user);
    		//TODO Investigate why this is
    		//session.login();
    		
    		/* Welcome and forward to races */
    		addInfoMessage("Welcome", user.getName());
    		return VIEW_RACES;
    	} catch (UserRegistrationException e) {
    		LOGGER.error("Registration error", e);
    		addErrorMessage(ERROR_USER_REGISTER, e.getMessage());
    	} catch (NamingException e) {
    		LOGGER.error(ERROR_USER_REGISTER, e);
    		addErrorMessage("Error", e);
    	} catch (Exception e) {
    		LOGGER.error(ERROR_USER_REGISTER, e);
    		addErrorMessage("Error", e);
    	}
    	return null;
    }
    
    public void register(ActionEvent event) {
    	LOGGER.info("register(ActionEvent) called");
        
        
    }
}
