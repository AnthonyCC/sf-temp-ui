package com.freshdirect.fdstore.atp;

import java.io.Serializable;

public class FDAvailabilityInfo implements Serializable {
	private static final long serialVersionUID = 181256114416105465L;

	public static final FDAvailabilityInfo AVAILABLE = new FDAvailabilityInfo(true);
	public static final FDAvailabilityInfo UNAVAILABLE = new FDAvailabilityInfo(false);

	protected final boolean available;

	public FDAvailabilityInfo(boolean available) {
		this.available = available;
	}

	public boolean isAvailable() {
		return this.available;
	}
	
	public String toString() {
		return this.available ? "Available" : "Unavailable";
	}
}
