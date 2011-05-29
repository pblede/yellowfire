package za.co.yellowfire.ui.converter;

import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.domain.training.ContentType;
import za.co.yellowfire.ui.FacesUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.naming.NamingException;
import javax.persistence.LockModeType;

public class ContentTypeConverter extends AbstractConverter implements Converter {
	private static final String SELECT = "Select...";
    /**
	 * Converts the value into a DomainObject instance by looking up the value as the id of the Club
	 */
	@Override public Object getAsObject(FacesContext context, UIComponent component, String value) {		
		if (value == null) {
			return null; 
		}
		if (value.equals("")) { 
			return null;
		}

		Long id;
		try {
			id = Long.parseLong(value.equals(SELECT) ? "-1" : value);
		} catch (NumberFormatException e) {
			throw new ConverterException("Unable to convert " + value + " to a Long", e);
		}
		
		DomainObject object;
		try {
			object = (DomainObject) getDomainManager().find(ContentType.class, id, LockModeType.OPTIMISTIC);
		    return object;
		} catch (NamingException e) {
			FacesUtil.addErrorMessage(context, component.getClientId(), "Converter Error", e);
			throw new ConverterException("Unable to lookup content type because DomainManager could not be resolved", e);
		} catch (Throwable e) {
			FacesUtil.addErrorMessage(context, component.getClientId(), "Converter Error", e);
			throw new ConverterException("Unable to lookup content type", e);
		}
	}

	/**
	 * Converts the DomainObject into a String by formating the id as a String
	 */ 
	@Override public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null) {
			return null;
		}
		if (!(value instanceof DomainObject)) {
			return SELECT;
		}
		
		return ((DomainObject) value).getId().toString();
	}
}
