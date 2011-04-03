package za.co.yellowfire.domain.result;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import za.co.yellowfire.domain.profile.User;

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
    
    /**
     * Retrieves the results for a specific person's calendar
     * @param person The person for whom the results should be shown
     * @param start The start date of the calendar
     * @param end The end date of the calendar
     * @return List<Result>
     */
    List<Result> calendar(User person, Date start, Date end);
}
