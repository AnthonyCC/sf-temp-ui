package com.freshdirect.transadmin.web.model;

import com.freshdirect.framework.util.TimeOfDay;

public class TimeRange  implements java.io.Serializable {
	
	private TimeOfDay startTime;
	private TimeOfDay endTime;
	
	public TimeOfDay getStartTime() {
		return startTime;
	}
	public TimeOfDay getEndTime() {
		return endTime;
	}
	public void setStartTime(TimeOfDay startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(TimeOfDay endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public String toString() {
		return "[startTime=" + startTime + ", endTime=" + endTime+ "]";
	}	
}
