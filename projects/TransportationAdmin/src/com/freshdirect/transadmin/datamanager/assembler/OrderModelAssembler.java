package com.freshdirect.transadmin.datamanager.assembler;

import com.freshdirect.routing.model.DeliveryModel;
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
		
		IDeliveryModel deliveryModel = new DeliveryModel();
		deliveryModel.setDeliveryDate(tmpInputModel.getDeliveryDate());
		deliveryModel.setDeliveryEndTime(tmpInputModel.getDeliveryEndTime());
		deliveryModel.setDeliveryModel(tmpInputModel.getDeliveryModel());
		deliveryModel.setDeliveryStartTime(tmpInputModel.getDeliveryStartTime());
		
		IZoneModel zoneModel = new ZoneModel();
		zoneModel.setZoneNumber(tmpInputModel.getDeliveryZone());
		deliveryModel.setDeliveryZone(zoneModel);
		
		ILocationModel locationModel = new LocationModel();
		locationModel.setApartmentNumber(tmpInputModel.getApartmentNumber());
		locationModel.setCity(tmpInputModel.getCity());		
		locationModel.setState(tmpInputModel.getState());		
		locationModel.setStreetAddress1(tmpInputModel.getStreetAddress1());
		locationModel.setStreetAddress2(tmpInputModel.getStreetAddress2());
		locationModel.setZipCode(tmpInputModel.getZipCode());
		locationModel.setCountry(TransportationAdminProperties.getDefaultCountry());
		
		deliveryModel.setDeliveryLocation(locationModel);
		infoModel.setDeliveryInfo(deliveryModel);
		
		return infoModel;
	}
	
	public Object decode(Object objInput) {
		
		IOrderModel tmpOutputModel = (IOrderModel)objInput;
		OrderInfoModel infoModel = new OrderInfoModel();
		infoModel.setLocationId(tmpOutputModel.getDeliveryInfo()
					.getDeliveryLocation().getLocationId());
		infoModel.setOrderNumber(tmpOutputModel.getOrderNumber());
		infoModel.setPackageSize1(""+tmpOutputModel.getDeliveryInfo().getPackagingInfo().getTotalSize1());
		infoModel.setPackageSize2(""+tmpOutputModel.getDeliveryInfo().getPackagingInfo().getTotalSize2());
		infoModel.setServiceTime(""+(long)tmpOutputModel.getDeliveryInfo().getServiceTime());
		infoModel.setDeliveryStartTime(tmpOutputModel.getDeliveryInfo().getDeliveryStartTime());
		infoModel.setDeliveryEndTime(tmpOutputModel.getDeliveryInfo().getDeliveryEndTime());
		infoModel.setDeliveryDate(tmpOutputModel.getDeliveryInfo().getDeliveryDate());	
		
		infoModel.setLocationName(TransportationAdminProperties.getLocationNamePrefix()+infoModel.getLocationId());
		infoModel.setStreetAddress1(tmpOutputModel.getDeliveryInfo()
					.getDeliveryLocation().getStreetAddress1());
		infoModel.setStreetAddress2(tmpOutputModel.getDeliveryInfo()
				.getDeliveryLocation().getStreetAddress2());
		infoModel.setCity(tmpOutputModel.getDeliveryInfo()
				.getDeliveryLocation().getCity());
		infoModel.setZipCode(tmpOutputModel.getDeliveryInfo()
				.getDeliveryLocation().getZipCode());
		infoModel.setCountry(tmpOutputModel.getDeliveryInfo()
				.getDeliveryLocation().getCountry());
		infoModel.setState(tmpOutputModel.getDeliveryInfo()
				.getDeliveryLocation().getState());
		infoModel.setEquipmentType(TransportationAdminProperties.getEquipmentTypePrefix()
										+tmpOutputModel.getDeliveryInfo().getDeliveryZone().getZoneNumber());
		infoModel.setDeliveryZone(tmpOutputModel.getDeliveryInfo().getDeliveryZone().getZoneNumber());
		infoModel.setLatitude(tmpOutputModel.getDeliveryInfo().getDeliveryLocation().getGeographicLocation().getLatitude());
		infoModel.setLongitude(tmpOutputModel.getDeliveryInfo().getDeliveryLocation().getGeographicLocation().getLongitude());
		return infoModel;
	}
}
