package za.co.yellowfire.domain.training;

import org.eclipse.persistence.config.QueryHints;
import za.co.yellowfire.domain.DomainEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "Outcome")
@Access(AccessType.FIELD)
@Table(name = "outcome", schema = "trn")
@NamedQueries({
        @NamedQuery(
            name="qry.outcomes",
            query="select o from Outcome o"
        ),
        @NamedQuery(
            name="qry.outcomes.refresh.cache",
            query="select o from Outcome o",
            hints = {
                @QueryHint(name= QueryHints.REFRESH, value="true")
            }
        )
})
public class Outcome extends DomainEntity {
    private static final long serialVersionUID = 1L;
    
    public static final String QRY_OUTCOMES = "qry.outcomes";
    public static final String QRY_OUTCOMES_REFRESH_CACHE = "qry.outcomes.refresh.cache";

    @Id
    @Column(name = "outcome_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Basic @Size(min = 1, max = 64)
    @Column(name = "outcome_description", nullable = false, insertable = true, updatable = true, length=64, precision = 0)
    @XmlAttribute(name = "description", required = true)
    private String description;

    @Version
    @Column(name = "version")
    private int version;

    public Outcome() {}

    public Outcome(Long id, String description) {
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
    public String toString() {
        return "Outcome{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", version=" + version +
                '}';
    }
}
