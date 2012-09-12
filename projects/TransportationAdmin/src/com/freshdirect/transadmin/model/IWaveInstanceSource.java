package com.freshdirect.transadmin.model;


import java.util.Date;

public interface IWaveInstanceSource {
	
	Date getDeliveryDate();
	TrnFacility getOriginFacility();
	TrnFacility getDestinationFacility();
	Zone getZone();
	Date getStartTime();
	Date getFirstDeliveryTime();
	Date getLastDeliveryTime();
	Date getCutOffTime();
	int getNoOfResources();
	boolean isValidSource();
	boolean needsConsolidation();
	
	void setOriginFacility(TrnFacility originFacility);
	void setDestinationFacility(TrnFacility destinationFacility);
	void setStartTime(Date value);
	void setFirstDeliveryTime(Date value);
	void setLastDeliveryTime(Date value);
	void setCutOffTime(Date value);
	void setNoOfResources(int value);
	int getNoOfResources1(); 
}

