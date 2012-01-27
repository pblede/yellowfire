package za.co.yellowfire.controller;

import org.jboss.seam.security.Identity;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.profile.Registered;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.domain.profile.UserManager;
import za.co.yellowfire.domain.profile.UserRegistrationException;
import za.co.yellowfire.domain.racing.Club;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.FacesUtil;
import za.co.yellowfire.ui.common.AbstractCommonUIController;
import za.co.yellowfire.ui.model.RequestResult;

import javax.annotation.PostConstruct;
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
public class ProfileController extends AbstractCommonUIController {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.PROFILE.getCategory());

    private static final String ERROR_USER_PERSIST = "controller.user.persist.error";
    private static final String INFO_USER_PERSISTED = "controller.user.persisted";
    /** Error code: User registration failed */
    private static final String ERROR_USER_REGISTER = "controller.register.error";

    @Inject @Registered
    private Event<Profile> registrationEventSrc;

    @Inject
    private Identity identity;
    private Profile profile;

    @EJB(name = "UserManager")
	private UserManager manager;

    @EJB(name = "DomainManager")
    private DomainManager domainManager;

    private List<Club> clubs;
    
    public Profile getProfile() {
        return profile;
    }
    
    public void setProfile(Profile profile) {
        this.profile = profile;
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
			FacesUtil.addErrorMessage("Error", e);
			return false;
		}
    }

    @PostConstruct 
    public void init() {
        if (this.identity != null) {
            this.profile = (Profile) identity.getUser();
            if (this.profile != null) {
                this.profile.setPasswordConfirmation(this.profile.getPassword());
            }
        } else {
            this.profile = new Profile();
        }
    }
    
    public void onPersist(ActionEvent event) {
		try {
			final String c = profile.getPasswordConfirmation();
			this.profile = manager.persist(profile);
			this.profile.setPasswordConfirmation(c);
			FacesUtil.addInfoMessage(INFO_USER_PERSISTED, "Update to profile saved");
		} catch (Exception e) {
			FacesUtil.addErrorMessage(ERROR_USER_PERSIST, e);
		}
	}

    public String onRegister() {
    	LOGGER.info("register() called");

        RequestResult result = new RequestResult();
    	try {
    		/* Register the user */
    		manager.register(profile);

            /* Send a registration event */
            registrationEventSrc.fire(profile);

    		/* Welcome and forward to races */
    		FacesUtil.addInfoMessage("Welcome " + profile.getFirstName() + profile.getLastName(), "An email will be sent to " + profile.getEmail() + " for verification.");
    	} catch (UserRegistrationException e) {
            result.failed(e.getMessage());
    		LOGGER.error("Registration error", e);
    		FacesUtil.addErrorMessage(ERROR_USER_REGISTER, e.getMessage());
    	} catch (Exception e) {
            result.failed(e.getMessage());
    		LOGGER.error(ERROR_USER_REGISTER, e);
    		FacesUtil.addErrorMessage("Error", e);
    	}

        RequestContext context = RequestContext.getCurrentInstance();
        if (context != null) {
            LOGGER.info("Setting PrimeFaces result %s", result);
            context.addCallbackParam("result", result);
        } else {
            LOGGER.warn("Cannot set PrimeFaces result because context is null");
        }

        return "register";
    }

    public void onDelete() {
        LOGGER.info("register() called");
    }
    
    public String onCompleteConversation() {
        return "index";
    }
}
