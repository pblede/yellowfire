package za.co.yellowfire.domain.geocode;

import javax.ejb.Local;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local
public interface GeocodeManager extends Serializable {

    GeocodeResponse findAddress(String address) throws Exception;
}
