package za.co.yellowfire.domain.training;

import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.domain.racing.Race;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@XmlRootElement(name="venues")
public class Venues {
    @XmlElement(name= "venue")
    private List<Venue> venues = new ArrayList<Venue>();

    public Venues() {
    }

    public Venues(List<Venue> venues) {
        this.venues = venues;
    }

    public void add(Venue venue) {
        this.venues.add(venue);
    }

    @XmlTransient
    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }
}
