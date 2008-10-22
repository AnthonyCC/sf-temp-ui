package com.freshdirect.routing.service.impl;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.freshdirect.delivery.AddressScrubber;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.routing.constants.EnumGeocodeConfidenceType;
import com.freshdirect.routing.constants.EnumGeocodeQualityType;
import com.freshdirect.routing.dao.IGeographyDAO;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.GeocodeResult;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IGeocodeResult;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.proxy.stub.roadnet.GeocodeData;
import com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions;
import com.freshdirect.routing.proxy.stub.roadnet.MapArc;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetPortType;
import com.freshdirect.routing.proxy.stub.transportation.Location;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_PortType;
import com.freshdirect.routing.service.IGeographyService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDataEncoder;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingUtil;

public class GeographyService implements IGeographyService {

	private IGeographyDAO geographyDAOImpl;


	public ILocationModel getLocation(ILocationModel model) throws RoutingServiceException  {
		try {
			ILocationModel locModel = geographyDAOImpl.getLocation(model.getStreetAddress1()
											, model.getApartmentNumber(), RoutingUtil.getZipCodes(model.getZipCode()));
			if(locModel.getLocationId() != null) {
				return locModel;
			}
		} catch (SQLException e) {
			throw new RoutingServiceException(e, IIssue.PROCESS_LOCATION_NOTFOUND);
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
		return getGeocode(model.getStreetAddress1(), model.getZipCode(), model.getCountry());

	}

	public IGeocodeResult getGeocode(String street, String zipCode, String country) throws RoutingServiceException  {
		
		IGeocodeResult geocodeResult = new GeocodeResult();
		IGeographicLocation result = new GeographicLocation();
		geocodeResult.setGeographicLocation(result);
		try {

			RouteNetPortType port = RoutingServiceLocator.getInstance().getRouteNetService();
			com.freshdirect.routing.proxy.stub.roadnet.Address address = new com.freshdirect.routing.proxy.stub.roadnet.Address();
			address.setLine1(street);
			address.setPostalCode(zipCode);
			address.setCountry(country);
			
			GeocodeOptions options = new GeocodeOptions();
			options.setReturnCandidates(true);
			
			GeocodeData geographicData = port.geocodeEx(address, options);
			if(geographicData != null && !RoutingUtil.isGeocodeAcceptable(geographicData.getConfidence().getValue()
												, geographicData.getQuality().getValue())) {
				String alternateZipCode = hasMatchingZipCode(geographicData, zipCode);
				if(alternateZipCode != null) {
					address.setPostalCode(alternateZipCode);
					GeocodeData altGeographicData = port.geocode(address);
					if(altGeographicData != null && !RoutingUtil.isGeocodeAcceptable(altGeographicData.getConfidence().getValue()
							, altGeographicData.getQuality().getValue())) {
						geographicData = altGeographicData;	
						geocodeResult.setAlternateZipcode(alternateZipCode);
					}
				}
			}
			result.setLatitude(""+(double)(geographicData.getCoordinate().getLatitude()/1000000.0));
			result.setLongitude(""+(double)(geographicData.getCoordinate().getLongitude()/1000000.0));
			result.setConfidence(geographicData.getConfidence().getValue());
			result.setQuality(geographicData.getQuality().getValue());

		} catch (ServiceException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_GEOCODE_UNSUCCESSFUL);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_GEOCODE_UNSUCCESSFUL);
		} catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_GEOCODE_UNSUCCESSFUL);
		}
		return geocodeResult;
	}
	
	private String hasMatchingZipCode(GeocodeData geographicData, String baseZipCode) {
		String result = null;
		List zipCodes = RoutingUtil.getZipCodes(baseZipCode);
		MapArc[] candidates = geographicData.getCandidates();
		if(zipCodes != null && candidates != null) {
			for(int intCount=0; intCount<candidates.length;intCount++) {
				if(zipCodes.contains(candidates[intCount].getPostalCode())) {
					result = candidates[intCount].getPostalCode();
				}				
			}
		}
		return result;
	}

	public IGeographicLocation getRoutingLocation(String locationId) throws RoutingServiceException  {

		IGeographicLocation result = new GeographicLocation();

		try {

			TransportationWebService_PortType port = RoutingServiceLocator.getInstance().getTransportationSuiteService();
			if(locationId != null) {
				Location deliveryLocation = port.retrieveLocationByIdentity
												(RoutingDataEncoder.encodeLocationIdentity(RoutingServicesProperties.getDefaultRegion()
															,RoutingServicesProperties.getDefaultLocationType(), locationId));
				if(deliveryLocation == null) {
					throw new RoutingServiceException(null, IIssue.PROCESS_LOCATION_NOTFOUND);
				} else {

					result.setLatitude(""+(double)(deliveryLocation.getLatitude()/1000000.0));
					result.setLongitude(""+(double)(deliveryLocation.getLongitude()/1000000.0));
					result.setConfidence("gcLow");
					result.setQuality("grManual");
				}

			}
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		}
		return result;
	}
	
	public void sendLocationByIds(List locationIds) throws RoutingServiceException  {

		try {
			if(locationIds != null && locationIds.size() > 0) {
				Location[] result = null;
				List lstOrders = geographyDAOImpl.getLocationByIds(locationIds);
				if(lstOrders != null) {
					result = new Location[lstOrders.size()];
					Iterator tmpIterator = lstOrders.iterator();
					ILocationModel locModel = null;			
					int intCount = 0;
					while(tmpIterator.hasNext()) {
						locModel = (ILocationModel)tmpIterator.next();
						if(locModel != null) {					
							result[intCount++] = RoutingDataEncoder.encodeLocation(locModel
																	, RoutingServicesProperties.getDefaultRegion()
																	, RoutingServicesProperties.getDefaultLocationType());
						}
					}
				}
				TransportationWebService_PortType port = RoutingServiceLocator.getInstance().getTransportationSuiteService();
				Location[] saveResult = port.saveLocations(result);
				if(saveResult != null && saveResult.length >0) {
					throw new RoutingServiceException(null, IIssue.PROCESS_LOCATION_SAVEERROR);
				}
			}
			
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		} catch (SQLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		}
	}

	public List batchGeocode(List dataList) throws RoutingServiceException {

		List lstResult = new ArrayList();
		try {

			RouteNetPortType port = RoutingServiceLocator.getInstance().getRouteNetService();
			if(dataList != null) {
				com.freshdirect.routing.proxy.stub.roadnet.Address[] addressLst = getAddressArray(dataList);
				GeocodeData[] geographicData = port.batchGeocode(addressLst, new GeocodeOptions());
				lstResult = getGeographyList(geographicData);
			}

		} catch (ServiceException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_GEOCODE_UNSUCCESSFUL);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_GEOCODE_UNSUCCESSFUL);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_GEOCODE_UNSUCCESSFUL);
		}
		return lstResult;
	}
	
	public IBuildingModel getNewBuilding(ILocationModel baseModel) throws RoutingServiceException {
		
		IBuildingModel buildingModel = null;
		IGeocodeResult geocodeResult = getGeocode(baseModel);
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
				
		buildingModel.setBuildingId(getBuildingId());
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
		} catch (InvalidAddressException iae1) {
			try {
				streetAddressResult = AddressScrubber.standardizeForGeocode(address2);
			} catch (InvalidAddressException iae2) {
				throw new RoutingServiceException(iae2, IIssue.PROCESS_ADDRESSSTANDARDIZE_UNSUCCESSFUL);
			}
		}
		return streetAddressResult;
	}


}
