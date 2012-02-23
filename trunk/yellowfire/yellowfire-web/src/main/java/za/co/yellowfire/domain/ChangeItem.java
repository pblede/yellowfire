package za.co.yellowfire.domain;

import java.io.Serializable;

/**
 * A change item represents a property change to a domain object. This is used in conjunction with the change tracking
 * built into the domain objects so that these can be merged to the persistent store.
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class ChangeItem implements Serializable {
    private static final long serialVersionUID = 1;
    
    private String property;
    private Object value;

    public ChangeItem(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangeItem)) return false;

        ChangeItem that = (ChangeItem) o;

        if (property != null ? !property.equals(that.property) : that.property != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return property != null ? property.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ChangeItem {" +
                "property='" + property + '\'' +
                ", value=" + value +
                '}';
    }
}
