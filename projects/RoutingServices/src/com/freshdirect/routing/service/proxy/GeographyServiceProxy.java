package com.freshdirect.routing.service.proxy;

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
import com.freshdirect.routing.service.IGeographyService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.util.IGeocodeEngine;
import com.freshdirect.routing.service.util.LocationLocatorResult;

public class GeographyServiceProxy  extends BaseServiceProxy {
	
	public ILocationModel getLocation(ILocationModel model) throws RoutingServiceException  {
		return getService().getLocation(model);
	}
	
	
	public IGeocodeResult getGeocode(ILocationModel model) throws RoutingServiceException {
		
		return getService().getGeocode(model);
	}
	
	
	public List<ILocationModel> getLocationsForSnapshot() throws RoutingServiceException {
		return getService().getLocationsForSnapshot();
	}
	public String getLocationId() throws RoutingServiceException {
		return getService().getLocationId();
	}
	
	public void insertLocations(List dataList) throws RoutingServiceException {
		getService().insertLocations(dataList);
	}
	
	public List getStateList() throws RoutingServiceException{
		return getService().getStateList();
	}
	
	public String standardizeStreetAddress(ILocationModel address) throws RoutingServiceException {
		return getService().standardizeStreetAddress(address);
	}
	
	public String standardizeStreetAddress(String address1, String address2) throws RoutingServiceException {
		return getService().standardizeStreetAddress(address1, address2);
	}
	
	public List batchGeocode(List dataList) throws RoutingServiceException {
		return getService().batchGeocode(dataList);
	}
	
	public IBuildingModel getBuildingLocation(ILocationModel model) throws RoutingServiceException {
		return getService().getBuildingLocation(model);
	}
	
	
	
	public IBuildingModel getBuildingLocation(String street, String zipCode) throws RoutingServiceException {
		return getService().getBuildingLocation(street, zipCode);
	}
	
	public IGeocodeResult getGeocode(String street, String zipCode, String country) throws RoutingServiceException {
		return getService().getGeocode(street, zipCode, country);
	}
	
	public String getBuildingId() throws RoutingServiceException {
		return getService().getBuildingId();
	}
	
	public void insertBuildings(List dataList) throws RoutingServiceException {
		getService().insertBuildings(dataList);
	}
	
	public IGeographicLocation getRoutingLocation(String locationId) throws RoutingServiceException {
		return getService().getRoutingLocation(locationId);
	}
	
	public IGeographicLocation getLocalGeocode(String srubbedStreet, String apartment, String zipCode) throws RoutingServiceException {
		return getService().getLocalGeocode(srubbedStreet, apartment, zipCode);
	}
	
	public void sendLocationByIds(List locationIds) throws RoutingServiceException {
		getService().sendLocationByIds(locationIds);
	}
	
	public IBuildingModel getNewBuilding(ILocationModel baseModel) throws RoutingServiceException {
		return getService().getNewBuilding(baseModel);
	}
	
	public IBuildingModel getNewBuilding(IGeocodeEngine geocodeEngine, ILocationModel baseModel) throws RoutingServiceException {
		return getService().getNewBuilding(geocodeEngine, baseModel);
	}
		
	public IGeographyService getService() {
		return RoutingServiceLocator.getInstance().getGeographyService();
	}
	
	public LocationLocatorResult locateAddress(String streetAddress1, String streetAddress2
			, String apartmentNumber, String city
			, String state, String zipCode
						, String country)  throws RoutingServiceException {
		return getService().locateAddress(streetAddress1, streetAddress2
													, apartmentNumber, city
													, state, zipCode
																, country);
	}
	
	public Map<String, IAreaModel> getAreaLookup() throws RoutingServiceException {
		return getService().getAreaLookup();
	}
	
	public Map<String, IZoneModel> getZoneLookup() throws RoutingServiceException {
		return getService().getZoneLookup();
	}
	
	public ILocationModel locateOrder(IOrderModel orderModel)  throws RoutingServiceException {
		return getService().locateOrder(orderModel);
	}

	public Map<String, IFacilityModel> getFacilityLookup() throws RoutingServiceException {
		return getService().getFacilityLookup();
}
}
