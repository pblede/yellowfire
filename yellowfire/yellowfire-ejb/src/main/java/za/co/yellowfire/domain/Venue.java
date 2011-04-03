package za.co.yellowfire.domain;

import org.eclipse.persistence.config.QueryHints;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;


/**
 * @author Mark P Ashworth
 * @version 0.0.1
 * TODO Need a filter between Sport venues and Training venues
 */
@Entity(name = "Venue")
@Access(AccessType.FIELD)
@Table(name = "venue", schema = "cde")
@NamedQueries({
        @NamedQuery(
            name="qry.venues",
            query="select v from Venue v"
        ),
        @NamedQuery(
            name="qry.venues.refresh.cache",
            query="select v from Venue v",
            hints = {
                @QueryHint(name=QueryHints.REFRESH, value="true")
            }
        )
})
//@XmlType(name = "venue", propOrder = {"id", "name", "address", "gps"})
public class Venue implements DomainObject {
	private static final long serialVersionUID = 1L;

    public static final String QRY_VENUES = "qry.venues";
    public static final String QRY_VENUES_REFRESH_CACHE = "qry.venues.refresh.cache";

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venue_id", nullable = false, insertable = true, updatable = true, length = 19, precision = 0)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Basic
    @Column(name = "venue_name", nullable = false, insertable = true, updatable = true, length =128, precision = 0)
    @XmlAttribute(name = "name", required = true)
    private String name;

    @Basic
    @Column(name = "venue_address", nullable = true, insertable = true, updatable = true, length = 512, precision = 0)
    @XmlAttribute(name = "address", required = true)
    private String address;

    @Basic
    @Column(name = "venue_gps", nullable = true, insertable = true, updatable = true, length = 128, precision = 0)
    @XmlAttribute(name = "gps", required = false)
    private String gps;

    @Transient
    private String gpsLatitude;

    @Transient
    private String gpsLongitude;

    @Version
    @Column(name = "version")
    private int version;

    /* Default constructor */
    public Venue() { }

    public Venue(String name, String address, String gps) {
        this.name = name;
        this.address = address;
        this.gps = gps;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    protected void formatGps() {
        setGps(this.gpsLongitude + ", " + this.gpsLatitude);
    }

    protected void parseGps() {
        if (getGps() != null) {
            String[] parts = getGps().split(",");
            if (parts.length == 2) {
                this.gpsLongitude = parts[0].trim();
                this.gpsLatitude = parts[1].trim();
            }
        }
    }

    public String getGpsLongitude() {
        if (this.gpsLongitude == null) {
            parseGps();
        }
        return this.gpsLongitude;
    }

    public String getGpsLatitude() {
        if (this.gpsLatitude == null) {
            parseGps();
        }
        return this.gpsLatitude;
    }

    public void setGpsLongitude(String value) {
        this.gpsLongitude = value;
        formatGps();
    }

    public void setGpsLatitude(String value) {
        this.gpsLatitude = value;
        formatGps();
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", gps='" + gps + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Venue other = (Venue) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
