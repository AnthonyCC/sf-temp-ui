package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.EnumOrderMetricsSource;

public class PackagingModel extends BaseModel implements IPackagingModel {
	
	private long noOfCartons;
	
	private long noOfFreezers;
	
	private long noOfCases;
		
	private EnumOrderMetricsSource source;
		
	public EnumOrderMetricsSource getSource() {
		return source;
	}

	public void setSource(EnumOrderMetricsSource source) {
		this.source = source;
	}

	public boolean isDefault() {
		return source != null && source.equals(EnumOrderMetricsSource.DEFAULT);
	}

	public long getNoOfCartons() {
		return noOfCartons;
	}

	public void setNoOfCartons(long noOfCartons) {
		this.noOfCartons = noOfCartons;
	}

	public long getNoOfCases() {
		return noOfCases;
	}

	public void setNoOfCases(long noOfCases) {
		this.noOfCases = noOfCases;
	}

	public long getNoOfFreezers() {
		return noOfFreezers;
	}

	public void setNoOfFreezers(long noOfFreezers) {
		this.noOfFreezers = noOfFreezers;
	}
	
	public String toString() {
		return noOfCartons+"-"+noOfFreezers+"-"+noOfCases+"\n";
	}
}
