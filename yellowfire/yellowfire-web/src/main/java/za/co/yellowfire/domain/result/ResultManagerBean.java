package za.co.yellowfire.domain.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.manager.DomainManager;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Local(ResultManager.class)
//@Remote(ResultManagerRemote.class)
@Stateless(
		name = "ResultManager",
		//mappedName = "yellowfire/session/ResultManager",
		description = "Manages the result related information")
public class ResultManagerBean implements ResultManager/*, ResultManagerRemote*/  {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());
	
    @EJB private DomainManager manager;

	/**
     * Persists the result of a training or race session
     * @param result The result
     * @return Result
     */
    @Override public Result persist(Result result) {
    	LOGGER.info("ResultManager.persist() : " + result);

        if (result.getId() != null) {
            result = (Result) manager.merge(result);
        } else {
            manager.persist(result);
        }
        return result;
    }
}
