package com.freshdirect.dashboard.model;

import java.text.ParseException;
import java.util.Date;

import com.freshdirect.dashboard.util.DateUtil;

public class TimeslotModel {
	
	private String zone;
	private Date timeslot;
	private int capacity;
	private int orders;
	private int projectedOrders;
	private int currentUtilization;
	private int projectedUtilization;
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public Date getTimeslot() {
		return timeslot;
	}
	public void setTimeslot(Date timeslot) {
		this.timeslot = timeslot;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getOrders() {
		return orders;
	}
	public void setOrders(int orders) {
		this.orders = orders;
	}
	public int getProjectedOrders() {
		return projectedOrders;
	}
	public void setProjectedOrders(int projectedOrders) {
		this.projectedOrders = projectedOrders;
	}
	public int getCurrentUtilization() {
		return currentUtilization;
	}
	public void setCurrentUtilization(int currentUtilization) {
		this.currentUtilization = currentUtilization;
	}
	public int getProjectedUtilization() {
		return projectedUtilization;
	}
	public void setProjectedUtilization(int projectedUtilization) {
		this.projectedUtilization = projectedUtilization;
	}
	public String getTimeslotEx() {
		if(this.getTimeslot() != null) {
			try {
				return DateUtil.getServerTime(this.timeslot);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
