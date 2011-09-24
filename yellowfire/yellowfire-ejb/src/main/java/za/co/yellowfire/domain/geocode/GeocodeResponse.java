package za.co.yellowfire.domain.geocode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Geocode response returned from Google Maps
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="GeocodeResponse")
public class GeocodeResponse implements Serializable {

    /** The Geocode status */
    @XmlElement(name = "status")
    private GeocodeStatus status;

    /** The results */
    @XmlElement(name = "result", type = GeocodeResult.class)
    private Collection<GeocodeResult> results;

    /**
     * Constructs the Geocode response
     */
    public GeocodeResponse() { }

    /**
     * Constructs the Geocode response from the status
     * @param status The Geocode status
     */
    public GeocodeResponse(GeocodeStatus status) {
        this.status = status;
    }

    /**
     * Constructs the Geocode response from the status and results
     * @param status The Geocode status
     * @param results The results
     */
    public GeocodeResponse(GeocodeStatus status, Collection<GeocodeResult> results) {
        this.status = status;
        this.results = new ArrayList<GeocodeResult>(results);
    }

    /**
     * The Geocode status
     * @return Geocode status
     */
    public GeocodeStatus getStatus() {
        return status;
    }

    /**
     * The Geocode status
     * @param status Geocode status
     */
    public void setStatus(GeocodeStatus status) {
        this.status = status;
    }

    /**
     * The Geocode results
     * @return A collection of results
     */
    public Collection<GeocodeResult> getResults() {
        return results;
    }

    /**
     * The Geocode results
     * @param results A collection of results
     */
    public void setResults(List<GeocodeResult> results) {
        this.results = results;
    }
}
