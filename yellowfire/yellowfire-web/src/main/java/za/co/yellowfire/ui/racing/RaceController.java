package za.co.yellowfire.ui.racing;

import za.co.yellowfire.domain.racing.Race;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.Map;

@ViewScoped
@Named("raceController")
public class RaceController extends AbstractRacingUIController {

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