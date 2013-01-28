package com.freshdirect.transadmin.model;

import com.freshdirect.framework.util.TimeOfDay;

public class PlantCapacity implements java.io.Serializable, TrnBaseEntityI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3233908587133309841L;
	private String id;
	private String dayOfWeek;
	private int capacity;
	
	private TimeOfDay dispatchTime;
	
	public boolean isObsoleteEntity() {
		return false;
	}

	public TimeOfDay getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(TimeOfDay dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
