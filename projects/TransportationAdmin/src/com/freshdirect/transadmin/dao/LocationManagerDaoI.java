package com.freshdirect.transadmin.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvBuildingDetail;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.DlvServiceTime;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.DlvServiceTimeType;

public interface LocationManagerDaoI extends BaseManagerDaoI {

	Collection getServiceTimeTypes() throws DataAccessException;
	
	DlvServiceTimeType getServiceTimeType(String id) throws DataAccessException;
	
	Collection getServiceTimes() throws DataAccessException;
	
	DlvServiceTime getServiceTime(String code, String zoneType) throws DataAccessException;
	
	Collection getDeliveryLocations(String srubbedAddress, String apt, String zipCode, String confidence, String quality) throws DataAccessException;
	
	DlvLocation getDlvLocation(String id) throws DataAccessException;
	
	String[] getServiceTypes() throws DataAccessException;
		
	Collection getDeliveryBuildings(String srubbedAddress, String zipCode, String confidence, String quality) throws DataAccessException;
	
	DlvBuilding getDlvBuilding(String id) throws DataAccessException;
	
	Collection getServiceTimeScenarios() throws DataAccessException;
	
	Collection getServiceTimeScenarios(String date) throws DataAccessException;
	
	Collection getServiceTimeScenariosForDayofWeek(int dayOfWeek) throws DataAccessException;
	
	DlvServiceTimeScenario getServiceTimeScenario(String code) throws DataAccessException;
	
	DlvServiceTimeScenario getDefaultServiceTimeScenario() throws DataAccessException;
	
	Collection getScenariosWithNoDay() throws DataAccessException;
	
	Collection getDefaultServiceTimeScenarioDay() throws DataAccessException;
	
	Collection getServiceTimesForZoneTypes(List zoneTypeLst) throws DataAccessException;
	
	Collection getScenariosForZoneTypes(List zoneTypeLst) throws DataAccessException; 
	
	Collection getDeliveryLocations(String buildingId) throws DataAccessException;
    
   	DlvBuildingDetail getDlvBuildingDtl(String id) throws DataAccessException;		//agb
    
   	Collection getDeliveryBuildingDetails(String srubbedAddress, String zipCode) throws DataAccessException;
   	
   	Collection getDlvScenarioZones(String scenarioCode) throws DataAccessException;
   	
   	Collection getDlvServiceTimeScenarioDays() throws DataAccessException;

   	DlvScenarioDay getServiceTimeScenarioDay(String code) throws DataAccessException;
   	
  	void deleteServiceTimeScenario(DlvServiceTimeScenario scenario) throws DataAccessException;
}
