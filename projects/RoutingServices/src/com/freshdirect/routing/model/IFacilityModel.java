package com.freshdirect.routing.model;

public interface IFacilityModel {
	
	String CROSS_DOCK = "CD";
	
	String getFacilityCode();
	void setFacilityCode(String facilityCode);	
	
	String getFacilityTypeModel();
	void setFacilityTypeModel(String facilityTypeModel);	
	
	String getRoutingCode();
	void setRoutingCode(String RoutingCode);	
	
	int getLeadToTime();
	void setLeadToTime(int leadToTime);
	
	int getLeadFromTime();	
	void setLeadFromTime(int leadFromTime);
	
	String getPrefix();
	void setPrefix(String prefix);
	
	String getLatitude();
	void setLatitude(String latitude);

	String getLongitude();
	void setLongitude(String longitude);
	
}
