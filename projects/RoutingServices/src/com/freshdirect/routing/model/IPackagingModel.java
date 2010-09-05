package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.EnumOrderMetricsSource;

public interface IPackagingModel {
	
	long getNoOfCartons();

	void setNoOfCartons(long noOfCartons);

	long getNoOfCases();

	void setNoOfCases(long noOfCases);

	long getNoOfFreezers();

	void setNoOfFreezers(long noOfFreezers);
		
	boolean isDefault();
	
	EnumOrderMetricsSource getSource();
	void setSource(EnumOrderMetricsSource source);
}
