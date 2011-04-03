package za.co.yellowfire.ui.converter;

import za.co.yellowfire.domain.training.DurationType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class DurationTypeConverter extends AbstractConverter implements Converter {
	/**
	 * Converts the value into a DomainObject instance by looking up the value as the id of the Club
	 */
	@Override public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return DurationType.valueOf(Integer.parseInt(value));
	}

	/**
	 * Converts the DomainObject into a String by formating the id as a String
	 */ 
	@Override public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null) {
			return null;
		}
		return Integer.toString(((DurationType) value).getId());
	}
}
