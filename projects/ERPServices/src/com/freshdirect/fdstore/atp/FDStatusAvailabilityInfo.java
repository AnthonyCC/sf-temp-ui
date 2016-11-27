package com.freshdirect.fdstore.atp;

import com.freshdirect.fdstore.EnumAvailabilityStatus;

public class FDStatusAvailabilityInfo extends FDAvailabilityInfo {

	protected final EnumAvailabilityStatus status;

	public FDStatusAvailabilityInfo(boolean available, EnumAvailabilityStatus status) {
		super(available);
		this.status = status;
	}

	public EnumAvailabilityStatus getStatus() {
		return this.status;
	}

	public String toString() {
		return "[FDStatusAvailabilityInfo available: " + this.available + " status: " + this.status + "]";
	}

}