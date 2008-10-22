package com.freshdirect.routing.model;

public interface IPackagingModel {
	
	long getNoOfCartons();

	void setNoOfCartons(long noOfCartons);

	long getNoOfCases();

	void setNoOfCases(long noOfCases);

	long getNoOfFreezers();

	void setNoOfFreezers(long noOfFreezers);
	
	double getTotalSize1();
	void setTotalSize1(double size);
	
	double getTotalSize2();
	void setTotalSize2(double size);
	
	boolean isDefault();

	void setDefault(boolean isDefault);

}
