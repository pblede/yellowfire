package za.co.yellowfire.domain.racing;

import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.profile.Profile;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * 
 * @author Mark Ashworth
 * @version 0.0.1
 */
@Entity(name = "RaceRegistration")
@Access(AccessType.FIELD)
@Table(name = "race_registration", schema = "rce")
@XmlAccessorType(XmlAccessType.FIELD)
public class RaceRegistration extends DomainEntity {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "race_registration_id", nullable = false, insertable = true, updatable = true)
    @XmlAttribute(name = "id", required = false)
    private Long id;
	
	@XmlElement(name = "race")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Race.class)
    @JoinColumn(name = "race_id", nullable = false, updatable = true, referencedColumnName = "race_id")
    private Race race;
	
	@XmlElement(name = "person")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "person_id", nullable = false, updatable = true, referencedColumnName = "person_id")
    private Profile person;
	
	@XmlElement(name = "fee")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_fee_id", nullable = false, updatable = true, referencedColumnName = "category_fee_id")
	private CategoryFee fee;
	
	@Column(name = "create_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
    @Column(name = "update_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

	public Long getId() {
		return id;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public Profile getPerson() {
		return person;
	}

	public void setPerson(Profile person) {
		this.person = person;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
