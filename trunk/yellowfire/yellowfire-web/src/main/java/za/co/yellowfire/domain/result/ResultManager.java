package za.co.yellowfire.domain.result;

import javax.ejb.Local;
import java.io.Serializable;

/**
 * 
 * @author Mark Ashworth
 * @version 0.0.1
 */
@Local
public interface ResultManager extends Serializable {

	/**
     * Persists the result of a training or race session
     * @param result The result
     * @return Result
     */
    Result persist(Result result);
}
