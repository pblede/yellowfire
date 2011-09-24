package za.co.yellowfire.domain.training;

import za.co.yellowfire.domain.DomainEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "TrainingProvider")
@Access(AccessType.FIELD)
@Table(name = "training_provider", schema = "cde")
@Cacheable(true)
@NamedQueries({
        @NamedQuery(
            name="qry.training.providers",
            query="select p from TrainingProvider p"
        )
})
public class TrainingProvider extends DomainEntity {

    public static final String QRY_TRAINING_PROVIDERS = "qry.training.providers";

    @Id
    @Column(name = "training_provider_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Column(name = "training_provider_code")
    private String code;

    @Column(name = "training_provider_description")
    private String name;

    @Version
    @Column(name = "version")
    @SuppressWarnings("unused")
    private int version;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrainingProvider that = (TrainingProvider) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TrainingProvider{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", version=" + version +
                '}';
    }
}
