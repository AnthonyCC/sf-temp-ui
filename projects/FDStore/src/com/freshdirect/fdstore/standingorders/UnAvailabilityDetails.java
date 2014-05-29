package com.freshdirect.fdstore.standingorders;

import java.io.Serializable;

public class UnAvailabilityDetails implements Serializable {
		
	double unavailQty;
	UnavailabilityReason reason;
	
	public UnAvailabilityDetails(double unavailQty, UnavailabilityReason reason) {
		super();
		this.unavailQty = unavailQty;
		this.reason = reason;
	}
	
	public double getUnavailQty() {
		return unavailQty;
	}

	public UnavailabilityReason getReason() {
		return reason;
	}
}
