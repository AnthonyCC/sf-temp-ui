package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.List;

import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.DlvServiceTime;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.DlvServiceTimeType;
import com.freshdirect.transadmin.model.DlvBuildingDtl;
import com.freshdirect.transadmin.model.GeoRestriction;

public interface LocationManagerI  extends BaseManagerI {
	
	Collection getServiceTimeTypes();
	
	DlvServiceTimeType getServiceTimeType(String id);
	
	Collection getServiceTimes();
	
	DlvServiceTime getServiceTime(String code, String zoneType);
	
	Collection getDeliveryLocations(String srubbedAddress, String apt, String zipCode, String confidence, String quality);
	
	DlvLocation getDlvLocation(String id);
	
	String[] getServiceTypes();
	
	Collection getConfidenceTypes();
	
	Collection getQualityTypes();
	
	Collection getDeliveryBuildings(String srubbedAddress, String zipCode, String confidence, String quality);
	
	DlvBuilding getDlvBuilding(String id);
	
	Collection getServiceTimeScenarios();
	
	DlvServiceTimeScenario getServiceTimeScenario(String code);
	
	DlvServiceTimeScenario getDefaultServiceTimeScenario();
	
	Collection getServiceTimesForZoneTypes(List zoneTypeLst);
	
	Collection getScenariosForZoneTypes(List zoneTypeLst);
	
	Collection getDeliveryLocations(String buildingId);
    
   	DlvBuildingDtl getDlvBuildingDtl(String id);		//agb
   	Collection getDeliveryBuildingDetails(String srubbedAddress, String zipCode);
   	
 

}
