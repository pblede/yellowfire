package za.co.yellowfire.domain.racing;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.*;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "CourseCategory")
@Access(AccessType.FIELD)
@Table(name = "course_category", schema = "rce", catalog = "race")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "category", propOrder={"id", "name", "fees", "prize"})
public class CourseCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "id", required = false)
    private long id;

    @Basic
    @Column(name = "category_name", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @XmlElement(name = "fee")
    @OneToMany(/*mappedBy = "category",*/ fetch = FetchType.EAGER, targetEntity = CategoryFee.class)
    @JoinColumn(name = "category_id", table = "category_fee")
    private Set<CategoryFee> fees;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    @XmlElement(name = "prize")
    private Set<CategoryPrize> prize;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course get_course() {
        return course;
    }

    public void set_course(Course course) {
        this.course = course;
    }
    
    public Set<CategoryFee> getFees() {
        return fees;
    }

    public List<CategoryFee> getFeesList() {
        return new ArrayList<CategoryFee>(fees);
    }

    public void setFees(Set<CategoryFee> fees) {
        this.fees = fees;
    }

    public void add(CategoryFee fee) {
        if (this.fees == null) this.fees = new HashSet<CategoryFee>();
        this.fees.add(fee);
    }

    public Set<CategoryPrize> getPrizes() {
        return prize;
    }

    public List<CategoryPrize> getPrizesList() {
        return new ArrayList<CategoryPrize>(prize);
    }

    public void setPrizes(Set<CategoryPrize> prizes) {
        this.prize = prizes;
    }

    public void add(CategoryPrize prize) {
        if (this.prize == null) this.prize = new HashSet<CategoryPrize>();
        this.prize.add(prize);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseCategory that = (CourseCategory) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (course != null ? !course.equals(that.course) : that.course != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (course != null ? course.hashCode() : 0);
        return result;
    }
}
