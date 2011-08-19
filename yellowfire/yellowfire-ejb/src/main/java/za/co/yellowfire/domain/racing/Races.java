package za.co.yellowfire.domain.racing;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlRootElement(name="races")
public class Races {
    @XmlElement(name= "race")
    private List<Race> races = new ArrayList<Race>();

    public void add(Race race) {
        this.races.add(race);
    }

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }
}
