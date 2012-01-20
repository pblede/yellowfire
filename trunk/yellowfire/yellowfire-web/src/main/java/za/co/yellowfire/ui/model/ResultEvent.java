package za.co.yellowfire.ui.model;

import org.primefaces.model.ScheduleEvent;
import za.co.yellowfire.domain.result.Result;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Mark Ashworth
 * @version 0.0.1
 */
public class ResultEvent implements ScheduleEvent, Serializable {
	private static final long serialVersionUID = 1L;
	
	private Result result;
	
	public ResultEvent() {
		this(new Result());
	}

	public ResultEvent(Result result) {
		this.result = result;
	}

	@Override public Object getData() {
		return getResult();
	}

	@Override public Date getEndDate() {
		return getResult().getEnd();
	}

	@Override public String getId() {
		return getResult().getId().toString();
	}

	@Override public Date getStartDate() {
		return getResult().getStart();
	}

	@Override public String getStyleClass() {
		switch(getResult().getType()) {
		case Race:
			return "race";
		case Training:
			return "training";
		default:
			return "";
		}
	}

	@Override public String getTitle() {
		final String resultName;
		switch(getResult().getType()) {
		case Race:
			if (getResult().getRace() != null && getResult().getRace().getName() != null) {
				resultName = "Race: " + getResult().getRace().getName();
			} else {
				resultName = "Race: <Unknown>";
			}
			break;
		case Training:
			resultName = getResult().getName();
			break;
		default:
			resultName = "Unknown";
		};
		return resultName;
	}

	@Override public boolean isAllDay() {
		return false;
	}

	@Override public void setId(String id) {
		/* Ignore */
	}

    @Override
    public boolean isEditable() {
        return false;
    }

    public Result getResult() {
		return this.result;
	}
	
	public void setResult(Result result) {
		System.out.println("setResult " + result);
		this.result = result;
	}
}
