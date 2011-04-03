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
public class GeocodeViewport implements Serializable {

    @XmlElement(name = "northeast")
    private GeocodeNorthEast northEast;

    @XmlElement(name = "southwest")
    private GeocodeSouthWest southWest;

    public GeocodeNorthEast getNorthEast() {
        return northEast;
    }

    public void setNorthEast(GeocodeNorthEast northEast) {
        this.northEast = northEast;
    }

    public GeocodeSouthWest getSouthWest() {
        return southWest;
    }

    public void setSouthWest(GeocodeSouthWest southWest) {
        this.southWest = southWest;
    }

    @Override
    public String toString() {
        return "GeocodeViewport{" +
                "northEast=" + northEast +
                ", southWest=" + southWest +
                '}';
    }
}
