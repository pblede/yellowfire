package za.co.yellowfire.domain.result;

import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.ObjectTypeConverter;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.domain.racing.Race;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Result")
@Access(AccessType.FIELD)
@Table(name = "result", schema = "rce")
@NamedQueries(
        @NamedQuery(
            name="qry.result.calendar",
            query="select r from Result r where r.person.id = :person and r.start >= :start and r.start <= :end"/*,
            hints={
                @QueryHint(name=QueryHints.RESULT_SET_TYPE, value=ResultSetType.ForwardOnly),
                @QueryHint(name=QueryHints.SCROLLABLE_CURSOR, value="true")}*/
        )
)
@ObjectTypeConverter(
        name = "ResultTypeConverter",
        dataType = Integer.class,
        objectType = ResultType.class,
        conversionValues = {
            @ConversionValue(dataValue = "1", objectValue = "Race"),
            @ConversionValue(dataValue = "2", objectValue = "Training")
})
public class Result extends DomainEntity {
	private static final long serialVersionUID = 1L;

	public static final String QRY_RESULT_CALENDAR = "qry.result.calendar";
    
    public static final String FIELD_ID = "id";
    public static final String FIELD_RACE = "race";
    public static final String FIELD_PERSON = "person";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_START = "start";
    public static final String FIELD_END = "end";
    public static final String FIELD_TYPE = "type";
    
	public Result() {
		super();
	}

	public Result(Date start, Date end) {
		super();
		this.start = start;
		this.end = end;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id", nullable = false, insertable = true, updatable = true)
//    @XmlAttribute(name = "id", required = false)
    private Long id;
	
//	@XmlElement(name = "person")
	@ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Profile.class)
    @JoinColumn(name = "person_id", nullable = false, updatable = true, referencedColumnName = "person_id")
	private Profile person;
	
//	@XmlElement(name = "race")
	@ManyToOne(fetch = FetchType.EAGER, optional = true, targetEntity = Race.class)
    @JoinColumn(name = "race_id", nullable = true, updatable = true, insertable = true)
	private Race race;
	
//	@XmlAttribute(name = "name", required = true)
    @Column(name = "result_name", nullable = false, insertable = true, updatable = true)
	private String name;
	
//	@XmlAttribute(name = "start_time", required = true)
//	@XmlJavaTypeAdapter(value = DateTypeAdapter.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "result_start", nullable = false, insertable = true, updatable = true)
	private Date start;
	
//	@XmlAttribute(name = "end_time", required = true)
//	@XmlJavaTypeAdapter(value = DateTypeAdapter.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "result_end", nullable = false, insertable = true, updatable = true)
	private Date end;
	
//	@XmlAttribute(name = "type", required = true)
//  @Enumerated(EnumType.ORDINAL)
	@Column(name = "result_type_id", nullable = false, insertable = true, updatable = true)
	@Convert("ResultTypeConverter")
	private ResultType type;
	
//	@XmlAttribute(name = "pace", required = false)
	@Column(name = "result_pace", nullable = false, insertable = true, updatable = true)
	private Double pace;

    @Column(name = "result_distance", nullable = true, insertable = true, updatable = true)
    private double distance;

    @Column(name = "result_time_hours", nullable = true, insertable = true, updatable = true)
    private int timeTakenHours;

    @Column(name = "result_time_minutes", nullable = true, insertable = true, updatable = true)  
    private int timeTakenMinutes;

    @Column(name = "result_time_seconds", nullable = true, insertable = true, updatable = true)
    private int timeTakenSeconds;

    @Column(name = "result_notes", nullable = true, insertable = true, updatable = true, length = 256)
    private String notes;
    
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

	public Profile getPerson() {
		return person;
	}

	public void setPerson(Profile person) {
		this.person = person;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public ResultType getType() {
		return type;
	}

	public void setType(ResultType type) {
		this.type = type;
	}

	public Double getPace() {
		return pace;
	}

	public void setPace(Double pace) {
		this.pace = pace;
	}

    public int getTimeTakenHours() {
        return timeTakenHours;
    }

    public void setTimeTakenHours(int timeTakenHours) {
        this.timeTakenHours = timeTakenHours;
    }

    public int getTimeTakenMinutes() {
        return timeTakenMinutes;
    }

    public void setTimeTakenMinutes(int timeTakenMinutes) {
        this.timeTakenMinutes = timeTakenMinutes;
    }

    public int getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(int timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public int getTotalTimeTakenMinutes() {
        int minutes = getTimeTakenHours() * 60;
        minutes += getTimeTakenMinutes();
        return minutes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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
        return "Result{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", person='" + person + '\'' +
                ", race=" + race +
                ", name='" + name + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", timeTaken='" + timeTakenHours + ":" + timeTakenMinutes + ":" + timeTakenSeconds + '\'' +
                ", pace='" + (pace == null ? "null" : pace) + '\'' +
                ", distance='" + distance + '\'' +
                ", notes='" + (notes == null ? "" : notes)  + '\'' +
                '}';
    }
}
