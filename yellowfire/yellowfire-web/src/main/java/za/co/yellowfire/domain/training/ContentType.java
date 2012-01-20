package za.co.yellowfire.domain.training;

import za.co.yellowfire.domain.DomainEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "ContentType")
@Access(AccessType.FIELD)
@Table(name = "content_type", schema = "cde")
@Cacheable(true)
@NamedQueries({
        @NamedQuery(
            name="qry.content.types",
            query="select ct from ContentType ct"
        )
})
public class ContentType extends DomainEntity {
    private static final long serialVersionUID = 1L;

    public static final String QRY_CONTENT_TYPES = "qry.content.types";

    @Id
    @Column(name = "content_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Basic @Size(min = 1, max = 64)
    @Column(name = "content_type_description", nullable = false, insertable = true, updatable = true, length=64, precision = 0)
    @XmlAttribute(name = "description", required = true)
    private String description;

    @Version
    @Column(name = "version")
    private int version;

    public ContentType() {}

    public ContentType(Long id, String description) {
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

        ContentType that = (ContentType) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ContentType{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", version=" + version +
                '}';
    }
}
