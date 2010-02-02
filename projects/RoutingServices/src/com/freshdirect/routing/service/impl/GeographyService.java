package com.freshdirect.routing.service.impl;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.constants.EnumGeocodeConfidenceType;
import com.freshdirect.routing.constants.EnumGeocodeQualityType;
import com.freshdirect.routing.dao.IGeographyDAO;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IGeocodeResult;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.proxy.stub.roadnet.GeocodeData;
import com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService;
import com.freshdirect.routing.proxy.stub.transportation.Location;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.service.IGeographyService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.util.BaseGeocodeEngine;
import com.freshdirect.routing.service.util.IGeocodeEngine;
import com.freshdirect.routing.util.AddressScrubber;
import com.freshdirect.routing.util.RoutingDataEncoder;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingUtil;

public class GeographyService extends BaseService implements IGeographyService {

	private IGeographyDAO geographyDAOImpl;

	private IGeocodeEngine baseGeocodeEngine = new BaseGeocodeEngine();
	
	public ILocationModel getLocation(ILocationModel model) throws RoutingServiceException  {
		try {
			ILocationModel locModel = geographyDAOImpl.getLocation(model.getStreetAddress1()
											, model.getApartmentNumber(), RoutingUtil.getZipCodes(model.getZipCode()));
			if(locModel.getLocationId() != null) {
				return locModel;
			}
		} catch (SQLException e) {
			//throw new RoutingServiceException(e, IIssue.PROCESS_LOCATION_NOTFOUND);
			
		}
		return null;
	}

	public IGeographyDAO getGeographyDAOImpl() {
		return geographyDAOImpl;
	}

	public void setGeographyDAOImpl(IGeographyDAO geographyDAOImpl) {
		this.geographyDAOImpl = geographyDAOImpl;
	}

	public IGeocodeResult getGeocode(ILocationModel model) throws RoutingServiceException {
		return getGeocode(null, model.getStreetAddress1(), model.getZipCode(), model.getCountry());

	}

	public IGeocodeResult getGeocode(String street, String zipCode, String country) throws RoutingServiceException  {
		
		return getGeocode( null, street, zipCode, country);
	}
	
	private IGeocodeResult getGeocode(IGeocodeEngine geocodeEngine, String street, String zipCode, String country) throws RoutingServiceException  {
		if(geocodeEngine != null) {
			return geocodeEngine.getGeocode( street, zipCode, country);
		} else {
			return baseGeocodeEngine.getGeocode( street, zipCode, country);
		}
	}
	
	public ILocationModel locateOrder(IOrderModel orderModel)  throws RoutingServiceException {
						
		ILocationModel locModel = orderModel.getDeliveryInfo().getDeliveryLocation();
		
		locModel.setStreetAddress1(standardizeStreetAddress(locModel));
		ILocationModel locationModel = getLocation(locModel);			
		
		IBuildingModel buildingModel = null;			
		if(locationModel == null) {				
			buildingModel = getBuildingLocation(locModel);
			
			if(buildingModel != null && buildingModel.getBuildingId() != null) {
																			
	    		locModel.setGeographicLocation(buildingModel.getGeographicLocation());		    							
				
			} else {
				
				buildingModel = getNewBuildingEx(locModel);					
				locModel.setZipCode(buildingModel.getZipCode());
				locModel.setGeographicLocation(buildingModel.getGeographicLocation());
				buildingModel.setServiceTimeType(locModel.getServiceTimeType());
								
				insertBuilding(buildingModel);
				buildingModel = getBuildingLocation(locModel);					
				
			}
			
			locModel.setBuildingId(buildingModel.getBuildingId());
			
			insertLocation(locModel);
			
			locationModel = getLocation(locModel);
			
			locModel.setLocationId(locationModel.getLocationId());
			locModel.setGeographicLocation(locationModel.getGeographicLocation());
			locModel.setServiceTimeType(locationModel.getServiceTimeType());
			
		} else {
			locModel.setLocationId(locationModel.getLocationId());
			locModel.setGeographicLocation(locationModel.getGeographicLocation());
			locModel.setServiceTimeType(locationModel.getServiceTimeType());
		}
		
		return locModel;
	}

