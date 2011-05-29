package za.co.yellowfire.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.profile.Authenticated;
import za.co.yellowfire.domain.profile.Guest;
import za.co.yellowfire.domain.profile.User;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.ui.FacesUtil;
import za.co.yellowfire.ui.security.CurrentUserManager;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@SessionScoped
@Named("userSession")
public class UserSessionController implements Serializable {
    private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());
    
    @Inject
    private CurrentUserManager manager;

    @Inject @Guest
    private Event<User> logoutEventSrc;

    private Date businessDate = new Date();
    
    public User getUser() {
        return manager.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return manager.isLoggedIn();
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void onIdleSession() {
        LOGGER.info("onIdleSession()");
        FacesUtil.addWarnMessage("Your session is closed", "You have been idle for too long");
        logoutEventSrc.fire(getUser());
        FacesUtil.invalidateSession();
    }

    public void onActivatedSession() {
        LOGGER.info("onActivatedSession()");
        FacesUtil.addWarnMessage("Welcome back", "You have been idle for too long and need refresh the page");
    }
}
