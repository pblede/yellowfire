package za.co.yellowfire.domain.racing;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import za.co.yellowfire.domain.*;
import za.co.yellowfire.domain.jaxb.DateTypeAdapter;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "Race")
@Access(AccessType.FIELD)
@Table(name = "race", schema = "rce")
@NamedQueries({
        @NamedQuery(
            name="qry.upcoming.races",
            query="select r from Race r where r.date >= :date"/*,
            hints={
                @QueryHint(name=QueryHints.RESULT_SET_TYPE, value=ResultSetType.ForwardOnly),
                @QueryHint(name=QueryHints.SCROLLABLE_CURSOR, value="true")}*/
        ),
        @NamedQuery(
            name="qry.races.by.date",
            query="select r from Race r where r.date = :date"/*,
            hints={
                @QueryHint(name=QueryHints.RESULT_SET_TYPE, value=ResultSetType.ForwardOnly),
                @QueryHint(name=QueryHints.SCROLLABLE_CURSOR, value="true")}*/
        ),
        @NamedQuery(
            name="qry.races.by.id",
            query="select r from Race r where r.id = :id"/*,
            hints={
                @QueryHint(name=QueryHints.RESULT_SET_TYPE, value=ResultSetType.ForwardOnly),
                @QueryHint(name=QueryHints.SCROLLABLE_CURSOR, value="true")}*/
        )
		}
)
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "race", propOrder = {"id", "name", "date", "courses", "venue", "notes"}, namespace="race")
public class Race extends DomainEntity {
    private static final long serialVersionUID = 1L;
    //private static ExpressionBuilder BUILDER = new ExpressionBuilder();
    
    public static final String QRY_UPCOMING_RACES = "qry.upcoming.races";
    public static final String QRY_RACES_FOR_DATE = "qry.races.by.date";
    public static final String QRY_RACES_FOR_ID = "qry.races.by.id";
    
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_DISTRICT = "district";
    public static final String FIELD_CLUB = "club";
    public static final String FIELD_COURSES = "courses";
    public static final String FIELD_NOTES = "notes";
    public static final String FIELD_VENUE = "venue";
    public static final String FIELD_EVENT_LINK = "eventLink";
    public static final String FIELD_SPONSOR_LINKS = "sponsorLinks";
    public static final String[] BATCH_READ_ATTRIBUTES = {
        FIELD_COURSES,
        FIELD_NOTES,
        FIELD_SPONSOR_LINKS
    };
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "race_id", table = "race", nullable = false, insertable = true, updatable = true, length = 19, precision = 0)
    @XmlAttribute(name = "id", required = false)
    private Long id;
    
    @Basic
    @Column(name = "race_name", table = "race", nullable = false, insertable = true, updatable = true, length = 2147483647, precision = 0)
    @XmlAttribute(name = "name", required = true)
    private String name;
    
    @XmlJavaTypeAdapter(value = DateTypeAdapter.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "race_date", table = "race", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @XmlAttribute(name = "date", required = true)
    private Date date;
    
    @XmlElement(name = "district")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = District.class)
    @JoinColumn(name = "district_id", nullable = false, updatable = true, referencedColumnName = "district_id")
    private District district;
   
    @XmlElement(name = "club")
    @ManyToOne(fetch = FetchType.EAGER, optional = true, targetEntity = Club.class)
    @JoinColumn(name = "club_id", nullable = true, updatable = true)
    private Club club;
    
    @XmlElement(name = "course")
    @OneToMany(mappedBy = "race", fetch = FetchType.EAGER, targetEntity = Course.class)
    @OrderBy(value = "startTime")
    private List<Course> courses = new ArrayList<Course>();
    
    @XmlElement(name = "note")
    @OneToMany(mappedBy = "race", fetch = FetchType.EAGER, targetEntity = Note.class)
    @OrderBy(value = "type")
    private List<Note> notes = new ArrayList<Note>();
    
    @XmlElement(name = "venue")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Venue.class)
    @JoinColumn(name = "venue_id", nullable = false, updatable = true, referencedColumnName = "venue_id")
    private Venue venue;
    
    @XmlElement(name = "event-link")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = GraphicLink.class)
    @JoinColumn(name = "graphic_link_id", nullable = true, updatable = true, referencedColumnName = "graphic_link_id")
    private GraphicLink eventLink;
    
    @XmlElement(name = "sponsor-link")
    @OneToMany(mappedBy = "race", fetch = FetchType.EAGER, targetEntity = RaceSponsor.class)
    @OrderBy(value = "id")
    private List<RaceSponsor> sponsors;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public List<Course> getCourses() {
        return courses;
    }

    @XmlTransient
    public Course[] getCoursesArray() {
        return courses.toArray(new Course[courses.size()]);
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Note> getNotes() {
        return notes;
    }

    @XmlTransient
    public List<Note> getNotes(final NoteType type) {
        FilterIterator iter = new FilterIterator(notes.iterator());
        iter.setPredicate(new Predicate()  {

            @Override
            public boolean evaluate(Object o) {
                Note note = (Note) o;
                return note.getType() == type;
            }
        });
        List<Note> values = new ArrayList<Note>();
        while (iter.hasNext()) {
            values.add((Note) iter.next());
        }
        return values;
    }

    public List<Note> getRegistrationNotes() {
        return getNotes(NoteType.Registration);
    }

    public List<Note> getGeneralNotes() {
        return getNotes(NoteType.General);
    }

    public List<Note> getRulesNotes() {
        return getNotes(NoteType.Rules);
    }

    public List<Note> getAccomodationNotes() {
        return getNotes(NoteType.Accomodation);
    }

    public List<Note> getMedalsNotes() {
        return getNotes(NoteType.Medals);
    }

    public List<Note> getPrizesNotes() {
        return getNotes(NoteType.Prizes);
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public GraphicLink getEventLink() {
        return eventLink;
    }

    public void setEventLink(GraphicLink eventLink) {
        this.eventLink = eventLink;
    }

    public void setEventLink(URL linkURL, URL imageURL, Integer scalePercentage, String alternativeText) {
        this.eventLink = new GraphicLink(linkURL, imageURL, scalePercentage, alternativeText);
    }

    public List<RaceSponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<RaceSponsor> sponsors) {
        this.sponsors = sponsors;
    }

    public boolean isNew() {
    	if (getCreated() != null) {
	    	Calendar cal = new GregorianCalendar();
	    	cal.add(Calendar.MONTH, -1);
	    	
	    	Calendar created = new GregorianCalendar();
	    	created.setTime(getDate());
	    	
	    	return created.after(cal);
    	}
    	return false;
    }
    
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Race race = (Race) o;

        if (name != null ? !name.equals(race.name) : race.name != null) {
            return false;
        }
        if (date != null ? !date.equals(race.date) : race.date != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Race [id=" + id + ", name=" + name + ", date=" + date
                + ", courses=" + courses + ", notes=" + notes + ", venue="
                + venue + "]";
    }
    
//    public static Expression EXPRESSION_UPCOMING_RACES() {
//        return BUILDER.get(FIELD_DATE).greaterThanEqual(new Date());
//    }
//    
//    public static List<Expression> ORDERBY_DATE() {
//        return Arrays.asList(BUILDER.get(FIELD_DATE));
//    }
}
