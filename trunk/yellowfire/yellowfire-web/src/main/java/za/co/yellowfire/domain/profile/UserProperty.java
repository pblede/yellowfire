package za.co.yellowfire.domain.profile;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "UserProperty")
@Access(AccessType.FIELD)
@Table(name = "vw_user_property", schema = "cde")
@NamedQueries(
        @NamedQuery(
            name="qry.user.property.like.name",
            query="select u from UserProperty u where u.name like :name"/*,
            hints={
                @QueryHint(name=QueryHints.RESULT_SET_TYPE, value=ResultSetType.ForwardOnly),
                @QueryHint(name=QueryHints.SCROLLABLE_CURSOR, value="true")}*/
        )
)
@Converter(name="UserPropertyConverter", converterClass=PropertyConverter.class)
@XmlAccessorType(XmlAccessType.FIELD)
public class UserProperty implements Property {
    private static final long serialVersionUID = 1L;
    
    public static final String QRY_USER_PROPERTY_LIKE_NAME = "qry.user.property.like.name";
        
    @Id
    @Column(name = "property_name", nullable = false, insertable = true, updatable = true, length = 256, precision = 0)
    @XmlAttribute(name = "name", required = true)
    private String name;
    
    @Basic
    @Column(name = "property_value", nullable = false, insertable = true, updatable = true, length = 1024, precision = 0)
    @XmlAttribute(name = "value", required = true)
    @Convert("UserPropertyConverter")
    private String value;
    
    @Basic
    @Column(name = "property_type", nullable = true, insertable = true, updatable = true, length = 1024, precision = 0)
    @XmlAttribute(name = "type", required = true)
    private String type;
    

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		if (this.value != null) {
			this.type = value.getClass().getCanonicalName();
		} else {	
			this.type = null;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserProperty prop = (UserProperty) o;

        if (name != null ? !name.equals(prop.name) : prop.name != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserProperty [name=" + name + ", type=" + type + ", value=" + value
                + "]";
    }
}
