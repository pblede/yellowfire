package za.co.yellowfire.domain.profile;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class PropertyConverter implements Converter {
    private static final long serialVersionUID = 1L;
	private DatabaseMapping databaseMapping;
    private PropertyTypeConverter typeConverter = new PropertyTypeConverter();

    @Override
    public Object convertObjectValueToDataValue(Object o, Session session) {
        if (databaseMapping.getAttributeName().equals(Property.FIELD_VALUE)) {
            return typeConverter.convertObjectValueToDataValue(o, session);
        }
        return o;
    }

    @Override
    public Object convertDataValueToObjectValue(Object o, Session session) {
        if (databaseMapping.getAttributeName().equals(Property.FIELD_VALUE)) {
            return typeConverter.convertDataValueToObjectValue(o, session);
        }
        return o;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public void initialize(DatabaseMapping databaseMapping, Session session) {
        this.databaseMapping = databaseMapping;
    }
}
