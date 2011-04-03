package za.co.yellowfire.ui.security;

import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

//import org.openfaces.event.AjaxActionEvent;
import org.primefaces.context.RequestContext;

import za.co.yellowfire.controller.AbstractController;
import za.co.yellowfire.domain.racing.Race;
import za.co.yellowfire.domain.profile.Credential;
import za.co.yellowfire.domain.profile.User;
import za.co.yellowfire.domain.profile.UserManager;

/**
 *
 */
@SessionScoped
@ManagedBean(name = "userController")
public class UserController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private static final String ERROR_USER_LOGIN = "controller.user.login.error";
	
	@EJB(name="UserManager")
	private UserManager manager;
	
    private User user = new User();
    private String passwordConfirmation;
    
    private Race selectedRace;
    private boolean registered;
    private boolean loggedIn;
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public Race getSelectedRace() {
        return selectedRace;
    }

    public void setSelectedRace(Race selectedRace) {
        this.selectedRace = selectedRace;
    }

    public boolean isRegistered() {
        return registered;
    }
    
    public void setRegistered(boolean value) {
        this.registered = value;
    }
    
    public boolean isLoggedIn() {
    	return this.loggedIn;
    }
    
    public boolean getUserNameAvailable(String value) {
    	return value.equalsIgnoreCase("mark");
    }
    
    public boolean userNameAvailable(FacesContext context, UIComponent component, Object value) {
        return getUserNameAvailable((String) value);
    }
    
    public void register(ActionEvent actionEvent) {
       
    	RequestContext context = RequestContext.getCurrentInstance();
        
    	if (!getUserNameAvailable(user.getName())) {
    		context.addCallbackParam("error", "The username is not available");
    		addErrorMessage("Error", "The password confirmation should match the password");
    	}
    	if (user.getPassword() == null) {
    		context.addCallbackParam("error", "The password is required");
    		addErrorMessage("Error", "The password is required");
    	} else if (user.getPassword().trim().equals("")) {
    		context.addCallbackParam("error", "The password is required");
    		addErrorMessage("Error", "The password is required");
    	} else if (!user.getPassword().equals(passwordConfirmation)) {
    		context.addCallbackParam("error", "The password confirmation should match the password");
    		addErrorMessage("Error", "The password confirmation should match the password");
    	}
    	
    	
    	Map<String, Object> callbackParams = context.getCallbackParams();
    	if (!callbackParams.containsKey("error")) {
    		context.addCallbackParam("loggedIn", true);
    		addInfoMessage("Welcome", "Mark");

            //Classcast exception during compile ;-?
    		//if (actionEvent instanceof AjaxActionEvent) {
            //	((AjaxActionEvent) actionEvent).setAjaxResult("registered");
            //}
    		
    		registered = true;
    	} else {
    		registered = false;
    	}
    }
    
    /*
    public void register(AjaxActionEvent actionEvent) {
        RequestContext context = RequestContext.getCurrentInstance();
        
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", "Mark");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", true);
        
        actionEvent.setAjaxResult("registered");
        registered = true;
    }
    */
    public void register() {
        RequestContext context = RequestContext.getCurrentInstance();
        
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", "Mark");
        FacesContext.getCurrentInstance().addMessage(null, msg);
	context.addCallbackParam("loggedIn", true);
        registered = true;
    }
    
    /**
     * Used by the prime-layout.xhtml login function
     * @deprecated To be removed
     */
    public void login() {
    	try {
	    	User u = manager.login(new Credential(getUser().getName(), getUser().getPassword()));
	    	if (u != null) {
	    		this.loggedIn = true;
	    		this.user = u;
	    	} else {
	    		this.loggedIn = false;
	    	}
    	} catch (Exception e) {
    		addErrorMessage(ERROR_USER_LOGIN, e);
    	}
    }
    
    /**
     * Used by the prime-layout.xhtml login function
     * @param actionEvent The action event

    public void login(AjaxActionEvent actionEvent) {
    	if (isLoggedIn()) {
    		addInfoMessage("Welcome", getUser().getName());
    		addAjaxResult(actionEvent, "loggedIn", true);
    	}
    }
    */
    public void logout(ActionEvent actionEvent) {
    	this.loggedIn = false;
    }
}
