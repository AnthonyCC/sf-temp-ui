package com.freshdirect.routing.service.proxy;

import java.util.List;

import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IGeocodeResult;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.service.IGeographyService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.util.IGeocodeEngine;

public class GeographyServiceProxy  extends BaseServiceProxy {
	
	public ILocationModel getLocation(ILocationModel model) throws RoutingServiceException  {
		return getService().getLocation(model);
	}
	
	
	public IGeocodeResult getGeocode(ILocationModel model) throws RoutingServiceException {
		
		return getService().getGeocode(model);
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
	
	public ILocationModel locateOrder(IOrderModel orderModel)  throws RoutingServiceException {
		return getService().locateOrder(orderModel);
	}
}
