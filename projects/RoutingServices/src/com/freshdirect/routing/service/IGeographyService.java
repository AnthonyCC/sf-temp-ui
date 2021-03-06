package com.freshdirect.routing.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IFacilityModel;
import com.freshdirect.routing.model.IGeocodeResult;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.util.IGeocodeEngine;
import com.freshdirect.routing.service.util.LocationLocatorResult;

public interface IGeographyService {
	
	
	List<ILocationModel> getLocationsForSnapshot() throws RoutingServiceException;
	
	ILocationModel getLocation(ILocationModel model) throws RoutingServiceException;
	
	IGeocodeResult getGeocode(ILocationModel model) throws RoutingServiceException;
	
	IGeocodeResult getGeocode(String street, String zipCode, String country) throws RoutingServiceException;
	
	String getLocationId() throws RoutingServiceException;
	
	String standardizeStreetAddress(ILocationModel address) throws RoutingServiceException;
	
	String standardizeStreetAddress(String address1, String address2) throws RoutingServiceException;
	
	void insertLocations(List dataList) throws RoutingServiceException;
	
	List getStateList() throws RoutingServiceException;
	
	List batchGeocode(List dataList) throws RoutingServiceException;
	
	IBuildingModel getBuildingLocation(ILocationModel model) throws RoutingServiceException;
	
	IBuildingModel getBuildingLocation(String street, String zipCode) throws RoutingServiceException;
	
	String getBuildingId() throws RoutingServiceException;
	
	void insertBuildings(List dataList) throws RoutingServiceException;
	
	IGeographicLocation getRoutingLocation(String locationId) throws RoutingServiceException;
	
	IGeographicLocation getLocalGeocode(String srubbedStreet, String apartment, String zipCode) throws RoutingServiceException; 
	
	void sendLocationByIds(List locationIds) throws RoutingServiceException; 
	
	IBuildingModel getNewBuilding(ILocationModel baseModel)  throws RoutingServiceException; 
	
	IBuildingModel getNewBuilding(IGeocodeEngine geocodeEngine, ILocationModel baseModel) throws RoutingServiceException;
	
	LocationLocatorResult locateAddress(String streetAddress1, String streetAddress2
			, String apartmentNumber, String city
					, String state, String zipCode
								, String country)  throws RoutingServiceException;
	
	ILocationModel locateOrder(IOrderModel orderModel)  throws RoutingServiceException;
	
	Map<String, IAreaModel> getAreaLookup() throws RoutingServiceException;
	Map<String, IZoneModel> getZoneLookup() throws RoutingServiceException;
	Map<String, IFacilityModel> getFacilityLookup() throws RoutingServiceException;

	Map<String, Integer> getZoneETAIntervalLookup() throws RoutingServiceException;

}
