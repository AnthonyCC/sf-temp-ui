package com.freshdirect.routing.manager;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.routing.constants.EnumProcessInfoType;
import com.freshdirect.routing.constants.EnumProcessType;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.util.RoutingUtil;

public class GeographyManager extends BaseProcessManager {

	public void processRequest(ProcessContext request) throws RoutingProcessException  {
		
		GeographyServiceProxy proxy = new GeographyServiceProxy();		
		ILocationModel locModel = request.getLocationInfo();
		IOrderModel orderModel = request.getOrderInfo();
		boolean isNew = false;
		
		if(request.getBuildingList() == null) {
			request.setBuildingList(new HashMap());
		}
		
		if(request.getLocationList() == null) {
			request.setLocationList(new HashMap());
		}
		
		try {			
			locModel.setStreetAddress1(proxy.standardizeStreetAddress(locModel));
			ILocationModel locationModel = fetchLocation(request, proxy, locModel);//proxy.getLocation(locModel);			
			
			IBuildingModel buildingModel = null;			
			if(locationModel == null) {
				isNew = true;
				buildingModel = fetchBuilding(request, proxy, locModel);//proxy.getBuildingLocation(locModel);
				locModel.setLocationId(proxy.getLocationId());
				if(buildingModel != null && buildingModel.getBuildingId() != null) {
					
					locModel.setBuildingId(buildingModel.getBuildingId());										
		    		locModel.setGeographicLocation(buildingModel.getGeographicLocation());		    							
					
				} else {
					
					buildingModel = proxy.getNewBuilding(locModel);					
					locModel.setZipCode(buildingModel.getZipCode());
					locModel.setGeographicLocation(buildingModel.getGeographicLocation());
					buildingModel.setServiceTimeType(locModel.getServiceTimeType());
														
					((Map)request.getBuildingList()).put(buildingModel.getSrubbedStreet()
														+"$"+buildingModel.getZipCode(),buildingModel);
				}
				
				locModel.setBuildingId(buildingModel.getBuildingId());
				((Map)request.getLocationList()).put(locModel.getStreetAddress1()
														+"$"+locModel.getApartmentNumber()
														+"$"+locModel.getZipCode(),locModel);
			} else {
				locModel.setLocationId(locationModel.getLocationId());
				locModel.setGeographicLocation(locationModel.getGeographicLocation());
				locModel.setServiceTimeType(locationModel.getServiceTimeType());
			}
			addProcessInfo(request, orderModel, locModel, isNew);
		} catch (RoutingServiceException e) {			
			e.printStackTrace();
		}		

	}
	
	private IBuildingModel fetchBuilding(ProcessContext request
											, GeographyServiceProxy proxy
												, ILocationModel locModel) throws RoutingServiceException {
		
		Object tmpBuildingList = request.getBuildingList();
		if(tmpBuildingList != null) {
			Object tmpBuilding = ((Map)tmpBuildingList).get(locModel.getStreetAddress1()+"$"+locModel.getZipCode());
			if(tmpBuilding != null) {
				return (IBuildingModel)tmpBuilding;
			}
		}
		return proxy.getBuildingLocation(locModel);
	}
	
	private ILocationModel fetchLocation(ProcessContext request
			, GeographyServiceProxy proxy
			, ILocationModel locModel) throws RoutingServiceException {

		Object tmpLocationList = request.getLocationList();
		if(tmpLocationList != null) {
			Object tmpLocation = ((Map)tmpLocationList).get(locModel.getStreetAddress1()
					+"$"+locModel.getApartmentNumber()
					+"$"+locModel.getZipCode());
			if(tmpLocation != null) {
				return (ILocationModel)tmpLocation;
			}
		}
		return proxy.getLocation(locModel);
	}
	
	private void addProcessInfo(ProcessContext request,IOrderModel orderModel, ILocationModel locModel, boolean isNew) {
		if(locModel != null && orderModel != null) {
			
			if(isNew) {
				ProcessInfo  processLocationInfo = new ProcessInfo();
				processLocationInfo.setProcessType(EnumProcessType.LOAD_LOCATIONINFO);
				processLocationInfo.setProcessInfoType(EnumProcessInfoType.INFO);
				processLocationInfo.setOrderId(orderModel.getOrderNumber());
				processLocationInfo.setLocationId(locModel.getLocationId());
				request.addProcessInfo(processLocationInfo);
			}
			
			if(locModel.getGeographicLocation() != null 
						&& !RoutingUtil.isGeocodeAcceptable(locModel.getGeographicLocation().getConfidence()
																, locModel.getGeographicLocation().getQuality())) {
				ProcessInfo  processLocationGeocode = new ProcessInfo();
				processLocationGeocode.setProcessType(EnumProcessType.LOAD_LOCATIONGEOCODE);
				processLocationGeocode.setProcessInfoType(EnumProcessInfoType.WARNING);
				processLocationGeocode.setOrderId(orderModel.getOrderNumber());
				processLocationGeocode.setLocationId(locModel.getLocationId());
				request.addProcessInfo(processLocationGeocode);
			}
		}
		
	}

}
