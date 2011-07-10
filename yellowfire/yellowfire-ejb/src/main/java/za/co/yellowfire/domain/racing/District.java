package za.co.yellowfire.domain.racing;

import za.co.yellowfire.domain.DomainEntity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * @author Mark Ashworth
 * @version 0.0.1
 */
@Entity(name = "District")
@Access(AccessType.FIELD)
@Table(name = "district", schema = "rce")
@XmlAccessorType(XmlAccessType.FIELD)
public class District extends DomainEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id", nullable = false, insertable = true, updatable = true)
    @XmlAttribute(name = "id", required = false)
    private Long id;

	@Basic
	@XmlAttribute(name = "code", required = true)
	@Column(name = "district_code", nullable = false, insertable = true, updatable = true, length = 12)
	private String code;
	
	@Basic
	@XmlAttribute(name = "name", required = true)
	@Column(name = "district_name", nullable = false, insertable = true, updatable = true, length = 1024)
	private String name;
	
	@Column(name = "create_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
    @Column(name = "update_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
    public String toString() {
        return "District{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
