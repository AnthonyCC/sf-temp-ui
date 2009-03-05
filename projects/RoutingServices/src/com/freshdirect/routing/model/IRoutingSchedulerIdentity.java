package com.freshdirect.routing.model;

public interface IRoutingSchedulerIdentity {
	
	IAreaModel getArea();

	void setArea(IAreaModel area);

	java.util.Date getDeliveryDate();

	void setDeliveryDate(java.util.Date deliveryDate);

	java.lang.String getRegionId();

	void setRegionId(java.lang.String regionId);
	
	boolean isDepot();

	void setDepot(boolean isDepot);
		
}
