package com.freshdirect.routing.model;

import java.util.Date;

public class GenericSearchModel implements IGenericSearchModel {
	private Date sourceDate;
	private String[] area;
	private Date cutOffDate;
	private Date startTime;
	private Date endTime;
	public Date getSourceDate() {
		return sourceDate;
	}
	public void setSourceDate(Date sourceDate) {
		this.sourceDate = sourceDate;
	}
	public String[] getArea() {
		return area;
	}
	public void setArea(String[] area) {
		this.area = area;
	}
	public Date getCutOffDate() {
		return cutOffDate;
	}
	public void setCutOffDate(Date cutOffDate) {
		this.cutOffDate = cutOffDate;
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
}
