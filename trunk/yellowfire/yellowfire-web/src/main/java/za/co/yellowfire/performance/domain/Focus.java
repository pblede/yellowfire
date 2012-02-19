package za.co.yellowfire.performance.domain;

import za.co.yellowfire.domain.DomainEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Cacheable(true)
@Access(AccessType.FIELD)
@Entity(name = "Focus")
@Table(name = "performance_focus", schema = "cde")
@NamedQueries({
        @NamedQuery(
                name="qry.performance.focuses",
                query="select f from Focus f"
        )
})
public class Focus extends DomainEntity {
    private static final long serialVersionUID = 1L;

    public static final String QRY_PERFORMANCE_FOCUSES = "qry.performance.focuses";

    @Id
    @Column(name = "performance_focus_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Basic
    @Size(min = 1, max = 64)
    @Column(name = "performance_focus_description", nullable = false, insertable = true, updatable = true, length=64, precision = 0)
    @XmlAttribute(name = "description", required = true)
    private String description;

    @Version
    @Column(name = "version")
    private int version;

    public Focus() {}

    public Focus(Long id, String description) {
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

        Focus that = (Focus) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Focus{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", version=" + version +
                '}';
    }
}


