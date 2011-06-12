package za.co.yellowfire.domain.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.domain.profile.User;
import za.co.yellowfire.manager.DomainManagerBean;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Local(ResultManager.class)
//@Remote(ResultManagerRemote.class)
@Stateless(
		name = "ResultManager",
		//mappedName = "yellowfire/session/ResultManager",
		description = "Manages the result related information")
public class ResultManagerBean extends DomainManagerBean implements ResultManager/*, ResultManagerRemote*/  {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());
	
	/**
     * Persists the result of a training or race session
     * @param result The result
     * @return Result
     */
    @Override public Result persist(Result result) {
    	LOGGER.info("ResultManager.persist() : " + result);

        if (result.getId() != null) {
            result = (Result) super.merge(result);
        } else {
            super.persist(result);
        }
        return result;
    }
    
    /**
     * Retrieves the results for a specific person's calendar
     * @param person The person for whom the results should be shown
     * @param start The start date of the calendar
     * @param end The end date of the calendar
     * @return List<Result>
     */
    @SuppressWarnings("unchecked")
    @Override public List<Result> calendar(User person, Date start, Date end) {
    	LOGGER.debug("ResultManager.calendar() : {} : {} : {}", new Object[]{person, start, end});
    	Map<String, Object> params = new HashMap<String, Object>(3);
    	params.put(Result.FIELD_PERSON, person.getId());
    	params.put(Result.FIELD_START, start);
    	params.put(Result.FIELD_END, end);
    	
    	return (List<Result>) query(Result.QRY_RESULT_CALENDAR, params);
    }
}
