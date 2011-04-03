package za.co.yellowfire.ui.model;

import za.co.yellowfire.domain.racing.Race;
import za.co.yellowfire.domain.racing.RaceManager;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.DataModel;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class RacesModel extends DataModel<Race> {
    private static final long serialVersionUID = 1L;

    private RaceManager raceManager;
    //private RaceDomainQuery query;
    private List<Race> races;
    private int index = 0;
    private boolean reset = false;
    private final Object lock = new Object();
    
    public RacesModel(RaceManager raceManager/*, RaceDomainQuery query*/) {
        this.raceManager = raceManager;
        //this.query = query;
    }

    @Override
    public boolean isRowAvailable() {
        System.out.println("RacesModel.isRowAvailable for " + getRowIndex());
        return getRowIndex() >= 0 && getRowIndex() < getRaces().size();
    }

    @Override
    public int getRowCount() {
        System.out.println("RacesModel.getRowCount()");
        return getRaces().size();
    }

    @Override
    public Race getRowData() {
        System.out.println("RacesModel.getRowData() for " + getRowIndex());
        return getRaces().get(getRowIndex());
    }

    @Override
    public int getRowIndex() {
        System.out.println("getRowIndex():" + this.index);
        return this.index;
    }

    @Override
    public void setRowIndex(int i) {
        System.out.println("setRowIndex(" + this.index + ")");
        this.index = i;
    }

    public void reset() {
        System.out.println("RacesModel.reset()");
        if (this.races != null) {
            synchronized (lock) {
                if (this.races != null) {
                    //this.races.clear();
                    this.races = null;
                }
            }
        }
    }

    public List<Race> getRaces() {
        System.out.println("\tRacesModel.getRaces()");

        PhaseId phaseId = FacesContext.getCurrentInstance().getCurrentPhaseId();
        System.out.println("RacesModel.getRaces() phaseId = " + phaseId.toString());
        if (reset == false && phaseId.getOrdinal() == PhaseId.RESTORE_VIEW.getOrdinal()) {
            reset = true;
        }
        
        if (phaseId.getOrdinal() == PhaseId.RESTORE_VIEW.getOrdinal()) {
            reset();
        }
        synchronized (lock) {
            if (races == null) {
                races = raceManager.retrieveUpcomingRaces();
            }
        }
        return this.races;
    }

    @Override
    public Object getWrappedData() {
        System.out.println("RacesModel.getWrappedData()");
        return getRaces();
    }

    @Override
    public void setWrappedData(Object o) { /*Not implemented */ throw new RuntimeException("SetWrappedData is not supported");}
}