	public IGeographicLocation getRoutingLocation(String locationId) throws RoutingServiceException  {

		IGeographicLocation result = new GeographicLocation();

		try {

			TransportationWebService port = getTransportationSuiteBatchService(null);
			if(locationId != null) {
				ILocationModel locModel = geographyDAOImpl.getLocationById(locationId);
				if(locModel != null) {
					Location deliveryLocation = port.retrieveLocationByIdentity
													(RoutingDataEncoder.encodeLocationIdentity
																(getRegion(geographyDAOImpl.getZoneMapping(
																		RoutingUtil.getDouble(locModel.getGeographicLocation().getLatitude()), 
																		RoutingUtil.getDouble(locModel.getGeographicLocation().getLongitude())))
																,RoutingServicesProperties.getDefaultLocationType(), locationId));
					if(deliveryLocation == null) {
						throw new RoutingServiceException(null, IIssue.PROCESS_LOCATION_NOTFOUND);
					} else {
	
						result.setLatitude(""+(double)(deliveryLocation.getLatitude()/1000000.0));
						result.setLongitude(""+(double)(deliveryLocation.getLongitude()/1000000.0));
						result.setConfidence(EnumGeocodeConfidenceType.LOW.getName());
						result.setQuality(EnumGeocodeQualityType.MANUAL.getName());
					}
				}
			}
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (SQLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		}
		return result;
	}
	
	public void sendLocationByIds(List locationIds) throws RoutingServiceException  {

		try {
			if(locationIds != null && locationIds.size() > 0) {
				Location[] result = null;
				List lstOrders = geographyDAOImpl.getLocationByIds(locationIds);
				Map locationByRegion = new HashMap();
				if(lstOrders != null) {
					
					Iterator tmpIterator = lstOrders.iterator();
					ILocationModel locModel = null;
					String _region = null;
					List locations = null;
					while(tmpIterator.hasNext()) {
						locModel = (ILocationModel)tmpIterator.next();
						_region = getRegion(geographyDAOImpl.getZoneMapping(
								RoutingUtil.getDouble(locModel.getGeographicLocation().getLatitude()), 
								RoutingUtil.getDouble(locModel.getGeographicLocation().getLongitude())));
						locations = (List)locationByRegion.get(_region);
						
						if(locations == null) {
							locations = new ArrayList();
							locationByRegion.put(_region, locations);					
						}
						locations.add(locModel);
					}
					tmpIterator = locationByRegion.keySet().iterator();
													
					
					Iterator _locIterator = null;
					while(tmpIterator.hasNext()) {
						_region = (String)tmpIterator.next();
						locations = (List)locationByRegion.get(_region);
						int intCount = 0;
						result = new Location[locations.size()];
						
						_locIterator = locations.iterator();
						while(_locIterator.hasNext()) {
							locModel = (ILocationModel)_locIterator.next();
							if(locModel != null) {					
								result[intCount++] = RoutingDataEncoder.encodeLocation(locModel
																		, _region
																		, RoutingServicesProperties.getDefaultLocationType()
																		, null);
							}
						}
						TransportationWebService port = getTransportationSuiteBatchService(null);
						Location[] saveResult = port.saveLocations(result);
						if(saveResult != null && saveResult.length >0) {
							throw new RoutingServiceException(null, IIssue.PROCESS_LOCATION_SAVEERROR);
						}
					}
				}
				
			}
			
		} catch (SQLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		}
	}

	public List batchGeocode(List dataList) throws RoutingServiceException {

		List lstResult = new ArrayList();
		try {

			RouteNetWebService port = getRouteNetBatchService();
			if(dataList != null) {
				com.freshdirect.routing.proxy.stub.roadnet.Address[] addressLst = getAddressArray(dataList);
				GeocodeData[] geographicData = port.batchGeocode(addressLst, new GeocodeOptions());
				lstResult = getGeographyList(geographicData);
			}

		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_GEOCODE_UNSUCCESSFUL);
		}
		return lstResult;
	}
	
public IBuildingModel getNewBuilding(ILocationModel baseModel) throws RoutingServiceException {
		
		return getNewBuilding(null, baseModel);
	}
	
