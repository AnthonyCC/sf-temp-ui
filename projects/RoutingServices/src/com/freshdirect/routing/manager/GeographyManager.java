package com.freshdirect.routing.manager;

import com.freshdirect.routing.constants.EnumProcessInfoType;
import com.freshdirect.routing.constants.EnumProcessType;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.util.LocationLocatorResult;
import com.freshdirect.routing.util.RoutingUtil;

public class GeographyManager extends BaseProcessManager {

	public void processRequest(ProcessContext request) throws RoutingProcessException  {
		
		GeographyServiceProxy proxy = new GeographyServiceProxy();		
		ILocationModel locModel = request.getLocationInfo();
		IOrderModel orderModel = request.getOrderInfo();
		/*boolean isNew = false;
		
		if(request.getBuildingList() == null) {
			request.setBuildingList(new HashMap());
		}
		
		if(request.getLocationList() == null) {
			request.setLocationList(new HashMap());
		} */
		
		try {
			LocationLocatorResult result = proxy.locateAddress(locModel.getBuilding().getStreetAddress1()
																, locModel.getBuilding().getStreetAddress2()
																, locModel.getApartmentNumber()
																, locModel.getBuilding().getCity()
																, locModel.getBuilding().getState()
																, locModel.getBuilding().getZipCode()
																, locModel.getBuilding().getCountry());
			orderModel.getDeliveryInfo().setDeliveryLocation(result.getLocation());
			/*locModel.getBuilding().setStreetAddress1(proxy.standardizeStreetAddress(locModel));
			ILocationModel locationModel = fetchLocation(request, proxy, locModel);//proxy.getLocation(locModel);			
			
			IBuildingModel buildingModel = null;			
			if(locationModel == null) {
				isNew = true;
				buildingModel = fetchBuilding(request, proxy, locModel);//proxy.getBuildingLocation(locModel);
				locModel.setLocationId(proxy.getLocationId());
				if(buildingModel == null || buildingModel.getBuildingId() == null) {
					
					buildingModel = proxy.getNewBuilding(locModel);					
										
					buildingModel.setServiceTimeType(locModel.getServiceTimeType());
														
					((Map)request.getBuildingList()).put(buildingModel.getSrubbedStreet()
														+"$"+buildingModel.getZipCode(),buildingModel);
					
				}
				locModel.getBuilding().setSrubbedStreet(buildingModel.getSrubbedStreet());	
				locModel.getBuilding().setStreetAddress1(buildingModel.getStreetAddress1());
				locModel.getBuilding().setZipCode(buildingModel.getZipCode());
				locModel.getBuilding().setCountry(buildingModel.getCountry());
				locModel.getBuilding().setCity(buildingModel.getCity());	
				locModel.getBuilding().setState(buildingModel.getState());
				locModel.getBuilding().setBuildingId(buildingModel.getBuildingId());
				locModel.getBuilding().setGeographicLocation(buildingModel.getGeographicLocation());

				((Map)request.getLocationList()).put(locModel.getBuilding().getStreetAddress1()
														+"$"+locModel.getApartmentNumber()
														+"$"+locModel.getBuilding().getZipCode(),locModel);
			} else {
				locModel.setLocationId(locationModel.getLocationId());
				locModel.getBuilding().setBuildingId(locationModel.getBuilding().getBuildingId());
				locModel.getBuilding().setGeographicLocation(locationModel.getBuilding().getGeographicLocation());
				
				locModel.setServiceTimeType(locationModel.getServiceTimeType());
				locModel.setAdjustmentOperator(locationModel.getAdjustmentOperator());
				locModel.setServiceTimeAdjustment(locationModel.getServiceTimeAdjustment());
				locModel.setServiceTimeOverride(locationModel.getServiceTimeOverride());
			}*/
			addProcessInfo(request, orderModel, result.getLocation(), result.isNewLocation());
		} catch (RoutingServiceException e) {			
			e.printStackTrace();
		}		

	}
	
	/*private IBuildingModel fetchBuilding(ProcessContext request
											, GeographyServiceProxy proxy
												, ILocationModel locModel) throws RoutingServiceException {
		
		Object tmpBuildingList = request.getBuildingList();
		if(tmpBuildingList != null) {
			Object tmpBuilding = ((Map)tmpBuildingList).get(locModel.getBuilding().getStreetAddress1()+"$"+locModel.getBuilding().getZipCode());
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
			Object tmpLocation = ((Map)tmpLocationList).get(locModel.getBuilding().getStreetAddress1()
					+"$"+locModel.getApartmentNumber()
					+"$"+locModel.getBuilding().getZipCode());
			if(tmpLocation != null) {		
				return (ILocationModel)tmpLocation;
			}
		}
		
		return proxy.getLocation(locModel);
	} */
	
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
			
			if(locModel.getBuilding().getGeographicLocation() != null 
						&& !RoutingUtil.isGeocodeAcceptable(locModel.getBuilding().getGeographicLocation().getConfidence()
																, locModel.getBuilding().getGeographicLocation().getQuality())) {
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
