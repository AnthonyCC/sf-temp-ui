package com.freshdirect.routing.model;

import java.util.Date;

public class DeliveryReservation  extends BaseModel implements IDeliveryReservation {
	
	private Date expiryTime;
	private boolean reserved;
	private boolean newCost;
	public Date getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
	public boolean isReserved() {
		return reserved;
	}
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	public boolean isNewCost() {
		return newCost;
	}
	public void setNewCost(boolean newCost) {
		this.newCost = newCost;
	}
	
}
