package za.co.yellowfire.domain.profile;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface SystemManager extends Serializable {
    /**
     * Returns the timezone that the system should be using
     * @return String
     */
    String getTimezone();
}
