package za.co.yellowfire.domain.racing;

import za.co.yellowfire.Format;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.jaxb.TimeTypeAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "Course")
@Access(AccessType.FIELD)
@Table(name = "course", schema = "rce")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "course", propOrder = {"id", "categories","distance", "startTime", "description"}, namespace="race")
public class Course extends DomainEntity {
	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @XmlElement(name = "category")
    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, targetEntity = CourseCategory.class)
    private List<CourseCategory> categories;

    @XmlJavaTypeAdapter(value=TimeTypeAdapter.class)
    @XmlAttribute(name = "starttime", required = true)
    @Temporal(value = TemporalType.TIME)
    @Column(name = "course_starttime", nullable = false, insertable = true, updatable = true, length = 16, precision = 0)
    private Date startTime;

    @Basic
    @Column(name = "course_description", nullable = true, insertable = true, updatable = true, length = 2147483647, precision = 0)
    @XmlAttribute(name = "description", required = false)
    private String description;

    @Basic
    @Column(name = "course_distance", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @XmlAttribute(name = "distance", required = true)
    private String distance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public List<CourseCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<CourseCategory> categories) {
        this.categories = categories;
    }

    public void add(CourseCategory category) {
        if (this.categories == null) this.categories = new ArrayList<CourseCategory>();
        this.categories.add(category);
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getStartTimeString() {
        Date dte = getStartTime();
        return (dte != null ? new SimpleDateFormat(Format.TIME.getFormat()).format(dte) : null);
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (race != null ? !race.equals(course.race) : course.race != null) return false;
        if (distance != null ? !distance.equals(course.distance) : course.distance != null) return false;
        if (startTime != null ? !startTime.equals(course.startTime) : course.startTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (race != null ? race.hashCode() : 0);
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        return result;
    }
}
