package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.util.RoutingTimeOfDay;

public interface IWaveInstance {
	
	RoutingTimeOfDay getDispatchTime();
	
	RoutingTimeOfDay getWaveStartTime();
		
	int getPreferredRunTime();
	
	int getMaxRunTime();
	
	int getNoOfResources();
	
	int getRoutingWaveInstanceId();
	
	void setDispatchTime(RoutingTimeOfDay dispatchTime);
	
	void setWaveStartTime(RoutingTimeOfDay waveStartTime);
			
	void setPreferredRunTime(int preferredRunTime);
	
	void setMaxRunTime(int maxRunTime);
	
	void setNoOfResources(int noOfResources);
	
	void setRoutingWaveInstanceId(int routingWaveInstanceId);
	
	Date getCutOffTime();
	
	void setCutOffTime(Date cutOffTime);
	
	int getPriority();

	void setPriority(int priority);
}
