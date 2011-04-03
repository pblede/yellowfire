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
@Entity(name = "SystemProperty")
@Access(AccessType.FIELD)
@Table(name = "vw_system_property", schema = "cde")
@NamedQueries({
        @NamedQuery(
            name="qry.system.property.like.name",
            query="select u from SystemProperty u where u.name like :name"
        ),
        @NamedQuery(
            name="qry.system.property.name",
            query="select u from SystemProperty u where u.name = :name"
        )
})
@Converter(name="SystemPropertyConverter", converterClass=PropertyConverter.class)
@XmlAccessorType(XmlAccessType.FIELD)
public class SystemProperty implements Property {
    private static final long serialVersionUID = 1L;
    
    public static final String QRY_SYSTEM_PROPERTY_LIKE_NAME = "qry.system.property.like.name";
    public static final String QRY_SYSEM_PROPERTY_NAME = "qry.system.property.name";

    @Id
    @Column(name = "property_name", nullable = false, insertable = true, updatable = true, length = 256, precision = 0)
    @XmlAttribute(name = "name", required = true)
    private String name;
    
    @Basic
    @Column(name = "property_value", nullable = false, insertable = true, updatable = true, length = 1024, precision = 0)
    @XmlAttribute(name = "value", required = true)
    @Convert("SystemPropertyConverter")
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

        SystemProperty prop = (SystemProperty) o;

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
