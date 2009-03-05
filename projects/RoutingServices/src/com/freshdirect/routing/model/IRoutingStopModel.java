package com.freshdirect.routing.model;

import java.util.Date;

public interface IRoutingStopModel {
	
	 boolean isDepot();

	 void setDepot(boolean isDepot);
	
	 Date getStopArrivalTime();

	 void setStopArrivalTime(Date stopArrivalTime);

	 Date getTimeWindowStart();

	 void setTimeWindowStart(Date timeWindowStart);

	 Date getTimeWindowStop();

	 void setTimeWindowStop(Date timeWindowStop);

	
	 ILocationModel getLocation();

	 void setLocation(ILocationModel location);
	
	 int getStopNo();

	 void setStopNo(int stopNo);
}
