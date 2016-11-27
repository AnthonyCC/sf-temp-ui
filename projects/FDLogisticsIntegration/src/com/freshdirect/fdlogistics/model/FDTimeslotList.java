package com.freshdirect.fdlogistics.model;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.framework.util.DateRange;

public class FDTimeslotList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -851646912704992959L;

	private List<FDTimeslot> timeslots;
	
	private DateRange range;
	
	private Exception error;
	
	private int responseTime;
	
	private boolean isAdvanced;
	
	private String eventPk;

	public List<FDTimeslot> getTimeslots() {
		return timeslots;
	}

	public Exception getError() {
		return error;
	}

	public void setTimeslots(List<FDTimeslot> timeslots) {
		this.timeslots = timeslots;
	}

	public void setError(Exception error) {
		this.error = error;
	}

	public int getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}

	public DateRange getRange() {
		return range;
	}

	public void setRange(DateRange range) {
		this.range = range;
	}

	public boolean isAdvanced() {
		return isAdvanced;
	}
	

	public void setAdvanced(boolean isAdvanced) {
		this.isAdvanced = isAdvanced;
	}

	public String getEventPk() {
		return eventPk;
	}

	public void setEventPk(String eventPk) {
		this.eventPk = eventPk;
	}
	
	
}
