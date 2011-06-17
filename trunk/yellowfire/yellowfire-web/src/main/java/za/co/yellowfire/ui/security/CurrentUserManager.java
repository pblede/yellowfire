package za.co.yellowfire.ui.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.profile.*;
import za.co.yellowfire.domain.racing.Club;
import za.co.yellowfire.domain.racing.RaceManager;
import za.co.yellowfire.domain.training.TrainingCourse;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Stateful @SessionScoped
public class CurrentUserManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

	private User user = new User();
    private boolean loggedIn = false;

    private TrainingCourse course;

    @EJB(name = "DomainManager")
	private DomainManager manager;

	@Produces @Authenticated @Named("currentUser")
	public User getCurrentUser() {
		return user;
	}

    @Produces @Default @Named()
    public TrainingCourse getCurrentCourse() {
        return course;
    }

    @Produces @Named("isLoggedIn")
    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Produces @Named("sessionTimeout")
    public int getSessionTimeout() {
        return 5000;
    }

    @Produces @Guest
    @Named("guestUser")
	public User getGuestUser() {
		return new User();
	}

	public void onLogin(@Observes @Authenticated User user) {
		LOGGER.info("onLogin : " + user);
		this.user = user;
        this.loggedIn = true;
	}

    public void onLogout(@Observes @Guest User user) {
		LOGGER.info("onLogout : " + user);
		this.user = new User();
        this.loggedIn = false;
	}

    public void onAuthenticationFailure(@Observes @AuthenticateFailure AuthenticationFailure failure) {
        LOGGER.info("onAuthenticationFailure : " + failure.getType() + ":" + failure.getUser());
        this.user = new User();
        this.loggedIn = false;
    }

    public void onEditTrainingCourse(@Observes TrainingCourse course) {
        LOGGER.info("onEditTrainingCourse : " + course);
        this.course = course;
    }
}
