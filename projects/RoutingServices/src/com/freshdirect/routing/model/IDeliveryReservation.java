package com.freshdirect.routing.model;

import java.util.Date;

public interface IDeliveryReservation {
	
	Date getExpiryTime();
	void setExpiryTime(Date expiryTime);
	boolean isReserved();
	void setReserved(boolean reserved);
	boolean isNewCost();
	void setNewCost(boolean newCost);
}
