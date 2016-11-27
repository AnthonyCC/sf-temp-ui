package com.freshdirect.transadmin.model;

import java.util.Date;

import com.freshdirect.routing.model.BaseModel;

public class TimeslotModel extends BaseModel implements ITimeslotModel {
	
	private static final long serialVersionUID = 1L;
	private String area;
	private Date startTime;
	private Date endTime;
	private Date destStartTime;
	private Date destEndTime;	
	private String timeSlotId;		
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getDestStartTime() {
		return destStartTime;
	}
	public void setDestStartTime(Date destStartTime) {
		this.destStartTime = destStartTime;
	}
	public Date getDestEndTime() {
		return destEndTime;
	}
	public void setDestEndTime(Date destEndTime) {
		this.destEndTime = destEndTime;
	}
	public String getTimeSlotId() {
		return timeSlotId;
	}
	public void setTimeSlotId(String timeSlotId) {
		this.timeSlotId = timeSlotId;
	}	
	
}