	public IBuildingModel getNewBuildingEx(ILocationModel baseModel) throws RoutingServiceException {
		
		return processNewBuilding(null, baseModel, false);
	}
	
	public IBuildingModel getNewBuilding(IGeocodeEngine geocodeEngine, ILocationModel baseModel) throws RoutingServiceException {
						
		return processNewBuilding(geocodeEngine, baseModel, true);
	}
	
		
	private IBuildingModel processNewBuilding(IGeocodeEngine geocodeEngine, ILocationModel baseModel, boolean processID) throws RoutingServiceException {
		
		IBuildingModel buildingModel = null;
		IGeocodeResult geocodeResult = getGeocode(geocodeEngine
													, baseModel.getStreetAddress1()
														, baseModel.getZipCode()
															, baseModel.getCountry());
		IGeographicLocation geoLocation = geocodeResult.getGeographicLocation();					
		
		if(!RoutingUtil.isGeocodeAcceptable(geoLocation.getConfidence(), geoLocation.getQuality())) {
			IGeographicLocation storeFrontLocationModel = getLocalGeocode
								(baseModel.getStreetAddress1(), null, baseModel.getZipCode());
			if(storeFrontLocationModel == null) {							
				geoLocation.setConfidence(EnumGeocodeConfidenceType.LOW.getName());
				geoLocation.setQuality(EnumGeocodeQualityType.STOREFRONTUNSUCCESSFULGEOCODE.getName());
			} else {
				geoLocation.setLatitude(storeFrontLocationModel.getLatitude());
				geoLocation.setLongitude(storeFrontLocationModel.getLongitude());
				geoLocation.setConfidence(storeFrontLocationModel.getConfidence());
				geoLocation.setQuality(storeFrontLocationModel.getQuality());
			}
		} 
		
		buildingModel = new BuildingModel();
		
		if(geocodeResult.getAlternateZipcode() != null){
			buildingModel.setZipCode(geocodeResult.getAlternateZipcode());
		} else {
			buildingModel.setZipCode(baseModel.getZipCode());
		}
		
		if(processID) {
			buildingModel.setBuildingId(getBuildingId());
		}
		buildingModel.setSrubbedStreet(baseModel.getStreetAddress1());
		buildingModel.setCity(baseModel.getCity());
		buildingModel.setState(baseModel.getState());		
		buildingModel.setCountry(baseModel.getCountry());
		buildingModel.setGeographicLocation(geoLocation);
		//buildingModel.setServiceTimeType(baseModel.getServiceTimeType());
					
		return buildingModel;
	}

	private List getGeographyList(GeocodeData[] inputDataList) {

		List result = new ArrayList();
		IGeographicLocation resultLocation = null;
		if(inputDataList != null) {
			int size = inputDataList.length;
			for(int intCount=0;intCount < size;intCount++) {
				resultLocation = new GeographicLocation();
				resultLocation.setLatitude(""+(double)(inputDataList[intCount].getCoordinate().getLatitude()/1000000.0));
				resultLocation.setLongitude(""+(double)(inputDataList[intCount].getCoordinate().getLongitude()/1000000.0));
				resultLocation.setConfidence(inputDataList[intCount].getConfidence().getValue());
				resultLocation.setQuality(inputDataList[intCount].getQuality().getValue());
				result.add(resultLocation);
			}
		}
		return result;
	}

