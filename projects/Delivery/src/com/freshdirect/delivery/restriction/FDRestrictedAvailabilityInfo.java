package com.freshdirect.delivery.restriction;

import com.freshdirect.fdstore.atp.FDAvailabilityInfo;

public class FDRestrictedAvailabilityInfo extends FDAvailabilityInfo {
	
	private RestrictionI restriction; 
	
	public FDRestrictedAvailabilityInfo(boolean available,  RestrictionI restriction){
		super(available);
		this.restriction = restriction;
	}
	
	public RestrictionI getRestriction(){
		return this.restriction;
	}
	
	public String toString() {
		return super.toString() + " " + restriction;
	}

}
