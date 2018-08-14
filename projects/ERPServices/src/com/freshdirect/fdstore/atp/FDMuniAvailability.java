/**
 * @author ekracoff
 * Created on May 11, 2005*/

package com.freshdirect.fdstore.atp;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.framework.util.DateRange;


public class FDMuniAvailability implements FDAvailabilityI{
	
	private static final long serialVersionUID = 7928664639519928003L;
	private final FDMuniAvailabilityInfo availabilityInfo;
	
	public FDMuniAvailability(@JsonProperty("municipalityInfo") MunicipalityInfo muni) {
		this.availabilityInfo = new FDMuniAvailabilityInfo(false, muni);
	}
	
	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange) {
		return this.availabilityInfo;
	}

	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		return this.availabilityInfo;
	}
	
	public Date getFirstAvailableDate(DateRange requestedRange) {
		return this.availabilityInfo.isAvailable() ? requestedRange
				.getStartDate() : null;
	}

	@Override
	public double getAvailabileQtyForDate(Date targetDate) {
		// TODO Auto-generated method stub
		return -3;
	}
	public FDMuniAvailabilityInfo getAvailabilityInfo() {
		return availabilityInfo;
	}
}
