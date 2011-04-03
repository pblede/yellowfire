package za.co.yellowfire.ui.model;

import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.primefaces.model.LazyScheduleModel;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import za.co.yellowfire.Naming;
import za.co.yellowfire.domain.profile.User;
import za.co.yellowfire.domain.result.Result;
import za.co.yellowfire.domain.result.ResultManager;
import za.co.yellowfire.log.LogType;


/**
 * 
 * @author Mark Ashworth
 * @version 0.0.1
 */
public class ResultsScheduleModel extends LazyScheduleModel {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MODEL.getCategory());
	
	private User person;
	
	public ResultsScheduleModel(User person) {
		this.person = person;
	}
	
	public ResultManager getResultManager() throws NamingException {
		return (ResultManager) new InitialContext().lookup(Naming.MANAGER_RESULT);
	}
	
	@Override public void loadEvents(Date start, Date end) {
		clear();
		
		try {
			if (person != null) {
				List<Result> results = getResultManager().calendar(person, start, end);
				if (results != null) {
					for(Result r : results) {
						addEvent(new ResultEvent(r));
					}
				}
			}
		} catch (NamingException e) {
			LOGGER.error("Unable to load result events ", e);
		}
	}
}
