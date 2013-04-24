package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.List;

import com.freshdirect.transadmin.model.DeliveryGroup;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvBuildingDetail;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.DlvServiceTimeType;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.TrnFacilityLocation;
import com.freshdirect.transadmin.model.TrnFacilityType;

public interface LocationManagerI  extends BaseManagerI {
	
	Collection getServiceTimeTypes();
	
	DlvServiceTimeType getServiceTimeType(String id);
	
	Collection getServiceTimes();
	
	Collection getDeliveryLocations(String srubbedAddress, String apt, String zipCode, String confidence, String quality);
	
	DlvLocation getDlvLocation(String id);
	
	String[] getServiceTypes();
	
	Collection getConfidenceTypes();
	
	Collection getQualityTypes();
	
	Collection getDeliveryBuildings(String srubbedAddress, String zipCode, String confidence, String quality, String group);
	
	DlvBuilding getDlvBuilding(String id);
	
	Collection getServiceTimeScenarios();
	
	Collection getServiceTimeScenarios(String date);
	
	Collection getScenariosWithNoDay();
	
	Collection getServiceTimeScenariosForDayofWeek(int dayOfWeek);
	
	DlvServiceTimeScenario getServiceTimeScenario(String code);
	
	DlvServiceTimeScenario getDefaultServiceTimeScenario();
	
	Collection getDefaultServiceTimeScenarioDay();
	
	Collection getZonesForServiceTimeTypes(List serviceTypeLst);
	
	Collection getDeliveryLocations(String buildingId);
    
   	DlvBuildingDetail getDlvBuildingDtl(String id);

   	Collection getDeliveryBuildingDetails(String srubbedAddress, String zipCode);
   	
   	Collection getDlvScenarioZones(String scenarioId);
   	
   	Collection getDlvServiceTimeScenarioDays();
   	
   	DlvScenarioDay getServiceTimeScenarioDay(String code);
   	
   	void deleteServiceTimeScenario(DlvServiceTimeScenario scenario);
   	
   	Collection getDefaultZoneSupervisors(String zoneId);
   	
   	Collection getDeliveryGroups();
   	
   	DeliveryGroup getDeliveryGroupById(String Id);
   	
   	List<DlvLocation> getBuildingGroup(String dlvGroup);

   	Collection getTrnFacilityTypes();
   	Collection getTrnFacilitys();
   	TrnFacility getTrnFacility(String id);
   	TrnFacilityType getTrnFacilityType(String id);

	Collection getTrnFacilityLocations();
	TrnFacilityLocation getTrnFacilityLocation(String id);
}
