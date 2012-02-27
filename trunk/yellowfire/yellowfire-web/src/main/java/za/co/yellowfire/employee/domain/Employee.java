package za.co.yellowfire.employee.domain;

import za.co.yellowfire.common.domain.Address;
import za.co.yellowfire.common.domain.PersonName;
import za.co.yellowfire.common.domain.Title;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.jaxb.DateTypeAdapter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Cacheable(true)
@Access(AccessType.FIELD)
@Entity(name = "Employee")
@Table(name = "employee", schema = "cde")
@NamedQueries({
        @NamedQuery(
                name="qry.employees",
                query="select e from Employee e"
        ),
        @NamedQuery(
                name="qry.employees.by.code",
                query="select e from Employee e where e.code = :code"
        ),
        @NamedQuery(
                name="qry.unlinked.profiles",
                query="select p from User p where p.id not in (select e.profile.id from Employee e)"
        )
})
public class Employee extends DomainEntity {
    private static final long serialVersionUID = 1L;

    public static final String QRY_EMPLOYEES = "qry.employees";
    public static final String QRY_EMPLOYEES_BY_CODE = "qry.employees.by.code";

    public static final String QRY_UNLINKED_PROFILES = "qry.unlinked.profiles";

    public static final String FIELD_CODE = "code";
    public static final String FIELD_JOINED = "joined";
    public static final String FIELD_TERMINATED = "terminated";
    public static final String FIELD_TERMINATION_REASON = "terminationReason";
    public static final String FIELD_PROFILE = "profile";

    public static final String[] TRACKED = new String[] {
            FIELD_PROFILE,
    };
    
    @Id
    @Column(name = "employee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @NotNull
    @Column(name = "employee_number", length = 32)
    private String code;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Profile profile;

    @XmlJavaTypeAdapter(value=DateTypeAdapter.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "date_joined", nullable = false, insertable = true, updatable = true)
    @XmlAttribute(name = "joined", required = true)
    private Date joined;

    @XmlJavaTypeAdapter(value=DateTypeAdapter.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "date_terminated", nullable = false, insertable = true, updatable = true)
    @XmlAttribute(name = "terminated", required = true)
    private Date terminated;

    @ManyToOne
    @JoinColumn(name = "termination_reason_id")
    private TerminationReason terminationReason;
    
    @Version
    @Column(name = "version")
    private int version;

    public Employee() {}

    public Employee(String code, Profile profile, Date joined) {
        this.code = code;
        this.profile = profile;
        this.joined = joined;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getFirstName() {
        return profile != null  ? profile.getFirstName() : null;
    }

    public String getLastName() {
        return profile != null  ? profile.getLastName() : null;
    }
    
    public String getIdNumber() {
        return profile != null  ? profile.getIdNumber() : null;
    }

    public Date getJoined() {
        return joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }

    public Date getTerminated() {
        return terminated;
    }

    public void setTerminated(Date terminated) {
        this.terminated = terminated;
    }

    public TerminationReason getTerminationReason() {
        return terminationReason;
    }

    public void setTerminationReason(TerminationReason terminationReason) {
        this.terminationReason = terminationReason;
    }

    @Override
    public String[] getTrackedProperties() {
        return TRACKED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee that = (Employee) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Employee {" +
                "id=" + id +
                ", code=" + code +
                ", joined=" + joined +
                ", terminated=" + (terminated != null ? terminated : "") +
                ", terminationReason=" + (terminationReason != null ? terminationReason : "") +
                '}';
    }
}
