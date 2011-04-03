package za.co.yellowfire.domain.geocode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="GeocodeResponse")
public class GeocodeResponse implements Serializable {

    @XmlElement(name = "status")
    private GeocodeStatus status;

    @XmlElement(name = "result", type = GeocodeResult.class)
    private Collection<GeocodeResult> results;
    
    public GeocodeStatus getStatus() {
        return status;
    }

    public void setStatus(GeocodeStatus status) {
        this.status = status;
    }

    public Collection<GeocodeResult> getResults() {
        return results;
    }

    public void setResults(List<GeocodeResult> results) {
        this.results = results;
    }
}
