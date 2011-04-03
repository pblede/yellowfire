package za.co.yellowfire.controller;

import za.co.yellowfire.domain.profile.User;
import za.co.yellowfire.ui.security.CurrentUserManager;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@SessionScoped
@ManagedBean(name = "userSession")
public class UserSessionController implements Serializable {

    @Inject
    private CurrentUserManager manager;
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
}
