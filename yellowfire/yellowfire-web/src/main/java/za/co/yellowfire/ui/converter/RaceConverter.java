package za.co.yellowfire.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.naming.NamingException;
import javax.persistence.LockModeType;

import za.co.yellowfire.domain.racing.Race;

/**
 * 
 * @author Mark Ashworth
 * @version 0.0.1
 */
public class RaceConverter extends AbstractConverter implements Converter {

	/**
	 * Converts the value into a Race instance by looking up the value as the id of the Race
	 */
	@Override public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		if (value == null) { 
			LOGGER.debug("RaceConverter.getAsObject() : value is null");
			return null; 
		}
		if (value.equals("")) { 
			LOGGER.debug("RaceConverter.getAsObject() : value is empty string");
			return null; 
		}
		
		Long id = null;
		try {
			LOGGER.debug("RaceConverter.getAsObject() : parsing value " + value + " to a long");
			id = Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new ConverterException("Unable to convert " + value + " to a Long", e);
		}
		
		LOGGER.debug("RaceConverter.getAsObject() : looking up club with id " + id);
		Race race;
		try {
			race = (Race) getDomainManager().find(Race.class, id, LockModeType.OPTIMISTIC);
		} catch (NamingException e) {
			throw new ConverterException("Unable to lookup the race because RaceManager could not be resolved", e);
		}
		LOGGER.debug("RaceConverter.getAsObject() : found race " + race);
		return race;
	}

	/**
	 * Converts the Race into a String by formating the id as a String
	 */
	@Override public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null) { 
			LOGGER.debug("RaceConverter.getAsString() : value is null");
			return null; 
		}
		if (!(value instanceof Race)) {
			LOGGER.debug("RaceConverter.getAsString() : value is not a instance of Race");
			return null; 
		}
		
		String ret = ((Race) value).getId().toString();
		LOGGER.debug("RaceConverter.getAsString() : returning " + ret);
		return ret;
	}
}
