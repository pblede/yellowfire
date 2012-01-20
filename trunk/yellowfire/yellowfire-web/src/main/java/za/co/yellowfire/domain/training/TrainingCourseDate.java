package za.co.yellowfire.domain.training;

import org.eclipse.persistence.config.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.log.LogType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.Date;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 * TODO Add a validator to ensure Course and Venue is selected
 */
@Entity(name = "TrainingCourseDate")
@Access(AccessType.FIELD)
@Table(name = "course_date", schema = "cde")
@NamedQueries({
        @NamedQuery(
            name="qry.training.course_dates",
            query="select cd from TrainingCourseDate cd"
        ),
        @NamedQuery(
            name="qry.training.course_dates.refresh.cache",
            query="select cd from TrainingCourseDate cd",
            hints = {
                @QueryHint(name= QueryHints.REFRESH, value="true")
            }
        )
})
public class TrainingCourseDate extends DomainEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.DOMAIN.getCategory());
    
    public static final String QRY_COURSE_DATES = "qry.training.course_dates";
    public static final String QRY_COURSE_DATES_REFRESH_CACHE = "qry.training.course_dates.refresh.cache";

    @Id
    @Column(name = "course_date_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    //@XmlElement(name = "course")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = TrainingCourse.class)
    @JoinColumn(name = "course_id", nullable = false, updatable = true, referencedColumnName = "course_id")
    private TrainingCourse course;

    //@XmlElement(name = "venue")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Venue.class)
    @JoinColumn(name = "venue_id", nullable = false, updatable = true, referencedColumnName = "venue_id")
    private Venue venue;

    @Temporal(TemporalType.DATE)
    @Column(name = "course_start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "course_end_date")
    private Date endDate;

    @Temporal(TemporalType.TIME)
    @Column(name = "course_start_time")
    private Date startTime;

    @Temporal(TemporalType.TIME)
    @Column(name = "course_end_time")
    private Date endTime;

    @Version
    @Column(name = "version")
    private int version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingCourse getCourse() {
        LOGGER.info("TrainingCourse.getCourse() : " + course);
        return course;
    }

    public void setCourse(TrainingCourse course) {
        this.course = course;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return "CourseDate{" +
                "startDate=" + startDate +
                ", startTime=" + startTime +
                '}';
    }
}
