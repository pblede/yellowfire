package za.co.yellowfire.domain.geocode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GeocodeGeometry implements Serializable {

    @XmlElement(name = "location")
    private GeocodeLocation location;

    @XmlElement(name = "location_type")
    private GeocodeLocationType locationType;

    @XmlElement(name = "viewport")
    private GeocodeViewport viewport;

    public GeocodeGeometry() {}

    public GeocodeGeometry(GeocodeLocation location) {
        this.location = location;
    }

    public GeocodeLocation getLocation() {
        return location;
    }

    public void setLocation(GeocodeLocation location) {
        this.location = location;
    }

    public GeocodeLocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(GeocodeLocationType locationType) {
        this.locationType = locationType;
    }

    public GeocodeViewport getViewport() {
        return viewport;
    }

    public void setViewport(GeocodeViewport viewport) {
        this.viewport = viewport;
    }
}
