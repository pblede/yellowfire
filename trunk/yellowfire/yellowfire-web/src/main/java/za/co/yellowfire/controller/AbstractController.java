package za.co.yellowfire.controller;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

//import org.openfaces.event.AjaxActionEvent;

import za.co.yellowfire.domain.profile.RoleType;
import za.co.yellowfire.ui.FacesUtil;

/**
 *
 * @author Mark Ashworth
 * @version 0.0.1
 */
public abstract class AbstractController implements Serializable {
    public static final long serialVersionUID = 1L;
    
    public static final String VIEW_RACE = "race";
    public static final String VIEW_RACES = "races";
    public static final String VIEW_REGISTER = "profile";
    public static final String VIEW_PROFILE = "profile";
    public static final String VIEW_RESULTS_CALENDAR = "result/calendar";
    
	/**
	 * 
	 * Return string which contains formated path from view root to component.
	 * 
	 * @param component
	 * @return string
	 */
	protected static String getComponentPath(UIComponent component) {
		StringBuilder builder = new StringBuilder("Component path: ");
		if (component == null) {
			builder.append("null");
		} else {
			getComponentPath(component, builder);
		}
		return builder.toString();
	}

	protected static void getComponentPath(UIComponent component, StringBuilder builder) {
		if (component != null) {
			getComponentPath(component.getParent(), builder);
			builder.append("/").append(component.getClass().getName());
			if (component instanceof UIViewRoot) {
				builder.append("[viewId=");
				builder.append(((UIViewRoot) component).getViewId());
			} else {
				builder.append("[id=");
				builder.append(component.getId());
			}
			builder.append("]");
		}
	}
   
    @SuppressWarnings("unchecked")
    public static <E> E getEventParameter(ActionEvent event, String paramName) {
        List<UIComponent> children = event.getComponent().getChildren();
        for (UIComponent component : children) {
            if (component instanceof UIParameter) {
                UIParameter uiParameter = (UIParameter) component;
                if (paramName.equals(uiParameter.getName())) {                    
                    return (E) uiParameter.getValue();                    
                }
            }
        }
        return null;
    }
    
    public void addInfoMessage(String summary, String detail) {
    	FacesUtil.addInfoMessage(summary, detail);
    }
    
    public void addWarnMessage(String summary, String detail) {
    	FacesUtil.addWarnMessage(summary, detail);
    }
    
    public void addErrorMessage(String summary, String detail) {
    	FacesUtil.addErrorMessage(summary, detail);
    }
    
    public void addErrorMessage(String summary, Throwable error) {
    	FacesUtil.addErrorMessage(summary, error);
    }
    
    //public void addAjaxResult(AjaxActionEvent event, String name, Object value) {
    //	RequestContext context = RequestContext.getCurrentInstance();
    //    context.addCallbackParam(name, value);
    //    event.setAjaxResult(value);
    //}
    
    protected static Object getManagedBean(final String beanName) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getELContext().getELResolver().getValue(facesContext.getELContext(), null, beanName);
    }

    protected static SessionController getSessionController() {
        return (SessionController) getManagedBean(SessionController.NAME);
    }
    
    protected static String getUserNameLoggedIn() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	return facesContext.getExternalContext().getRemoteUser();
    }
    
    protected static Principal getUserLoggedIn() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	return facesContext.getExternalContext().getUserPrincipal();
    }
    
    protected static boolean isUserLoggedIn() {
    	return getUserLoggedIn() != null;
    }
    
    protected static boolean isUserInRole(String role) {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	return facesContext.getExternalContext().isUserInRole(role);
    }
    
    protected static boolean isUserInRole(RoleType role) {
    	return isUserInRole(role.name());
    }
    
    protected static void invalidateSession() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	facesContext.getExternalContext().invalidateSession();
    }
}