	private com.freshdirect.routing.proxy.stub.roadnet.Address[] getAddressArray(List inputDataList) {

		Iterator iterator = inputDataList.iterator();
		ILocationModel tmpInputModel = null;
		com.freshdirect.routing.proxy.stub.roadnet.Address[] addressLst = new com.freshdirect.routing.proxy.stub.roadnet.Address[inputDataList.size()];
		int intCount = 0;

		while(iterator.hasNext()) {
			tmpInputModel = (ILocationModel)iterator.next();
			com.freshdirect.routing.proxy.stub.roadnet.Address address = new com.freshdirect.routing.proxy.stub.roadnet.Address();
			address.setLine1(tmpInputModel.getStreetAddress1());
			address.setPostalCode(tmpInputModel.getZipCode());
			address.setCountry(tmpInputModel.getCountry());
			addressLst[intCount++] = address;
		}

		return addressLst;
	}
	
	public void insertBuilding(IBuildingModel model) throws RoutingServiceException {
		try {
			geographyDAOImpl.insertBuilding(model);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_BUILDING_SAVEERROR);
		}
	}

	public void insertLocation(ILocationModel model) throws RoutingServiceException {
		try {
			geographyDAOImpl.insertLocation(model);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOCATION_SAVEERROR);
		}
	}
	
	public String getLocationId() throws RoutingServiceException {
		try {
			return geographyDAOImpl.getLocationId();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOCATION_NOTFOUND);
		}
	}

	public IBuildingModel getBuildingLocation(ILocationModel model) throws RoutingServiceException {
		try {
			return geographyDAOImpl.getBuildingLocation(model.getStreetAddress1(), RoutingUtil.getZipCodes(model.getZipCode()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_BUILDING_NOTFOUND);
		}
	}

	public IBuildingModel getBuildingLocation(String street, String zipCode) throws RoutingServiceException {
		try {
			return geographyDAOImpl.getBuildingLocation(street, RoutingUtil.getZipCodes(zipCode));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_BUILDING_NOTFOUND);
		}
	}

	public String getBuildingId() throws RoutingServiceException {
		try {
			return geographyDAOImpl.getBuildingId();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_BUILDING_NOTFOUND);
		}
	}

	public void insertBuildings(List dataList) throws RoutingServiceException {
		try {
			geographyDAOImpl.insertBuildings(dataList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_BUILDING_SAVEERROR);
		}
	}

	public void insertLocations(List dataList) throws RoutingServiceException {
		try {
			geographyDAOImpl.insertLocations(dataList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOCATION_SAVEERROR);
		}
	}

	public List getStateList() throws RoutingServiceException{
		try {
			return geographyDAOImpl.getStateList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_STATES_NOTFOUND);
		}
	}

	public IGeographicLocation getLocalGeocode(String srubbedStreet, String apartment, String zipCode) throws RoutingServiceException {
		try {
			return geographyDAOImpl.getLocalGeocode(srubbedStreet, apartment, zipCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOCALGECODE_UNSUCCESSFUL);
		}
	}

	public String standardizeStreetAddress(ILocationModel address) throws RoutingServiceException {
		
		return standardizeStreetAddress(address.getStreetAddress1(), address.getStreetAddress2());
	}
	
	public String standardizeStreetAddress(String address1, String address2) throws RoutingServiceException {
		String streetAddressResult = null;
		//String oldStreetAddress = address.getStreetAddress1();
		try {
			streetAddressResult = AddressScrubber.standardizeForGeocode(address1);
			//streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress1());
		} catch (RoutingServiceException iae1) {
			System.out.println(" >>> Error >> "+address1+"->"+address2);
			try {
				streetAddressResult = AddressScrubber.standardizeForGeocode(address2);
			} catch (RoutingServiceException iae2) {
				System.out.println(" >>> Error2 >> "+address1+"->"+address2);
				throw new RoutingServiceException(iae2, IIssue.PROCESS_ADDRESSSTANDARDIZE_UNSUCCESSFUL);
			}
		}
		return streetAddressResult;
	}

	private String getRegion(List zones) {
		IZoneModel zone = null;
		if(zones != null) {
			Iterator _iterator = zones.iterator();
			while(_iterator.hasNext()) {
				zone = (IZoneModel)_iterator.next();
				if(zone != null && zone.getArea() != null && zone.getArea().isDepot()) {
					break;
				}
			}
		}
		return RoutingUtil.getRegion(zone.getArea());
	}
	
	
}
