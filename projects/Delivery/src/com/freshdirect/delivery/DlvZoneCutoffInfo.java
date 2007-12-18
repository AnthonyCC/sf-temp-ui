package com.freshdirect.delivery;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.framework.util.TimeOfDay;

public class DlvZoneCutoffInfo implements Serializable {

	private final String zoneCode;
	private final Date day;
	private final TimeOfDay startTime;
	private final TimeOfDay endTime;
	private final TimeOfDay cutoffTime;

	public DlvZoneCutoffInfo(String zoneCode, Date day, TimeOfDay startTime, TimeOfDay endTime, TimeOfDay cutoffTime) {
		this.zoneCode = zoneCode;
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
		this.cutoffTime = cutoffTime;
	}
	
	public String getzoneCode() {
		return this.zoneCode;
	}

	public Date getDay() {
		return this.day;
	}

	public TimeOfDay getStartTime() {
		return this.startTime;
	}

	public TimeOfDay getEndTime() {
		return this.endTime;
	}

	public TimeOfDay getCutoffTime() {
		return this.cutoffTime;
	}

	public String toString() {
		return "DlvCutoffInfo[ZONECODE:" 
			+ this.zoneCode
			+ " DAY: "
			+ this.day
			+ " START_TIME:"
			+ this.startTime
			+ " END_TIME:"
			+ this.endTime
			+ " CUTOFF_TIME:"
			+ this.cutoffTime
			+ "]";
	}

}
