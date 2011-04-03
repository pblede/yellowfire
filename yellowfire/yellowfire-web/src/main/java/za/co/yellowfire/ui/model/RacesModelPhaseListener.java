package za.co.yellowfire.ui.model;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class RacesModelPhaseListener implements PhaseListener {
	private static final long serialVersionUID = 1L;
	
    @Override
    public void afterPhase(PhaseEvent phaseEvent) {
        System.out.println("After:" + phaseEvent.getPhaseId());
    }

    /**
     *
     * @param phaseEvent The phase event
     */
    @Override
    public void beforePhase(PhaseEvent phaseEvent) {
        System.out.println("Before:" + phaseEvent.getPhaseId());
        if (phaseEvent.getPhaseId().getOrdinal() == PhaseId.RESTORE_VIEW.getOrdinal()) {
            System.out.println("***********BEGIN***********");
        }
        if (phaseEvent.getPhaseId().getOrdinal() == PhaseId.UPDATE_MODEL_VALUES.getOrdinal()) {

            FacesContext context =phaseEvent.getFacesContext();
            UIViewRoot root = context.getViewRoot();
            if (root != null) {
                String id = root.getViewId();
                if (id.lastIndexOf("races") > -1) {
                    
                }
                System.out.println("view = " + root.getViewId());;
            }
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
