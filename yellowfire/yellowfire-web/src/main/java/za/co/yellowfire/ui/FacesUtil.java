package za.co.yellowfire.ui;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

public class FacesUtil {

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
	
	public static void addInfoMessage(String summary, String detail) {
    	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public static void addWarnMessage(String summary, String detail) {
    	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public static void addErrorMessage(String summary, String detail) {
    	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public static void addErrorMessage(String summary, Throwable error) {
    	addErrorMessage(FacesContext.getCurrentInstance(), null, summary, error);
    }
    
    public static void addErrorMessage(String clientId, String summary, Throwable error) {
    	addErrorMessage(FacesContext.getCurrentInstance(), null, summary, error);
    }
    
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
    	context.addMessage(null, msg);
    }
}
