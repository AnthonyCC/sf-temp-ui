package com.freshdirect.routing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.framework.util.DateUtil;

public class TimeslotCapacityModel implements Serializable {

	private Date baseDate;

	private String area;

	/** Total capacity */
	private int capacity;

	/** Chef's Table Capacity (ctCapacity <= capacity) */
	private int chefsTableCapacity;
	
	private int premiumCapacity;

	private int premiumCtCapacity;
	
	private Date startTime;
	
	private Date endTime;
	
	private List<String> exceptions;
	
	public TimeslotCapacityModel() {
		super();
		exceptions = new ArrayList<String>();
	}

	public Date getBaseDate() {
		return baseDate;
	}

	public void setBaseDate(Date baseDate) {
		this.baseDate = baseDate;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getChefsTableCapacity() {
		return chefsTableCapacity;
	}

	public void setChefsTableCapacity(int chefsTableCapacity) {
		this.chefsTableCapacity = chefsTableCapacity;
	}

	public int getPremiumCapacity() {
		return premiumCapacity;
	}

	public void setPremiumCapacity(int premiumCapacity) {
		this.premiumCapacity = premiumCapacity;
	}

	public int getPremiumCtCapacity() {
		return premiumCtCapacity;
	}

	public void setPremiumCtCapacity(int premiumCtCapacity) {
		this.premiumCtCapacity = premiumCtCapacity;
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

	public List<String> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<String> exceptions) {
		this.exceptions = exceptions;
	}
	
	public String getDispalyWindow() {
		if(this.startTime != null && 
				this.endTime != null) {
			return DateUtil.formatTime(this.startTime) + " - " + DateUtil.formatTime(this.endTime);
		}
		
		return "";
	}
}
