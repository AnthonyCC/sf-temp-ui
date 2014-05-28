package com.freshdirect.fdstore.standingorders;

import java.io.Serializable;

public class UnAvailabilityDetails implements Serializable {
		
	double availQty;
	UnavailabilityReason reason;
	
	public UnAvailabilityDetails(double availQty, UnavailabilityReason reason) {
		super();
		this.availQty = availQty;
		this.reason = reason;
	}
	public double getAvailQty() {
		return availQty;
	}
	public UnavailabilityReason getReason() {
		return reason;
	}
}
