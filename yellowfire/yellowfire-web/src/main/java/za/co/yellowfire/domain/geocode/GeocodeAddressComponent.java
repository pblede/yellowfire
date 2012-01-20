package za.co.yellowfire.domain.geocode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GeocodeAddressComponent implements Serializable {

    @XmlElement(name = "long_name")
    private String longName;
    @XmlElement(name = "short_name")
    private String shortName;
    @XmlElement(name = "type")
    private Collection<GeocodeAddressComponentType> types;

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Collection<GeocodeAddressComponentType> getTypes() {
        return types;
    }

    public void setTypes(Collection<GeocodeAddressComponentType> type) {
        this.types = type;
    }

    @Override
    public String toString() {
        return "GeocodeAddressComponent{" +
                "longName='" + longName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", type='" + types + '\'' +
                '}';
    }
}
