package com.freshdirect.fdstore.atp;

import java.util.Date;

public class FDLimitedAvailabilityInfo {
	private Date requestedDate; 
	private double quantity;
	private boolean available;
	
	public FDLimitedAvailabilityInfo(Date avDate, double qty, boolean available){
		this.requestedDate = avDate;
		this.quantity = qty;
		this.available = available;
	}
	
	public boolean isAvailable() {
		return available;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}
	public double getQuantity() {
		return quantity;
	}
	
	
}
