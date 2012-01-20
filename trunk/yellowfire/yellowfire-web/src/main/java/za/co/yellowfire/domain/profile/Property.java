package za.co.yellowfire.domain.profile;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface Property extends Serializable {
    String FIELD_ID = "id";
    String FIELD_NAME = "name";
    String FIELD_VALUE = "value";
    String FIELD_TYPE = "type";

    String getName();

	void setName(String name);

	String getValue();

	void setValue(String value);

	String getType();

	void setType(String type);
}
