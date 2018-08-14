/**
 * @author ekracoff
 * Created on May 11, 2005*/

package com.freshdirect.fdstore.atp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.common.pricing.MunicipalityInfo;


public class FDMuniAvailabilityInfo extends FDAvailabilityInfo{
	private static final long serialVersionUID = 4817427525414562257L;
	private final MunicipalityInfo muni;

	public FDMuniAvailabilityInfo(@JsonProperty("available") boolean available, @JsonProperty("muni") MunicipalityInfo muni) {
		super(available);
		this.muni = muni;
	}
	
	public MunicipalityInfo getMunicipalityInfo(){
		return this.muni;
	}

	
}
