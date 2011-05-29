package za.co.yellowfire.domain;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableConstant;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.eclipse.persistence.config.QueryHints;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;


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
            name="qry.venues.in.proximity",
            query="select v from Venue v where v.gpsLatitude >= :lat1 and v.gpsLatitude <= :lat2 and v.gpsLongitude >= :lng1 and v.gpsLongitude <= :lng2",
            hints = {
                @QueryHint(name=QueryHints.REFRESH, value="true")
            }
        )
})
@Searchable
@SearchableConstant(name = "type", values = {"venue", "location"})
@XmlType(name = "venue", propOrder = {"id", "name", "address", "gpsLatitude", "gpsLongitude"})
public class Venue implements DomainObject {
	private static final long serialVersionUID = 1L;

    public static final String QRY_VENUES = "qry.venues";
    public static final String QRY_VENUES_IN_PROXIMITY = "qry.venues.in.proximity";
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venue_id", nullable = false, insertable = true, updatable = true, length = 19, precision = 0)
    @XmlAttribute(name = "id", required = false)
    @SearchableId
    private Long id;

    @Basic
    @Column(name = "venue_name", nullable = false, insertable = true, updatable = true, length =128, precision = 0)
    @XmlAttribute(name = "name", required = true)
    @SearchableProperty
    private String name;

    @Basic
    @Column(name = "venue_address", nullable = true, insertable = true, updatable = true, length = 512, precision = 0)
    @XmlAttribute(name = "address", required = true)
    @SearchableProperty
    private String address;

    @Transient
    private String gps;

    @Basic
    @Column(name = "venue_latitude", nullable = true, insertable = true, updatable = true)
    @XmlAttribute(name = "latitude", required = false)
    @SearchableProperty
    private Double gpsLatitude;

    @Basic
    @Column(name = "venue_longitude", nullable = true, insertable = true, updatable = true)
    @XmlAttribute(name = "longitude", required = false)
    @SearchableProperty
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
        setGps(gps);
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlTransient
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

    @XmlTransient
    public Double getGpsLongitude() {
        return this.gpsLongitude;
    }

    @XmlTransient
    public Double getGpsLatitude() {
        return this.gpsLatitude;
    }

    public void setGpsLongitude(Double value) {
        this.gpsLongitude = value;
    }

    public void setGpsLatitude(Double value) {
        this.gpsLatitude = value;
    }

    public boolean hasGpsCoordinates() {
        return (getGpsLatitude() != null && getGpsLongitude() != null) && (getGpsLatitude() != 0 && getGpsLongitude() != 0);
    }

    public static Map<String, Object> getProximityQueryParams(double latitude, double longitude, double radius) {
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("lat1", latitude - radius);
        params.put("lat2", latitude + radius);
        params.put("lng1", longitude - radius);
        params.put("lng2", longitude + radius);
        return params;
    }

    public static Map<String, Object> getProximityQueryParams(double fromLatitude, double toLatitude,  double fromLongitude, double toLongitude) {
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("lat1", fromLatitude);
        params.put("lat2", toLatitude);
        params.put("lng1", fromLongitude);
        params.put("lng2", toLongitude);
        return params;
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
