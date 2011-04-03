package za.co.yellowfire.controller;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import za.co.yellowfire.domain.racing.Race;

@ViewScoped
@ManagedBean(name = "raceController")
public class RaceController {

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