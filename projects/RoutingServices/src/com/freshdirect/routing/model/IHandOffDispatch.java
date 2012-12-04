package com.freshdirect.routing.model;

import java.util.Date;
import java.util.Set;

import com.freshdirect.sap.bapi.BapiSendHandOff;

public interface IHandOffDispatch {	

	 String getDispatchId();
	 void setDispatchId(String dispatchId);

	 String getPlanId();
	 void setPlanId(String planId);

	 String getZone();
	 void setZone(String zone);

	 String getRegion();
	 void setRegion(String region);

	 Date getDispatchDate();
	 void setDispatchDate(Date dispatchDate); 

	 Date getFirstDeliveryTime();
	 void setFirstDeliveryTime(Date firstDeliveryTime);

	 Date getStartTime(); 
	 void setStartTime(Date startTime);

	 String getIsBullpen(); 
	 void setIsBullpen(String isBullpen);

	 String getSupervisorId(); 
	 void setSupervisorId(String supervisorId);

	 Date getMaxTime(); 
	 void setMaxTime(Date maxTime);

	 Date getCheckInTime();
	 void setCheckInTime(Date checkInTime);

	 String getRoute();
	 void setRoute(String route);
	
	 String getTruck();
	 void setTruck(String truck);

	 Set getDispatchResources();
	 void setDispatchResources(Set dispatchResources);
	 
	 Date getCutoffTime();
	 void setCutoffTime(Date cutoffTime);	 
		
	 String getOriginFacility(); 
	 void setOriginFacility(String originFacility);
	 
	 String getDestinationFacility();
	 void setDestinationFacility(String destinationFacility);
	 
	 boolean isTrailer();
	 void setTrailer(boolean isTrailer);
	 
	 String getDispatchType();
	 void setDispatchType(String dispatchType);

}
