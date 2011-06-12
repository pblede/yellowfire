package za.co.yellowfire.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.inject.Named;

import za.co.yellowfire.domain.racing.Race;

@ViewScoped
@Named("racesController")
public class RacesController extends AbstractController {
    private static final long serialVersionUID = 1L;
            
    private List<Race> races;

    private boolean reset = false;

    //private RacesModel races;

    public void reset() {
        if (this.races != null) {
            synchronized (this) {
                if (this.races != null) {
                    //this.races.clear();
                    this.races = null;
                }
            }
        }
    }

    /**
     *Returns the races
     * @return Race[]
     */
    public List<Race> getRaces() {
        PhaseId phaseId = FacesContext.getCurrentInstance().getCurrentPhaseId();
        if (reset == true && phaseId.getOrdinal() == PhaseId.RESTORE_VIEW.getOrdinal()) {
            reset = false;
            reset();
        }
        if (phaseId.getOrdinal() == PhaseId.RENDER_RESPONSE.getOrdinal()) {
            reset = true;
        }

    	if (races == null) {
    		synchronized(this) {
    			if (races == null) {
                    try {
                    	races = getSessionController().getRaceManager().retrieveUpcomingRaces();
                    } catch (Exception e) {
                    	e.printStackTrace();
                    }
    				
    			}
    		}
    	}
        return races;
    }

    public int getRacesCount() {
        return getRaces().size();
    }

    public void resetRaces(ActionEvent event) {
    }

    public String getAction() {
        return "/race";
    }

    /**
     * Select a race for viewing
     */
    public String view() { return "race"; }

    /**
     * Select a race for registering
     */
    public String register() { return "race/registration"; }
    
    /**
     * Select a race
     * @param event The event
     */
    public void select(ActionEvent event) {
    }
}
