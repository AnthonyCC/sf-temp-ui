package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.util.Date;

public class CrisisManagerCommand implements Serializable {
	
	private String cutOff;
	
	private Date selectedDate;
	
	private Date destinationDate;
	
	private String customerType;
	
	private String regions;
	
	private String startTime;
	
	private String endTime;
	
	private String deliveryType;
	
	public String getCutOff() {
		return cutOff;
	}

	public void setCutOff(String cutOff) {
		this.cutOff = cutOff;
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public Date getDestinationDate() {
		return destinationDate;
	}

	public void setDestinationDate(Date destinationDate) {
		this.destinationDate = destinationDate;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getRegions() {
		return regions;
	}

	public void setRegions(String regions) {
		this.regions = regions;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
}