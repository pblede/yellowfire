package za.co.yellowfire.domain.profile;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Date;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class PropertyTypeConverter implements Converter {
	private static final long serialVersionUID = 1L;
	
    @Override
    public Object convertObjectValueToDataValue(Object o, Session session) {
    	if (o != null) {
    		if (o instanceof Date) {
    			return o.getClass().getCanonicalName() + ":" + ((Date) o).getTime();
    		} else { 
    			return o.getClass().getCanonicalName() + ":" + o.toString();
    		}
    	}
    	return null;
    }

    @Override
    public Object convertDataValueToObjectValue(Object o, Session session) {
           	
    	if (o instanceof String) {
    		String s = (String) o;
    		
    		if (s.contains(":")) {
    			String parts[] = s.split(":");
    			if (parts[0].equalsIgnoreCase(String.class.getCanonicalName())) {
    				return parts[1];
    			} else if (parts[0].equalsIgnoreCase(Long.class.getCanonicalName())) {
    				return Long.parseLong(parts[1]);
    			} else if (parts[0].equalsIgnoreCase(Integer.class.getCanonicalName())) {
    				return Integer.parseInt(parts[1]);
    			} else if (parts[0].equalsIgnoreCase(Double.class.getCanonicalName())) {
    				return Double.parseDouble(parts[1]);
    			} else if (parts[0].equalsIgnoreCase(Float.class.getCanonicalName())) {
    				return Float.parseFloat(parts[1]);
    			} else if (parts[0].equalsIgnoreCase(BigDecimal.class.getCanonicalName())) {
    				return new BigDecimal(parts[1]);
    			} else if (parts[0].equalsIgnoreCase(BigInteger.class.getCanonicalName())) {
    				return new BigInteger(parts[1]);
    			} else if (parts[0].equalsIgnoreCase(Boolean.class.getCanonicalName())) {
    				return Boolean.parseBoolean(parts[1]);
    			} else if (parts[0].equalsIgnoreCase(Date.class.getCanonicalName())) {
    				return new Date(Long.parseLong(parts[1]));
    			} else if (parts[0].equalsIgnoreCase(Time.class.getCanonicalName())) {
    				return new Time(Long.parseLong(parts[1]));
    			}
    		}
    		
    	}
    	return o;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public void initialize(DatabaseMapping databaseMapping, Session session) {}
}
