package com.freshdirect.fdstore.content;

import java.util.Date;

public class CMSScheduleModel {
	private String day;
	private Date startDate;
	private Date endDate;
	private Date startTime;
	private Date endTime;
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setDay(String day) {
		this.day = day;
	}
	
	public String getDay() {
		return day;
	}
}
