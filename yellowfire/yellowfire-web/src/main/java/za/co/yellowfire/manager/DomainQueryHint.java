package za.co.yellowfire.manager;

import org.eclipse.persistence.config.QueryHints;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class DomainQueryHint implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final DomainQueryHint[] REFRESH = new DomainQueryHint [] {new DomainQueryHint(QueryHints.REFRESH, true)};

    private String hint;
    private Object value;

    public DomainQueryHint(String hint, Object value) {
        this.hint = hint;
        this.value = value;
    }

    public String getHint() {
        return hint;
    }

    public Object getValue() {
        return value;
    }
}
