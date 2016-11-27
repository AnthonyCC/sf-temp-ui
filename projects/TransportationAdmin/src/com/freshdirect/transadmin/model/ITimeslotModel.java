package com.freshdirect.transadmin.model;

import java.util.Date;

public interface ITimeslotModel {
	public String getArea();
	public void setArea(String area);
	
	public Date getStartTime();
	public void setStartTime(Date startTime);
	
	public Date getEndTime();
	public void setEndTime(Date endTime);
	
	public Date getDestStartTime();
	public void setDestStartTime(Date destStartTime);
	
	public Date getDestEndTime();
	public void setDestEndTime(Date destEndTime);
	
	public String getTimeSlotId();
	public void setTimeSlotId(String timeSlotId);

}
