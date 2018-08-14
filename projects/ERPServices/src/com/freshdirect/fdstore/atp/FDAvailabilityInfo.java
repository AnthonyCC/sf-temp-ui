package com.freshdirect.fdstore.atp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FDAvailabilityInfo implements Serializable {
	private static final long serialVersionUID = 181256114416105465L;

	public static final FDAvailabilityInfo AVAILABLE = new FDAvailabilityInfo(true);
	public static final FDAvailabilityInfo UNAVAILABLE = new FDAvailabilityInfo(false);

	protected final boolean available;

	public FDAvailabilityInfo(@JsonProperty("available") boolean available) {
		this.available = available;
	}
	public boolean getAvailable() {
		return this.available;
	}
	public boolean isAvailable() {
		return this.available;
	}
	
	public String toString() {
		return this.available ? "Available" : "Unavailable";
	}
}
