package za.co.yellowfire.domain.converter;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.domain.geocode.GeocodeResult;

import javax.faces.convert.ConverterException;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Converts a GeocodeResult into a Venue object instance
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class GeocodeResultConverter implements Converter {
    /**
     * Converts the GeocodeResult instance o into a instance of Venue
     * @param aClass The class of the object. only Venue.class is supported
     * @param o The instance of GeocodeResult to convert
     * @return The converted instance
     */
    @Override public Object convert(Class aClass, Object o) {
        if (o == null) {
            return null;
        } else if (!(o instanceof GeocodeResult)) {
            throw new ConverterException("Unsupported convertion of type " + o.getClass().getCanonicalName());
        }

        if (aClass == null) {
            throw new ConversionException("Class to convert to is null");
        } else if (!(aClass.equals(Venue.class))) {
            throw new ConversionException("Unsupported conversion from GeocodeResult to " + aClass.getCanonicalName());
        }

        DecimalFormat format = new DecimalFormat("##0.0######");

        GeocodeResult result = (GeocodeResult) o;

        try {
            Venue venue = new Venue();
            venue.setAddress(result.getFormattedAddress());
            venue.setGpsLatitude(format.parse(result.getGeometry().getLocation().getLatitude()).doubleValue());
            venue.setGpsLongitude(format.parse(result.getGeometry().getLocation().getLongitude()).doubleValue());
            venue.setName(result.getFormattedAddress());
            return venue;
        } catch (ParseException e) {
            throw new ConverterException("Unable to convert GeocodeResult because either GpsLongitude or GpsLatitude could not be parsed as a decimal");
        }
    }
}
