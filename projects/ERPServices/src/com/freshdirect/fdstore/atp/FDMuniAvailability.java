/**
 * @author ekracoff
 * Created on May 11, 2005*/

package com.freshdirect.fdstore.atp;

import java.util.Date;

import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.framework.util.DateRange;


public class FDMuniAvailability implements FDAvailabilityI{
	
	private final FDMuniAvailabilityInfo availabilityInfo;
	
	public FDMuniAvailability(MunicipalityInfo muni) {
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

}
