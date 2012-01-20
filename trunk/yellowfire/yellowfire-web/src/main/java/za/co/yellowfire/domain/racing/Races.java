package za.co.yellowfire.domain.racing;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The XML roow for the race domain model
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@XmlRootElement(name="races")
public class Races {
    @XmlElement(name= "race")
    private List<Race> races = new ArrayList<Race>();

    /**
     * Add a race to the root
     * @param race The race instance
     */
    public void add(Race race) {
        this.races.add(race);
    }

    /**
     * Returns the race instances
     * @return List of races
     */
    public List<Race> getRaces() {
        return races;
    }

    /**
     * Set the list of race instances
     * @param races The new list of race instances
     */
    public void setRaces(List<Race> races) {
        this.races = races;
    }
}
