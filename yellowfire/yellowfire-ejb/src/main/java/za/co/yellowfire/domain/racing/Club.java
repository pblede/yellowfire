package za.co.yellowfire.domain.racing;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * @author Mark Ashworth
 * @version 0.0.1
 */
@Entity(name = "Club")
@Access(AccessType.FIELD)
@Table(name = "club", schema = "rce")
@NamedQueries(
        @NamedQuery(
            name="qry.clubs",
            query="select c from Club c"/*,
            hints={
                @QueryHint(name=QueryHints.RESULT_SET_TYPE, value=ResultSetType.ForwardOnly),
                @QueryHint(name=QueryHints.SCROLLABLE_CURSOR, value="true")}*/
        )
)
@XmlAccessorType(XmlAccessType.FIELD)
public class Club implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String QRY_CLUBS = "qry.clubs";
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_CODE = "code";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DISTRICT = "district";
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id", nullable = false, insertable = true, updatable = true)
    @XmlAttribute(name = "id", required = false)
    private Long id;

	@XmlElement(name = "district")
    @ManyToOne(fetch = FetchType.EAGER,optional = false, targetEntity = District.class)
    @JoinColumn(name = "district_id", nullable = false, updatable = true, referencedColumnName = "district_id")
    private District district;
	
	@Basic
	@XmlAttribute(name = "name", required = true)
	@Column(name = "club_name", nullable = false, insertable = true, updatable = true, length = 1024)
	private String name;
	
	@Column(name = "create_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
    @Column(name = "update_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Version
    @Column(name = "version")
    private int version;

    public Club() {
	
    }
    
	public Club(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setEntityUpdated() {
		this.updated = new Date();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Club other = (Club) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", district='" + (district != null ? district : "null") + '\'' +
                '}';
    }
}
