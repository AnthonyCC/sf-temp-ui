package com.freshdirect.transadmin.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.freshdirect.routing.constants.EnumGeocodeConfidenceType;
import com.freshdirect.routing.constants.EnumGeocodeQualityType;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.LocationManagerDaoI;
import com.freshdirect.transadmin.model.DeliveryGroup;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvBuildingDetail;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.DlvServiceTimeType;
import com.freshdirect.transadmin.service.LocationManagerI;


public class LocationManagerImpl extends BaseManagerImpl  implements LocationManagerI  {
	
	private LocationManagerDaoI locationManagerDao = null;
	
	public LocationManagerDaoI getLocationManagerDao() {
		return locationManagerDao;
	}

	public void setLocationManagerDao(LocationManagerDaoI locationManagerDao) {
		this.locationManagerDao = locationManagerDao;
	}
	
	protected BaseManagerDaoI getBaseManageDao() {
		return getLocationManagerDao();
	}
	
	public Collection getServiceTimeTypes() {
		return getLocationManagerDao().getServiceTimeTypes(); 
	}
	
	public DlvServiceTimeType getServiceTimeType(String id) {
		return getLocationManagerDao().getServiceTimeType(id); 
	}
	
	public DlvServiceTimeScenario getServiceTimeScenario(String code) {
		return getLocationManagerDao().getServiceTimeScenario(code);
	}
	
	public DlvScenarioDay getServiceTimeScenarioDay(String code) {
		return getLocationManagerDao().getServiceTimeScenarioDay(code);
	}
	
	public void deleteServiceTimeScenario(DlvServiceTimeScenario scenario) {
		getLocationManagerDao().deleteServiceTimeScenario(scenario);
	}
	
	public Collection getScenariosWithNoDay() {
		return getLocationManagerDao().getScenariosWithNoDay();
	}
	
	public DlvServiceTimeScenario getDefaultServiceTimeScenario() {
		return getLocationManagerDao().getDefaultServiceTimeScenario();
	}
	
	public Collection getDefaultServiceTimeScenarioDay() {
		return getLocationManagerDao().getDefaultServiceTimeScenarioDay();
	}
	
	public Collection getServiceTimes() {
		return getLocationManagerDao().getServiceTimes(); 
	}
	
	public Collection getDeliveryLocations(String srubbedAddress, String apt,String zipCode, String confidence, String quality) {
		return getLocationManagerDao().getDeliveryLocations(srubbedAddress, apt, zipCode, confidence, quality); 
	}
	
	public DlvLocation getDlvLocation(String id) {
		return getLocationManagerDao().getDlvLocation(id); 
	}	
	
	public Collection getDeliveryBuildings(String srubbedAddress, String zipCode, String confidence, String quality, String group) {
		return getLocationManagerDao().getDeliveryBuildings(srubbedAddress, zipCode, confidence, quality, group);
	}
	
	public DlvBuilding getDlvBuilding(String id) {
		return getLocationManagerDao().getDlvBuilding(id);
	}
	
	public String[] getServiceTypes() {
		return getLocationManagerDao().getServiceTypes(); 
	}
	
	public Collection getConfidenceTypes() throws DataAccessException {
		return EnumGeocodeConfidenceType.getEnumList();
	}
	
	public Collection getQualityTypes() throws DataAccessException {
		return EnumGeocodeQualityType.getEnumList();
	}
	
	public Collection getServiceTimeScenarios() {
		return getLocationManagerDao().getServiceTimeScenarios();
	}
	
	public Collection getServiceTimeScenarios(String date) {
		return getLocationManagerDao().getServiceTimeScenarios(date);
	}
	public Collection getServiceTimeScenariosForDayofWeek(int dayOfWeek){
		return getLocationManagerDao().getServiceTimeScenariosForDayofWeek(dayOfWeek);
	}
	
	public Collection getDlvServiceTimeScenarioDays(){
		return getLocationManagerDao().getDlvServiceTimeScenarioDays();
	}
	
	public Collection getZonesForServiceTimeTypes(List serviceTypeLst) {
		return getLocationManagerDao().getZonesForServiceTimeTypes(serviceTypeLst);
	}
	
	public Collection getDeliveryLocations(String buildingId) {
		return getLocationManagerDao().getDeliveryLocations(buildingId);
	}

  	public DlvBuildingDetail getDlvBuildingDtl(String id) {			//agb
		return getLocationManagerDao().getDlvBuildingDtl(id);
	}
  
    public Collection getDeliveryBuildingDetails(String srubbedAddress, String zipCode)  {
		return getLocationManagerDao().getDeliveryBuildingDetails(srubbedAddress, zipCode);
	}
    
    public Collection getDlvScenarioZones(String scenarioId)  {
		return getLocationManagerDao().getDlvScenarioZones(scenarioId);
	}
    public Collection getDefaultZoneSupervisors(String zoneId)  {
		return getLocationManagerDao().getDefaultZoneSupervisors(zoneId);
	}
    
    public Collection getDeliveryGroups(){
		return getLocationManagerDao().getDeliveryGroups();
	}
    
    public DeliveryGroup getDeliveryGroupById(String id){
		return getLocationManagerDao().getDeliveryGroupById(id);
	}
}
