package za.co.yellowfire.ui.converter;

import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.domain.training.TrainingCourse;
import za.co.yellowfire.ui.FacesUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.naming.NamingException;
import javax.persistence.LockModeType;

public class TrainingCourseConverter extends AbstractConverter implements Converter {
	/**
	 * Converts the value into a DomainObject instance by looking up the value as the id of the Club
	 */
	@Override public Object getAsObject(FacesContext context, UIComponent component, String value) {		
		if (value == null) { 
			LOGGER.debug("TrainingCourseConverter.getAsObject() : value is null");
			return null; 
		}
		if (value.equals("")) { 
			LOGGER.debug("TrainingCourseConverter.getAsObject() : value is empty string");
			return null; 
		}
		
		Long id = null;
		try {
			LOGGER.debug("TrainingCourseConverter.getAsObject() : parsing value " + value + " to a long");
			id = Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new ConverterException("Unable to convert " + value + " to a Long", e);
		}
		
		LOGGER.debug("TrainingCourseConverter.getAsObject() : looking up club with id " + id);
		DomainObject object;
		try {
			object = (DomainObject) getDomainManager().find(TrainingCourse.class, id, LockModeType.OPTIMISTIC);
            LOGGER.info("TrainingCourseConverter.getAsObject() : found object " + object);
		    return object;
		} catch (NamingException e) {
			LOGGER.error("Unable to lookup club because RaceManager could not be resolved", e);
			FacesUtil.addErrorMessage(context, component.getClientId(), "Converter Error", e);
			throw new ConverterException("Unable to lookup club because RaceManager could not be resolved", e);
		} catch (Throwable e) {
			LOGGER.error("Unable to lookup club", e);
			FacesUtil.addErrorMessage(context, component.getClientId(), "Converter Error", e);
			throw new ConverterException("Unable to lookup club", e);
		}
	}

	/**
	 * Converts the DomainObject into a String by formating the id as a String
	 */ 
	@Override public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null) {
            LOGGER.info("NULL");
			return null;
		}
		if (!(value instanceof DomainObject)) {
            LOGGER.info("TrainingCourseConverter : -1");
			return "-1";
		}
		
		String ret = ((DomainObject) value).getId().toString();
        LOGGER.info("TrainingCourseConverter : " + ret);
		return ret;
	}
}
