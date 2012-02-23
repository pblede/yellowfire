package za.co.yellowfire.domain.geocode;

import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.DomainObject;

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
public class GeocodeResult extends DomainEntity {

    @XmlElement(name = "address_component")
    private Collection<GeocodeAddressComponent> addressComponents;

    @XmlElement(name = "geometry")
    private GeocodeGeometry geometry;

    /**
     * The <strong>types[]</strong> array indicates the type of the returned result. This array contains a set of one or more tags
     * identifying the type of feature returned in the result. For example, a geocode of "Chicago" returns "locality"
     * which indicates that "Chicago" is a city, and also returns "political" which indicates it is a political entity.
     */
    @XmlElement(name = "type")
    private Collection<String> type;

    /**
     * <strong>formattedAddress</strong> is a string containing the human-readable address of this location.
     * Often this address is equivalent to the "postal address," which sometimes differs from country to country.
     * (Note that some countries, such as the United Kingdom, do not allow distribution of true postal addresses due to
     * licensing restrictions.) This address is generally composed of one or more address components.
     * For example, the address "111 8th Avenue, New York, NY" contains separate address components for "111" (the street
     * number, "8th Avenue" (the route), "New York" (the city) and "NY" (the US state).
     * These address components contain additional information as noted below.
     */
    @XmlElement(name = "formatted_address")
    private String formattedAddress;

    @Override
    public Serializable getId() {
        return null;
    }

    public Collection<GeocodeAddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(Collection<GeocodeAddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public GeocodeGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GeocodeGeometry geometry) {
        this.geometry = geometry;
    }

    public Collection<String> getType() {
        return type;
    }

    public void setType(Collection<String> type) {
        this.type = type;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }


}
