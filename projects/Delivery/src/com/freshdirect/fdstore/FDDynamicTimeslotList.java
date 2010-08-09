package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.List;

public class FDDynamicTimeslotList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -851646912704992959L;

	private List<FDTimeslot> timeslots;
	
	private Exception error;

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
	
	
	
}
