package za.co.yellowfire.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.profile.Credentials;
import za.co.yellowfire.domain.profile.Guest;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.ui.FacesUtil;
import za.co.yellowfire.ui.annotation.UIContext;
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
    private Event<Profile> logoutEventSrc;

    /* User login credentials used by the quickBar*/
    private Credentials credential = new Credentials();
    
    private Date businessDate = new Date();

    private UIContext context = UIContext.Common;

    public Profile getUser() {
        return manager.getCurrentUser();
    }

    /**
     * User login credentials.
     * **NOTE** Used by the quickBar
     * @return Credential
     */
    public Credentials getCredential() {
        return credential;
    }
    
    public boolean isLoggedIn() {
        return manager.isLoggedIn();
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    /**
     * Fired when the user logs in.
     */
    public void login() {
        try {
            manager.login(getCredential());
        } catch (Throwable e) {
            /*Catching throwable here so I can find out what the hell is going on*/
            LOGGER.error("Unable to login", e);
        }
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
