package za.co.yellowfire.ui.racing;

import org.jboss.seam.security.Identity;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.DateUtil;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.domain.racing.Race;
import za.co.yellowfire.domain.result.Result;
import za.co.yellowfire.domain.result.ResultType;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.FacesUtil;
import za.co.yellowfire.ui.model.AbstractScheduleModel;
import za.co.yellowfire.ui.model.ResultEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author Mark Ashworth
 * @version 0.0.1
 */
@Named("resultsController") @Stateful @ConversationScoped
public class ResultsController extends AbstractRacingUIController {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());
	
	/* Reference */
	private List<ResultType> resultTypes = new ArrayList<ResultType>(Arrays.asList(ResultType.Training, ResultType.Race));

	/* Selected result */
	private ResultEvent selected = new ResultEvent();
	
	/* Current races for the selected date or event */
	private List<Race> races;
	
	/* Result calendar model */
	private AbstractScheduleModel<ResultEvent> resultModel;

    private List<Long> hours;
    private List<Long> minutes;
    private List<Long> seconds;

    @Inject
	private Identity identity;

    @EJB
    private DomainManager manager;

    @Inject
    private  Conversation conversation;

	/* Controls */
    private UIComponent racesControl;

    @PostConstruct
    protected void init() {
        /* Start a conversation */
        conversation.begin();

        this.resultModel = new AbstractScheduleModel<ResultEvent>() {
            @Override
            public List<ResultEvent> onLoadEvents(Date start, Date end) {
                List<ResultEvent> events = new ArrayList<ResultEvent>();
                List<Result> results = getCalendar((Profile) identity.getUser(), start, end);
                for (Result result : results) {
                    events.add(new ResultEvent(result));
                }
                return events;
            }
        };

        this.hours = new ArrayList<Long>();
        for (long i = 0; i <= 23; i++) {
            this.hours.add(i);
        }
        this.minutes = new ArrayList<Long>();
        for (long i = 0; i <= 59; i++) {
            this.minutes.add(i);
        }
        this.seconds = new ArrayList<Long>();
        for (long i = 0; i <= 59; i++) {
            this.seconds.add(i);
        }
    }
	
	public ScheduleEvent getSelected() {
		return selected;
	}

	public ScheduleModel getResultModel() {
		return resultModel;
	}

	public List<Race> getRacesForDate() {
		LOGGER.info("getRacesForDate()");
		if (this.races == null) {
			LOGGER.info("\tgetRacesForDate() is null");
			if (selected != null && selected.getResult() != null && selected.getResult().getStart() != null) {
				LOGGER.info("\t\tgetRacesForDate() loading races for " + selected.getResult().getStart());
				this.races = retrieveRacesForDate(selected.getResult().getStart());
			} else {
				LOGGER.info("\t\tgetRacesForDate() loading races for nothing selected");
				this.races = new ArrayList<Race>();
			}
		}
		return this.races;
	}

    /**
     * Retrieves the races for a specified date
     * @param date The date of the races to retrieve
     * @return The races that match the date
     */
    @SuppressWarnings("unchecked")
    public List<Race> retrieveRacesForDate(Date date) {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(Race.FIELD_DATE, DateUtil.getDate(date, false, false));
        return (List<Race>) manager.query(Race.QRY_RACES_FOR_DATE, params);
    }

    /**
     * Retrieves the races for a specified id
     * @param id The id of the races to retrieve
     * @return The races that match the id
     */
    @SuppressWarnings("unchecked")
    public List<Race> retrieveRacesForId(Long id) {
    	Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(Race.FIELD_ID, id);
        return (List<Race>) manager.query(Race.QRY_RACES_FOR_ID, params);
    }

    /**
     * Retrieves the results for a specific person's calendar
     * @param profile The profile for whom the results should be shown
     * @param start The start date of the calendar
     * @param end The end date of the calendar
     * @return List<Result>
     */
    @SuppressWarnings("unchecked")
    public List<Result> getCalendar(Profile profile, Date start, Date end) {
    	LOGGER.debug("ResultsController.calendar() : {} : {} : {}", new Object[]{profile, start, end});
    	Map<String, Object> params = new HashMap<String, Object>(3);
    	params.put(Result.FIELD_PERSON, profile.getUserId());
    	params.put(Result.FIELD_START, start);
    	params.put(Result.FIELD_END, end);

    	return (List<Result>) manager.query(Result.QRY_RESULT_CALENDAR, params);
    }

	public List<ResultType> getResultTypes() {
		return resultTypes;
	}

    public UIComponent getRacesControl() {
        return racesControl;
    }

    public void setRacesControl(UIComponent racesControl) {
        this.racesControl = racesControl;
    }

    public List<Long> getHours() { return this.hours; }
    public List<Long> getMinutes() { return this.minutes; }
    public List<Long> getSeconds() { return this.seconds; }
	
	public void onEventSelect(ScheduleEntrySelectEvent selectEvent) {
		LOGGER.info("onEventSelect : " + selectEvent);
		this.selected = (ResultEvent) selectEvent.getScheduleEvent(); 

        LOGGER.info("onEventSelect : " + selectEvent.getScheduleEvent());
        LOGGER.info("onEventSelect : " + this.selected.getResult());

		//Load the races for the event
        if (this.selected.getResult().getRace() != null) {
		    this.races = retrieveRacesForId(this.selected.getResult().getRace().getId());
        }
	}
	
	public void onDateSelect(DateSelectEvent selectEvent) {
		LOGGER.info("onDateSelect : " + selectEvent.getDate());
		this.selected = new ResultEvent(new Result(selectEvent.getDate(), selectEvent.getDate()));
		
		//Load the races for the date
		this.races = retrieveRacesForDate(selectEvent.getDate());
		
	}
	
	public void onEventMove(ScheduleEntryMoveEvent moveEvent) {
		FacesUtil.addInfoMessage("Event moved", "Detail: " + moveEvent.getDayDelta() + "d : " + moveEvent.getMinuteDelta() + "m");
	}
	
	public void onEventResize(ScheduleEntryResizeEvent resizeEvent) {
		FacesUtil.addInfoMessage("Event resized", "Detail: " + resizeEvent.getDayDelta() + "d : " + resizeEvent.getMinuteDelta() + "m");
	}
	
	public void onResultTypeChange(ValueChangeEvent changeEvent) {
		LOGGER.info("onResultTypeChange() : " + changeEvent);
	}

    public void onTrainingResult(ActionEvent event) {
        LOGGER.info("onTrainingResult() : " + event);
        selected.getResult().setType(ResultType.Training);
    }

    public void onRaceResult(ActionEvent event) {
        LOGGER.info("onRaceResult() : " + event);
        selected.getResult().setType(ResultType.Race);
    }

    public String onNewTraining() {
        /* Start a new conversation */
        //conversation.begin();

        /* Set the result type */
        this.selected.getResult().setType(ResultType.Training);
        return "training_result.jsf";
    }

    public String onNewRace() {
        /* Start a new conversation */
        //conversation.begin();

        /* Set the result type */
        this.selected.getResult().setType(ResultType.Race);
        return "race_result.jsf";
    }

    
    public void onDistanceChanged(AjaxBehaviorEvent event) {
        onTimeTakenChanged(event);
    }

    public void onStartTimeChanged(AjaxBehaviorEvent event) {
        onTimeTakenChanged(event);

        /* Reload the races */
        if (this.selected.getResult().getType() == ResultType.Race) {
            getRacesForDate();
        }
    }

    @SuppressWarnings("unused")
    public void onTimeTakenChanged(AjaxBehaviorEvent event) {

        Result result = this.selected.getResult();

        //Calculate the end time
        Date start = this.selected.getResult().getStart();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(start);
        cal.set(Calendar.HOUR, result.getTimeTakenHours());
        cal.set(Calendar.MINUTE, result.getTimeTakenHours());
        cal.set(Calendar.SECOND, result.getTimeTakenHours());
        cal.set(Calendar.MILLISECOND, 0);
        result.setEnd(cal.getTime());

        //Calculate the pace
        if (result.getTotalTimeTakenMinutes() != 0 && result.getDistance() != 0) {
            result.setPace(result.getTotalTimeTakenMinutes() / result.getDistance());
        }

        LOGGER.info("onTimeTakenChanged() : " + result.getEnd());
        LOGGER.info("onTimeTakenChanged() : " + result);
    }
    
    public void onSave(ActionEvent event) {
        LOGGER.info("onSave() : " + event);

        Result r = this.selected.getResult();
        r.setPerson((Profile) this.identity.getUser());
        if (r.getType() == ResultType.Training) {
            r.setName("Training - " + new SimpleDateFormat("yyyy-MM-dd").format(r.getStart()));
        }

        manager.persist(r);
        this.selected.setResult(r);

        FacesUtil.addInfoMessage("Training Saved", "The training session saved");

        /* Complete the conversation */
        conversation.end();
    }

    public void onClear(ActionEvent event) {
        LOGGER.info("onClear() : " + event);

        /* Complete the conversation */
        conversation.end();
    }
}
