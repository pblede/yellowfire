package za.co.yellowfire.domain.goal;

import za.co.yellowfire.domain.DomainEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.Date;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "Goal")
@Access(AccessType.FIELD)
@Table(name = "goal", schema = "cde")
@Cacheable(false)
@NamedQueries({
        @NamedQuery(
            name="qry.goals",
            query="select g from Goal g"
        )
})
public class Goal extends DomainEntity {
    private static final long serialVersionUID = 1L;
    public static final String QRY_GOALS = "qry.goals";

    @Id
    @Column(name = "goal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Size(min = 1, max = 64)
    @Column(name = "goal_name", length = 64)
    private String name;

    @Size(min = 0, max = 512)
    @Column(name = "goal_description", length = 512)
    private String description;

    @Size(min = 1, max = 512)
    @Column(name = "goal_measurement", length = 512)
    private String measurement;

    @Temporal(TemporalType.DATE)
    @Column(name = "goal_defined")
    private Date defined;

    @Temporal(TemporalType.DATE)
    @Column(name = "goal_start")
    private Date start;

    @Temporal(TemporalType.DATE)
    @Column(name = "goal_deadline")
    private Date deadline;


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
}
