package za.co.yellowfire.domain.search;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class Search implements Serializable {
    private static final long serialVersionUID = 1L;

    private String query;
    private String filter;
    private String fields;
}
