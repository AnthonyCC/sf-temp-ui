package com.freshdirect.routing.model;


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

}
