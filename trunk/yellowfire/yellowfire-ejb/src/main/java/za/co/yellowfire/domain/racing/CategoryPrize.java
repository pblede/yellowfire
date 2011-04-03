package za.co.yellowfire.domain.racing;

import java.io.Serializable;

import javax.persistence.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "CategoryPrize")
@Access(AccessType.FIELD)
@Table(name = "category_prize", schema = "rce")
@XmlType(name = "prize", propOrder={"id", "value", "description"})
public class CategoryPrize implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "category_prize_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "id", required = false)
    private long id;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CourseCategory category;

    @Basic
    @Column(name = "prize_description", nullable = true, insertable = true, updatable = true, length = 1024, precision = 0)
    @XmlAttribute(name = "description", required = false)
    private String description;

    @Basic
    @Column(name = "prize_value", nullable = false, insertable = true, updatable = true, length = 1024, precision = 2)
    @XmlAttribute(name = "value", required = true)
    private String value;


    public CategoryPrize() {
    }

    public CategoryPrize(String description, String value) {
        this.description = description;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CourseCategory getCategory() {
        return category;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryPrize that = (CategoryPrize) o;

        //if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        //result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
