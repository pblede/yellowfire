package za.co.yellowfire.domain.racing;

import za.co.yellowfire.domain.DomainEntity;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "CategoryFee")
@Access(AccessType.FIELD)
@Table(name = "category_fee", schema = "rce")
//@XmlType(name = "fee", propOrder={"id", "value", "description"}, namespace="race")
public class CategoryFee extends DomainEntity {
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "category_fee_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    //@XmlTransient
    //@ManyToOne
    //@JoinColumn(name = "category_id")
    //private CourseCategory category;

    @Basic
    @Column(name = "fee_value", nullable = false, insertable = true, updatable = true, length = 10, precision = 2)
    @XmlAttribute(name = "value", required = true)
    private BigDecimal value;

    @Basic
    @Column(name = "fee_description", nullable = true, insertable = true, updatable = true, length = 1024, precision = 0)
    @XmlAttribute(name = "description", required = false)
    private String description;

    public CategoryFee() {
    }

    public CategoryFee(String description, BigDecimal value) {
        this.description = description;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

//    public CourseCategory getCategory() {
//        return category;
//    }
//
//    public void setCategory(CourseCategory category) {
//        this.category = category;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryFee that = (CategoryFee) o;

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
