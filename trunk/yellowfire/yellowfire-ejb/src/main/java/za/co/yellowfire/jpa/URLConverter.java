package za.co.yellowfire.jpa;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class URLConverter implements Converter {
	private static final long serialVersionUID = 1L;
	
    private Session session;
    @Override
    public Object convertObjectValueToDataValue(Object objectValue, Session session) {
        if (objectValue instanceof URL) {
            return ((URL) objectValue).toExternalForm();
        }
        return objectValue.toString();
    }

    @Override
    public Object convertDataValueToObjectValue(Object dataValue, Session session) {
        if (dataValue instanceof String) {
            String data = (String) dataValue;
            if (!data.trim().equals("")) {
                try {
                    return new URL(data);
                } catch (MalformedURLException e) {
                    this.session.logMessage("Unable to convert " + data + " to an java.net.URL");
                }
            }
        }
        return null; 
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public void initialize(DatabaseMapping mapping, Session session) {
        this.session = session;
    }
}
