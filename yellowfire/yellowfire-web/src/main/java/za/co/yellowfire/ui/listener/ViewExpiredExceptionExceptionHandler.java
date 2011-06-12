package za.co.yellowfire.ui.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;

import javax.enterprise.context.NonexistentConversationException;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;
import java.util.Map;

/**
 * 2010-12-13 - Taken from http://weblogs.java.net/blog/edburns/archive/2009/09/03/dealing-gracefully-viewexpiredexception-jsf2
 * 2011-06-08 - Set the ViewRoot to the expired view when it is null because this is causing NullPointerExceptions when
 *              JSF tries to determine if the new view is different from the current view.
 */
public class ViewExpiredExceptionExceptionHandler extends ExceptionHandlerWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.LISTENER.getCategory());

    private ExceptionHandler wrapped;

    public ViewExpiredExceptionExceptionHandler(ExceptionHandler wrapped) { this.wrapped = wrapped; }

    @Override public ExceptionHandler getWrapped() { return this.wrapped; }

    @Override
    public void handle() throws FacesException {
        for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();

            FacesContext fc = FacesContext.getCurrentInstance();
            Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            NavigationHandler nav = fc.getApplication().getNavigationHandler();

            if (t instanceof NonexistentConversationException) {

                try {
                    // Push some useful stuff to the request scope for use in the page
                    requestMap.put("currentException", t);
                    // If the view roow it null, then set it to the view that expired.
                    if (fc.getViewRoot() == null) {
                        UIViewRoot view = fc.getApplication().getViewHandler().createView(fc, "/index.jsf");
                        fc.setViewRoot(view);
                    }
                    //Navigate to the view_expired outcome. Use view_expired.xhtml for implicit handling or
                    //configure a navigation rule in the faces-config.xml for this outcome
                    nav.handleNavigation(fc, null, "view_expired");
                    //Skip to rendering the response
                    fc.renderResponse();
                } catch (Throwable e) {
                    LOGGER.error("ViewExpiredExceptionExceptionHandler.handle() error {}", e);
                } finally {
                    //Remove this unhandled exception
                    i.remove();
                }

            } else if (t instanceof ViewExpiredException) {
                ViewExpiredException vee = (ViewExpiredException) t;

                try {
                    // Push some useful stuff to the request scope for use in the page
                    requestMap.put("currentViewId", vee.getViewId());
                    // If the view roow it null, then set it to the view that expired.
                    if (fc.getViewRoot() == null) {
                        UIViewRoot view = fc.getApplication().getViewHandler().createView(fc, vee.getViewId());
                        fc.setViewRoot(view);
                    }
                    //Navigate to the view_expired outcome. Use view_expired.xhtml for implicit handling or
                    //configure a navigation rule in the faces-config.xml for this outcome
                    nav.handleNavigation(fc, null, "view_expired");
                    //Skip to rendering the response
                    fc.renderResponse();
                } catch (Throwable e) {
                    LOGGER.error("ViewExpiredExceptionExceptionHandler.handle() error {}", e);
                } finally {
                    //Remove this unhandled exception
                    i.remove();
                }
            }
        }
        // At this point, the queue will not contain any ViewExpiredEvents.
        // Therefore, let the parent handle them.
        getWrapped().handle();
    }
}
