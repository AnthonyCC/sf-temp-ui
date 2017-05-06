package com.freshdirect.fdlogistics.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumWebServiceType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.ecommerce.data.dlv.ErpAddressData;
import com.freshdirect.ecommerce.data.dlv.FDReservationData;
import com.freshdirect.ecommerce.data.dlv.TimeslotData;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.logistics.delivery.model.EnumReservationClass;
import com.freshdirect.logistics.delivery.model.EnumReservationType;

public class DlvManagerDecoder {
	
	private static ObjectMapper objectMpper;

	public static FDReservation converter(FDReservationData reservationData) {
		FDReservation fdReservation = new FDReservation(
				new PrimaryKey(reservationData.getId()),
				encodeTimeslot(reservationData.getTimeslot()),	
				reservationData.getExpirationDateTime(),
				EnumReservationType.getEnum(reservationData.getType()),
				reservationData.getCustomerId(), 
				reservationData.getAddressId(),
				reservationData.isChefsTable(),
				reservationData.getOrderId(), 
				reservationData.getStatusCode(), 
				EnumReservationClass.getEnum(reservationData.getRsvClass()), 
				reservationData.isSteeringDiscount(), 
				EnumRegionServiceType.getEnum(reservationData.getRegionServiceType()),
				reservationData.getDeliveryFeeTier());
		fdReservation.setAddress(decodeErpAddress(reservationData.getAddress())); 
	return fdReservation;
	}


	private static FDTimeslot encodeTimeslot(TimeslotData timeslotData) {
		FDTimeslot fdTimeslot = objectMpper.convertValue(timeslotData, FDTimeslot.class);
		return fdTimeslot;
	}

	
	
	private static ErpAddressModel decodeErpAddress(ErpAddressData erpAddressData) {
		ErpAddressModel erpAddressModel = new ErpAddressModel();
		try {
		 erpAddressModel = objectMpper.convertValue(erpAddressData, ErpAddressModel.class);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(erpAddressModel.getAltContactPhone()!=null)
			erpAddressModel.setAltContactPhone(convertPhoneNumber( erpAddressModel.getAltContactPhone().getPhone(),erpAddressModel.getAltContactPhone().getExtension(),erpAddressModel.getAltContactPhone().getType()));
		if(erpAddressModel.getAltPhone()!=null)
			erpAddressModel.setAltPhone(convertPhoneNumber( erpAddressModel.getAltPhone().getPhone(),erpAddressModel.getAltPhone().getExtension(),erpAddressModel.getAltPhone().getType()));
		if(erpAddressData.getAltDeliverySetting()!=null)
			erpAddressModel.setAltDelivery(EnumDeliverySetting.getDeliverySetting(erpAddressData.getAltDeliverySetting()));
		if(erpAddressData.getUnattendedDeliveryFlag()!=null)
			erpAddressModel.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.getEnum(erpAddressData.getUnattendedDeliveryFlag()));
		if(erpAddressData.getWebServiceType()!=null)
			erpAddressModel.setWebServiceType(EnumWebServiceType.getEnum(erpAddressData.getWebServiceType()));

		return erpAddressModel;

	}

	public static void setMapper(ObjectMapper mapper) {
		objectMpper=mapper;
		
	}

	private final static PhoneNumber convertPhoneNumber(String phone, String extension, String type) {
		return new PhoneNumber(phone, NVL.apply(extension, ""), NVL.apply(type, ""));
	}
	

}
