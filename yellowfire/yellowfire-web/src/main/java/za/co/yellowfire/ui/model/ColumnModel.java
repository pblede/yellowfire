package za.co.yellowfire.ui.model;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class ColumnModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String header;
    private String property;

    public ColumnModel(String header, String property) {
        this.header = header;
        this.property = property;
    }

    public String getHeader() {
        return header;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColumnModel)) return false;

        ColumnModel that = (ColumnModel) o;

        if (header != null ? !header.equals(that.header) : that.header != null) return false;
        if (property != null ? !property.equals(that.property) : that.property != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = header != null ? header.hashCode() : 0;
        result = 31 * result + (property != null ? property.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ColumnModel {" +
                "header='" + header + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}
