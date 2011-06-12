package za.co.yellowfire.controller;

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import za.co.yellowfire.domain.racing.Race;

@ViewScoped
@Named("raceController")
public class RaceController implements Serializable {

    //@EJB(beanInterface = RaceManager.class, beanName = "RaceManager", mappedName = "bluefire/session/RaceManager")
    //private RaceManager raceManager;

    private Race selected;


	/**
	 * Selected race
	 * @param selected the selected race
	 */
	public void setSelected(Race selected) {
		this.selected = selected;
	}


	/**
	 * Selected race
	 * @return the selected race
	 */
	public Race getSelected() {

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        System.out.println("params = " + params.get("race.id"));
        
		return selected;
	}


}