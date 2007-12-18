/**
 * @author ekracoff
 * Created on May 11, 2005*/

package com.freshdirect.fdstore.atp;

import com.freshdirect.common.pricing.MunicipalityInfo;


public class FDMuniAvailabilityInfo extends FDAvailabilityInfo{
	private final MunicipalityInfo muni;

	public FDMuniAvailabilityInfo(boolean available, MunicipalityInfo muni) {
		super(available);
		this.muni = muni;
	}
	
	public MunicipalityInfo getMunicipalityInfo(){
		return this.muni;
	}

	
}
