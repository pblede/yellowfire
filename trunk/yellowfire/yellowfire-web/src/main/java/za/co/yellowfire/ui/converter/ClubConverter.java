package za.co.yellowfire.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;
import javax.inject.Scope;
import javax.naming.NamingException;
import javax.persistence.LockModeType;

import za.co.yellowfire.domain.racing.Club;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.FacesUtil;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Named("ClubConverter")
public class ClubConverter extends AbstractConverter implements Converter {

    /**
     * Default constructor
     */
    public ClubConverter() { }

    /**
     * Constructs the converter with the domain manager to use
     * @param manager The domain manager
     */
    public ClubConverter(DomainManager manager) {
        super(manager);
    }

    /**
	 * Converts the value into a Club instance by looking up the value as the id of the Club
	 */
	@Override public Object getAsObject(FacesContext context, UIComponent component, String value) {		
		if (value == null) { 
			LOGGER.debug("ClubConverter.getAsObject() : value is null");
			return null; 
		}
		if (value.equals("")) { 
			LOGGER.debug("ClubConverter.getAsObject() : value is empty string");
			return null; 
		}
		
		Long id = null;
		try {
			LOGGER.debug("ClubConverter.getAsObject() : parsing value " + value + " to a long");
			id = Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new ConverterException("Unable to convert " + value + " to a Long", e);
		}
		
		LOGGER.debug("ClubConverter.getAsObject() : looking up club with id " + id);
		Club club;
		try {
			club = (Club) getDomainManager().find(Club.class, id, LockModeType.OPTIMISTIC);
		} catch (NamingException e) {
			LOGGER.error("Unable to lookup club because RaceManager could not be resolved", e);
			FacesUtil.addErrorMessage(context, component.getClientId(), "Converter Error", e);
			throw new ConverterException("Unable to lookup club because RaceManager could not be resolved", e);
		} catch (Throwable e) {
			LOGGER.error("Unable to lookup club", e);
			FacesUtil.addErrorMessage(context, component.getClientId(), "Converter Error", e);
			throw new ConverterException("Unable to lookup club", e);
		}
		LOGGER.debug("ClubConverter.getAsObject() : found club " + club);
		return club;
	}

	/**
	 * Converts the Club into a String by formating the id as a String
	 */ 
	@Override public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null) { 
			LOGGER.debug("ClubConverter.getAsString() : value is null");
			return null; 
		}
		if (!(value instanceof Club)) {
			LOGGER.debug("ClubConverter.getAsString() : value is not a instance of Club");
			return null; 
		}
		
		String ret = ((Club) value).getId().toString();
		LOGGER.debug("ClubConverter.getAsString() : returning " + ret);
		return ret;
	}
}
