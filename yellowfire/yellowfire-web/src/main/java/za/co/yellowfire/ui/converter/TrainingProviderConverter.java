package za.co.yellowfire.ui.converter;

import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.domain.training.TrainingProvider;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.FacesUtil;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.persistence.LockModeType;
import java.io.Serializable;

@SessionScoped
@FacesConverter("TrainingProviderConverter")
public class TrainingProviderConverter extends AbstractConverter implements Converter, Serializable {
    private static final String SELECT = "Select...";

    @Inject
    private DomainManager manager;

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
		if (value.equals(SELECT)) {
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
			object = (DomainObject) manager.find(TrainingProvider.class, id, LockModeType.OPTIMISTIC);
		    return object;
		//} catch (NamingException e) {
		//	FacesUtil.addErrorMessage(context, component.getClientId(), "Converter Error", e);
		//	throw new ConverterException("Unable to lookup training provider because DomainManager could not be resolved", e);
		} catch (Throwable e) {
			FacesUtil.addErrorMessage(context, component.getClientId(), "Converter Error", e);
			throw new ConverterException("Unable to lookup training provider", e);
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
