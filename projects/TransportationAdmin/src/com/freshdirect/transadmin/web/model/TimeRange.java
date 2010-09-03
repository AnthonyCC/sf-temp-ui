package com.freshdirect.transadmin.web.model;

import java.util.Date;

import com.freshdirect.framework.util.TimeOfDay;

public class TimeRange  implements java.io.Serializable, Comparable<TimeRange> {
	
	private CustomTimeOfDay startTime;
	private CustomTimeOfDay endTime;
	

	public TimeRange() {		
	}
	
	public TimeRange(Date _startTime, Date _endTime) {
		super();
		this.startTime = new CustomTimeOfDay(_startTime);
		this.endTime = new CustomTimeOfDay(_endTime);
	}
	public CustomTimeOfDay getStartTime() {
		return startTime;
	}
	public CustomTimeOfDay getEndTime() {
		return endTime;
	}
	
	public void setStartTime(CustomTimeOfDay startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(CustomTimeOfDay endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(TimeRange o) {
		return this.startTime.compareTo(o.getStartTime()) + this.endTime.compareTo(o.getEndTime());
	}
	
	@Override
	public String toString() {
		return "[startTime=" + startTime + ", endTime=" + endTime+ "]";
	}	
	
	public String getTimeRangeString() {
		return startTime.getTimeString()+ " - " + endTime.getTimeString();
	}
	
}
