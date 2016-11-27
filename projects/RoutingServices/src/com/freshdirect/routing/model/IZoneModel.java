package com.freshdirect.routing.model;

import java.math.BigInteger;


 public interface IZoneModel {
	
	 IAreaModel getArea();

	 void setArea(IAreaModel area);

	 String getNeighborhood();

	 void setNeighborhood(String neighborhood);

	 String getRoutingFlag();

	 void setRoutingFlag(String routingFlag);

	 String getZoneId();

	 void setZoneId(String zoneId);

	 String getZoneNumber();

	 void setZoneNumber(String zoneNumber);

	 String getZoneType();

	 void setZoneType(String zoneType);
	 
     IServiceTimeTypeModel getServiceTimeType();
     
     void setServiceTimeType(IServiceTimeTypeModel serviceTimeType);
     
 	 int getLoadingPriority();
 	 void setLoadingPriority(int loadingPriority);
 	 
 	double getSvcAdjReductionFactor();

	void setSvcAdjReductionFactor(double svcAdjReductionFactor);
	
	boolean isManifestETAEnabled();

	void setManifestETAEnabled(boolean manifestETAEnabled);

	int getETAInterval();

	void setETAInterval(int eTAInterval);
}
