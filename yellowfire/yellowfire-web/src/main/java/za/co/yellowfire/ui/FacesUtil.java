package za.co.yellowfire.ui;

import za.co.yellowfire.ui.resources.MessageKey;
import za.co.yellowfire.ui.resources.MessageResources;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

/**
 * Java Server Faces utility methods
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class FacesUtil {

    /**
     * Returns the Bean Manager
     * @return BeanManager
     */
	protected BeanManager getBeanManager() {
        try{
            InitialContext initialContext = new InitialContext();
            return (BeanManager) initialContext.lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            //LOGGER.error("Couldn't get BeanManager through JNDI");
            return null;
        }
    }
        
	protected BeanManager getBeanManager(FacesContext facesContext) {
        return (BeanManager) ((ServletContext) facesContext.getExternalContext().getContext()).getAttribute("javax.enterprise.inject.spi.BeanManager"); 
    }
	
	protected Object getBeanByName(String name) {
        BeanManager bm = getBeanManager();
        Bean bean = bm.getBeans(name).iterator().next();
        if (bean != null) {
	        CreationalContext ctx = bm.createCreationalContext(bean); // could be inlined below
	        Object o = bm.getReference(bean, bean.getClass(), ctx); // could be inlined with return
	        return o;
        }
        return null;
    }

    /**
     * Adds an global INFO message. The summary is read from the dialog.info resource key
     * @param detail The detail of the message
     */
    public static void addInfoMessage(String detail) {
        addErrorMessage(MessageResources.MESSAGE(MessageKey.dialogInfo), detail);
    }

    /**
     * Adds an global INFO message.
     * @param summary The summary of the message
     * @param detail The detail of the message
     */
    public static void addInfoMessage(String summary, String detail) {
        addInfoMessage(null, summary, detail);
    }

    /**
     * Adds an INFO message.
     * @param componentId The component id that the message should be bound to or null for a global message
     * @param summary The summary of the message
     * @param detail The detail of the message
     */
	public static void addInfoMessage(String componentId, String summary, String detail) {
    	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(componentId, msg);
    }

    /**
     * Adds an global WARN message. The summary is read from the dialog.warn resource key
     * @param detail The detail of the message
     */
    public static void addWarnMessage(String detail) {
        addErrorMessage(MessageResources.MESSAGE(MessageKey.dialogWarning), detail);
    }

     /**
     * Adds an global WARN message.
     * @param summary The summary of the message
     * @param detail The detail of the message
     */
    public static void addWarnMessage(String summary, String detail) {
    	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void addWarnUserNotFound() {
        addWarnMessage(
                MessageResources.MESSAGE(MessageKey.warningUserNotFound),
                MessageResources.MESSAGE(MessageKey.msgUserPasswordIncorrect));
    }

    /**
     * Adds an global ERROR message. The summary is read from the dialog.error resource key
     * @param detail The detail of the message
     */
    public static void addErrorMessage(String detail) {
        addErrorMessage(MessageResources.MESSAGE(MessageKey.dialogError), detail);
    }

     /**
     * Adds an global ERROR message.
     * @param summary The summary of the message
     * @param detail The detail of the message
     */
    public static void addErrorMessage(String summary, String detail) {
    	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Adds a global ERROR message
     * @param summary The summary of the message
     * @param error The exception detail for the message
     */
    public static void addErrorMessage(String summary, Throwable error) {
    	addErrorMessage(FacesContext.getCurrentInstance(), null, summary, error);
    }

    /**
     * Adds a global ERROR message
     * @param clientId The client id of the message or null for global
     * @param summary The summary of the message
     * @param error The exception detail for the message
     */
    public static void addErrorMessage(String clientId, String summary, Throwable error) {
    	addErrorMessage(FacesContext.getCurrentInstance(), clientId, summary, error);
    }

    /**
     * Adds a global ERROR message
     * @param context The Faces context
     * @param clientId The client id of the message or null for global
     * @param summary The summary of the message
     * @param error The exception detail for the message
     */
    public static void addErrorMessage(FacesContext context, String clientId, String summary, Throwable error) {
    	String e = "error";
    	if (error != null) {
    		e = error.getMessage();
    		if (e != null) {
    			e = e.substring(0, e.length() > 250 ? 250 : e.length());
    		} else {
    			e = error.toString();
    		}
    	}
    	
    	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, e);
    	context.addMessage(clientId, msg);
    }

    /**
     * Invalidates the session
     */
    public static void invalidateSession() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    /**
     * Gets the view parameter from the Faces request parameter map
     * @param name The name of the request parameter
     * @return The string value of the parameter or null
     */
    public static String getViewParameter(String name) {
        if (FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null) {
            String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
            if (param != null && param.trim().length() > 0) {
                return param;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
