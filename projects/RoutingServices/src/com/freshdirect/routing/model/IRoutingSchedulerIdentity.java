package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.RoutingActivityType;

public interface IRoutingSchedulerIdentity {
	
	IAreaModel getArea();

	void setArea(IAreaModel area);

	java.util.Date getDeliveryDate();

	void setDeliveryDate(java.util.Date deliveryDate);

	java.lang.String getRegionId();

	void setRegionId(java.lang.String regionId);
	
	boolean isDepot();

	void setDepot(boolean isDepot);
	
	boolean isDynamic();
	
	void setDynamic(boolean isDynamic);
	
	RoutingActivityType getType();
	
	void setType(RoutingActivityType type);
		
}
