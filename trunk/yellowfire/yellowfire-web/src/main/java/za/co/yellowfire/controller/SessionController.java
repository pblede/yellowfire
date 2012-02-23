package za.co.yellowfire.controller;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.domain.profile.Guest;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.domain.profile.UserManager;
import za.co.yellowfire.domain.profile.UserRegistrationException;
import za.co.yellowfire.domain.racing.Club;
import za.co.yellowfire.domain.racing.Race;
import za.co.yellowfire.domain.racing.RaceManager;
import za.co.yellowfire.domain.result.ResultManager;
import za.co.yellowfire.common.log.LogType;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;

/**
 * @author Mark Ashworth
 * @version 0.0.1
 */
@SessionScoped
@Named("sessionController")
public class SessionController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

	public static final String NAME = "sessionController";
	//private static final String ERROR_USER_LOGIN = "controller.user.login.error";
	private static final String ERROR_USER_PERSIST = "controller.user.persist.error";
	private static final String ERROR_USER_REGISTER = "controller.user.register.error";

	//private static final String WARNING_USER_NOT_FOUND = "controller.user.not.found";
	//private static final String WARNING_USER_NOT_VERIFIED = "controller.user.not.verified";

	private static final String INFO_USER_PERSISTED = "controller.user.persisted";
	
	private Profile user = new Profile();
	private boolean loggedIn;
	private Date businessDate = new Date();

	/* Login event */
	//@Inject @Authenticated private Event<User> loginEventSrc;
    //@Inject @AuthenticateFailure private Event<AuthenticationFailure> authenticateFailureEventSrc;
    /* Logout event */
    @Inject @Guest private Event<Profile> logoutEventSrc;


	/* Race */
	private Race race;
	private MapModel raceMapModel;
	
	@EJB(name = "UserManager")
	private UserManager manager;

	@EJB(name = "RaceManager")
	private RaceManager raceManager;
	
	@EJB(name = "ResultManager")
	private ResultManager resultManager;
	
	public UserManager getUserManager() {
		return manager;
	}

	public RaceManager getRaceManager() {
		return raceManager;
	}

	public ResultManager getResultManager() {
		return resultManager;
	}
	
	public List<Club> getClubs() {
		return getRaceManager().retrieveClubs();
	}
	
	public Profile getUser() {
		if (user == null) {
			LOGGER.debug("getUser()");
			this.user = getUserManager().retrieve(getUserNameLoggedIn());
		}
		return user;
	}

	public void setUser(Profile user) {
		LOGGER.debug("setUser() : " + user);
		this.user = user;
	}

	public Race getRace() {
		return this.race;
	}
	
	public void setRace(Race race) {
		this.race = race;
		this.raceMapModel = new DefaultMapModel();
		
		if (race != null) {
			Venue venue = this.race.getVenue();
	        if (venue != null && venue.getGps() != null) {
	        	String[] val = venue.getGps().split(",");
	        	if (val.length == 2) {
	        		Marker marker = new Marker(new LatLng(Double.valueOf(val[0]), Double.valueOf(val[1])), venue.getName());
	        		this.raceMapModel.addOverlay(marker);
	        	}
	        }
		}
	}
	
	public MapModel getRaceMapModel() {
		return this.raceMapModel;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
		//return isUserLoggedIn();
	}

	public Date getBusinessDate() {
		return this.businessDate;
	}

	/**
	 * Used by the prime-layout.xhtml login function
	 */
	public void logout() {
		/* Fire the event that the user has logged out*/
        this.logoutEventSrc.fire(user);
        
        this.user = null;
		this.loggedIn = false;
		invalidateSession();

	}

    /**
     * Used by the training-layout.xhtml poll component
     */
    public void ping() {
        //Empty implementation used by the front-end to ping
    }

	/**
	 * Used by the prime-layout.xhtml login function
	 */
//	public void login() {
//        this.loggedIn = false;
//
//        try {
//            //Perform programmatic JAAS login
//            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//            request.login(getUser().getName(), getUser().getPassword());
//        } catch (Exception e) {
//            /* Fire the event that the user authentication failed because of the system */
//			addWarnMessage(WARNING_USER_NOT_FOUND, "The username or password is incorrect.");
//            this.authenticateFailureEventSrc.fire(new AuthenticationFailure(user, AuthenticationFailureType.Credentials));
//            return;
//        }
//
//		try {
//            //Perform a domain login
//			final String c = getUser().getPasswordConfirmation();
//			User u = manager.login(getUser());
//			if (u != null) {
//                LOGGER.info("SessionController.login() User : " + user);
//                if (!u.isVerified()) {
//                   /* The user is not verified */
//                    this.loggedIn = false;
//                   addWarnMessage(WARNING_USER_NOT_VERIFIED, "The user account is not verified.");
//
//                    /* Fire the event that the user authentication failed */
//                    this.authenticateFailureEventSrc.fire(new AuthenticationFailure(user, AuthenticationFailureType.NotVerified));
//                } else {
//                    /* The user is verified, allow the login*/
//                    this.loggedIn = true;
//                    this.user = u;
//                    this.user.setPasswordConfirmation(c);
//
//                    /* Fire the event that the user has logged in*/
//                    this.loginEventSrc.fire(user);
//                }
//			} else {
//				this.loggedIn = false;
//				addWarnMessage(WARNING_USER_NOT_FOUND, "The username or password is incorrect.");
//
//                /* Fire the event that the user authentication failed */
//                this.authenticateFailureEventSrc.fire(new AuthenticationFailure(user, AuthenticationFailureType.Credentials));
//			}
//		} catch (Exception e) {
//            LOGGER.error("Login error", e);
//			addErrorMessage(ERROR_USER_LOGIN, e);
//
//            /* Fire the event that the user authentication failed */
//            this.authenticateFailureEventSrc.fire(new AuthenticationFailure(user, AuthenticationFailureType.System));
//		}
//	}

    /**
     * Persist the user
     * @param event The JSF action event
     */
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

	/**
	 * Called by the quick login bar
	 * @return String
	 */
	public String createProfile() {
		return VIEW_REGISTER;
	}
	
	/**
	 * Called by the register view
	 * @return String
	 */
	public String register() {
		try {
			manager.register(user);
			addInfoMessage("Welcome", user.getUserName());
			return VIEW_RACES;
		} catch (UserRegistrationException e) {
			addErrorMessage(ERROR_USER_REGISTER, e.getMessage());
		} catch (Exception e) {
			addErrorMessage("Error", e);
		}
		return null;
	}
}
