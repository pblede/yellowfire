package za.co.yellowfire.ui.security;

import org.primefaces.context.RequestContext;
import za.co.yellowfire.controller.AbstractController;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.domain.profile.UserManager;
import za.co.yellowfire.domain.racing.Race;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.util.Map;

//import org.openfaces.event.AjaxActionEvent;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 * deprecated Using LoginController login which authenticates using JAAS via the servler container
 */
@SessionScoped
@Named("userController")
public class UserController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private static final String ERROR_USER_LOGIN = "controller.user.login.error";
	
	@EJB(name="UserManager")
	private UserManager manager;
	
    private Profile user = new Profile();
    private String passwordConfirmation;
    
    private Race selectedRace;
    private boolean registered;
    private boolean loggedIn;
    
    public Profile getUser() {
        return user;
    }

    public void setUser(Profile user) {
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
        
    	if (!getUserNameAvailable(user.getUserName())) {
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
	    	Profile u = manager.login(getUser().getUserName(), getUser().getPassword());
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
