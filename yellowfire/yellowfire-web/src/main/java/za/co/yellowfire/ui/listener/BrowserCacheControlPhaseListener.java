package za.co.yellowfire.ui.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

/**
 */
public class BrowserCacheControlPhaseListener {
    private static final long serialVersionUID = 1L;

    public void afterPhase(PhaseEvent event) {
    }

    public void beforePhase(PhaseEvent event) {
//        FacesContext facesContext = event.getFacesContext();
//        ExternalContext externalContext = facesContext.getExternalContext();
//        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
//        response.addHeader("Pragma", "no-cache");
//        response.addHeader("Cache-Control", "no-cache");
//        response.addHeader("Cache-Control", "no-store");
//        response.addHeader("Cache-Control", "must-revalidate");
//        response.addHeader("Expires", "-1");
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

}
