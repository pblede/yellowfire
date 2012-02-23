package za.co.yellowfire.employee.domain;

import za.co.yellowfire.domain.DomainEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Cacheable(true)
@Access(AccessType.FIELD)
@Entity(name = "TerminationReason")
@Table(name = "termination_reason", schema = "cde")
@NamedQueries({
        @NamedQuery(
                name="qry.termination.reasons",
                query="select r from TerminationReason r"
        )
})
public class TerminationReason extends DomainEntity {
    private static final long serialVersionUID = 1L;

    private static final String QRY_TERMINATION_REASONS = "qry.termination.reasons";

    @Id
    @Column(name = "termination_reason_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Basic
    @Size(min = 1, max = 64)
    @Column(name = "termination_reason_description", nullable = false, insertable = true, updatable = true, length=64, precision = 0)
    @XmlAttribute(name = "description", required = true)
    private String description;

    @Version
    @Column(name = "version")
    private int version;

    public TerminationReason() {}

    public TerminationReason(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TerminationReason that = (TerminationReason) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TerminationReason {" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", version=" + version +
                '}';
    }
}
