package za.co.yellowfire.domain.racing;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "RaceSponsor")
@Access(AccessType.FIELD)
@Table(name = "race_sponsor", schema = "rce")
@XmlAccessorType(XmlAccessType.FIELD)
public class RaceSponsor implements Serializable {
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "race_sponsor_id", nullable = false, insertable = true, updatable = true)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Column(name = "race_sponsor_name", nullable = false, insertable = true, updatable = true, length = 256)
    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlTransient
    @ManyToOne(targetEntity = Race.class)
    @JoinColumn(name = "race_id", referencedColumnName = "race_id")
    private Race race;

    @XmlElement(name = "sponsor-link")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = GraphicLink.class)
    @JoinColumn(name="graphic_link_id", nullable=true, updatable=true, referencedColumnName = "graphic_link_id")
    private GraphicLink sponsorLink;

    public RaceSponsor() {}

    public RaceSponsor(String name, GraphicLink sponsorLink) {
        this.name = name;
        this.sponsorLink = sponsorLink;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public GraphicLink getSponsorLink() {
        return sponsorLink;
    }

    public void setSponsorLink(GraphicLink sponsorLink) {
        this.sponsorLink = sponsorLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaceSponsor that = (RaceSponsor) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (race != null ? !race.equals(that.race) : that.race != null) return false;
        if (sponsorLink != null ? !sponsorLink.equals(that.sponsorLink) : that.sponsorLink != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (race != null ? race.hashCode() : 0);
        result = 31 * result + (sponsorLink != null ? sponsorLink.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RaceSponsor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", race=" + race +
                ", sponsorLink=" + sponsorLink +
                '}';
    }
}
