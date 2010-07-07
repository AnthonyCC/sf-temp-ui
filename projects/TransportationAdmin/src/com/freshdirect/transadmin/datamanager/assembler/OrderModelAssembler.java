package com.freshdirect.transadmin.datamanager.assembler;

import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.transadmin.datamanager.model.OrderInfoModel;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class OrderModelAssembler implements IDataAssembler {
	
	public Object encode(Object objInput) {
		
		OrderInfoModel tmpInputModel = (OrderInfoModel)objInput;
		IOrderModel infoModel = new OrderModel();
		infoModel.setOrderNumber(tmpInputModel.getOrderNumber());
		infoModel.setCustomerNumber(tmpInputModel.getCustomerNumber());
		
		IDeliveryModel deliveryModel = new DeliveryModel();
		deliveryModel.setDeliveryDate(tmpInputModel.getDeliveryDate());
		deliveryModel.setDeliveryEndTime(tmpInputModel.getDeliveryEndTime());
		deliveryModel.setDeliveryModel(tmpInputModel.getDeliveryModel());
		deliveryModel.setDeliveryStartTime(tmpInputModel.getDeliveryStartTime());
		
		IZoneModel zoneModel = new ZoneModel();
		zoneModel.setZoneNumber(tmpInputModel.getDeliveryZone());
		deliveryModel.setDeliveryZone(zoneModel);
		
		IBuildingModel building = new BuildingModel();
				
		building.setCity(tmpInputModel.getCity());		
		building.setState(tmpInputModel.getState());		
		building.setStreetAddress1(tmpInputModel.getStreetAddress1());
		building.setStreetAddress2(tmpInputModel.getStreetAddress2());
		building.setZipCode(tmpInputModel.getZipCode());
		building.setCountry(TransportationAdminProperties.getDefaultCountry());
		
		ILocationModel locationModel = new LocationModel(building);
		locationModel.setApartmentNumber(tmpInputModel.getApartmentNumber());
		deliveryModel.setDeliveryLocation(locationModel);
		infoModel.setDeliveryInfo(deliveryModel);
		
		return infoModel;
	}
	
	public Object decode(Object objInput) {
		
		IOrderModel tmpOutputModel = (IOrderModel)objInput;
		OrderInfoModel infoModel = new OrderInfoModel();
		try {
			infoModel.setLocationId(tmpOutputModel.getDeliveryInfo()
						.getDeliveryLocation().getLocationId());
			infoModel.setOrderNumber(tmpOutputModel.getOrderNumber());
			infoModel.setCustomerNumber(tmpOutputModel.getCustomerNumber());
			infoModel.setPackageSize1(""+tmpOutputModel.getDeliveryInfo().getPackagingInfo().getTotalSize1());
			infoModel.setPackageSize2(""+tmpOutputModel.getDeliveryInfo().getPackagingInfo().getTotalSize2());
			infoModel.setServiceTime(""+(long)tmpOutputModel.getDeliveryInfo().getServiceTime());
			infoModel.setDeliveryStartTime(tmpOutputModel.getDeliveryInfo().getDeliveryStartTime());
			infoModel.setDeliveryEndTime(tmpOutputModel.getDeliveryInfo().getDeliveryEndTime());
			infoModel.setDeliveryDate(tmpOutputModel.getDeliveryInfo().getDeliveryDate());	
			
			infoModel.setLocationName(TransportationAdminProperties.getLocationNamePrefix()+infoModel.getLocationId());
			infoModel.setStreetAddress1(tmpOutputModel.getDeliveryInfo()
						.getDeliveryLocation().getBuilding().getStreetAddress1());
			infoModel.setStreetAddress2(tmpOutputModel.getDeliveryInfo()
					.getDeliveryLocation().getBuilding().getStreetAddress2());
			infoModel.setCity(tmpOutputModel.getDeliveryInfo()
					.getDeliveryLocation().getBuilding().getCity());
			infoModel.setZipCode(tmpOutputModel.getDeliveryInfo()
					.getDeliveryLocation().getBuilding().getZipCode());
			infoModel.setCountry(tmpOutputModel.getDeliveryInfo()
					.getDeliveryLocation().getBuilding().getCountry());
			infoModel.setState(tmpOutputModel.getDeliveryInfo()
					.getDeliveryLocation().getBuilding().getState());
			infoModel.setEquipmentType(TransportationAdminProperties.getEquipmentTypePrefix()
											+tmpOutputModel.getDeliveryInfo().getDeliveryZone().getZoneNumber());
			infoModel.setDeliveryZone(tmpOutputModel.getDeliveryInfo().getDeliveryZone().getZoneNumber());
			infoModel.setLatitude(tmpOutputModel.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation().getLatitude());
			infoModel.setLongitude(tmpOutputModel.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation().getLongitude());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("objInput >>"+objInput);
		}
		return infoModel;
	}
}
