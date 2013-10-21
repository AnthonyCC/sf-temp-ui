package com.freshdirect.transadmin.model;

import java.util.Date;

public interface IWaveInstanceSource {
	
	Date getDeliveryDate();
	TrnFacility getOriginFacility();
	TrnFacility getDestinationFacility();
	Date getDispatchGroup();
	Zone getZone();
	Date getStartTime();
	Date getEndTime();
	Date getMaxReturnTime(); 
	Date getCutOffTime();
	int getNoOfResources();
	boolean isValidSource();
	boolean needsConsolidation();
	
	public String getEquipmentTypeS();
	public void setEquipmentTypeS(String equipmentTypeS);

	void setOriginFacility(TrnFacility originFacility);
	void setDestinationFacility(TrnFacility destinationFacility);
	void setDispatchGroup(Date dispatchGroup);
	void setStartTime(Date value);
	void setEndTime(Date value);
	void setMaxReturnTime(Date value);
	void setCutOffTime(Date value);
	void setNoOfResources(int value);
	int getNoOfResources1();
}

