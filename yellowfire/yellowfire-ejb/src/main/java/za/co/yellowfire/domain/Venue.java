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

    @Transient
    private String gps;

    @Basic
    @Column(name = "venue_latitude", nullable = true, insertable = true, updatable = true)
    @XmlAttribute(name = "latitude", required = false)
    private Double gpsLatitude;

    @Basic
    @Column(name = "venue_longitude", nullable = true, insertable = true, updatable = true)
    @XmlAttribute(name = "longitude", required = false)
    private Double gpsLongitude;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "venue_type", nullable = false, insertable = true, updatable = true)
    private VenueType type;

    @Version
    @Column(name = "version")
    @SuppressWarnings("unused")
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
        if (getGpsLatitude() != null && getGpsLongitude() != null) {
            return getGpsLatitude() + "," + getGpsLongitude();
        }
        return null;
    }

    public void setGps(String gps) {
        parseGps(gps);
    }

    protected void parseGps(String gps) {
        if (gps != null) {
            String[] parts = gps.split(",");
            if (parts.length == 2) {
                setGpsLongitude(Double.valueOf(parts[0].trim()));
                setGpsLatitude(Double.valueOf(parts[1].trim()));
            }
        }
    }

    public Double getGpsLongitude() {
        return this.gpsLongitude;
    }

    public Double getGpsLatitude() {
        return this.gpsLatitude;
    }

    public void setGpsLongitude(Double value) {
        this.gpsLongitude = value;
    }

    public void setGpsLatitude(Double value) {
        this.gpsLatitude = value;
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
        return !((this.name == null) ? (other.name != null) : !this.name.equals(other.name));
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
