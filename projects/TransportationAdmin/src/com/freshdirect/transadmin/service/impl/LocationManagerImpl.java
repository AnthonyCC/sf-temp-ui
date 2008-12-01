package com.freshdirect.transadmin.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.freshdirect.routing.constants.EnumGeocodeConfidenceType;
import com.freshdirect.routing.constants.EnumGeocodeQualityType;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.LocationManagerDaoI;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.DlvServiceTime;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.DlvServiceTimeType;
import com.freshdirect.transadmin.service.LocationManagerI;

import com.freshdirect.transadmin.model.DlvBuildingDtl;


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
	
	public DlvServiceTimeScenario getDefaultServiceTimeScenario() {
		return getLocationManagerDao().getDefaultServiceTimeScenario();
	}
	
	public Collection getServiceTimes() {
		return getLocationManagerDao().getServiceTimes(); 
	}
	
	public DlvServiceTime getServiceTime(String code, String zoneType) {
		return getLocationManagerDao().getServiceTime(code, zoneType); 
	}
	
	public Collection getDeliveryLocations(String srubbedAddress, String apt,String zipCode, String confidence, String quality) {
		return getLocationManagerDao().getDeliveryLocations(srubbedAddress, apt, zipCode, confidence, quality); 
	}
	
	public DlvLocation getDlvLocation(String id) {
		return getLocationManagerDao().getDlvLocation(id); 
	}	
	
	public Collection getDeliveryBuildings(String srubbedAddress, String zipCode, String confidence, String quality) {
		return getLocationManagerDao().getDeliveryBuildings(srubbedAddress, zipCode, confidence, quality);
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
	
	public Collection getServiceTimesForZoneTypes(List zoneTypeLst) {
		return getLocationManagerDao().getServiceTimesForZoneTypes(zoneTypeLst);
	}
	
	public Collection getScenariosForZoneTypes(List zoneTypeLst) {
		return getLocationManagerDao().getScenariosForZoneTypes(zoneTypeLst);
	}
	
	public Collection getDeliveryLocations(String buildingId) {
		return getLocationManagerDao().getDeliveryLocations(buildingId);
	}

  	public DlvBuildingDtl getDlvBuildingDtl(String id) {			//agb
		return getLocationManagerDao().getDlvBuildingDtl(id);
	}
  
    public Collection getDeliveryBuildingDetails(String srubbedAddress, String zipCode)  {
		return getLocationManagerDao().getDeliveryBuildingDetails(srubbedAddress, zipCode);
	}
    
}
