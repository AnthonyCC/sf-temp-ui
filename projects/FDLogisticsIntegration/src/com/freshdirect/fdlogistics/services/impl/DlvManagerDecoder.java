package com.freshdirect.fdlogistics.services.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumWebServiceType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.ecommerce.data.dlv.AddressData;
import com.freshdirect.ecommerce.data.dlv.ErpAddressData;
import com.freshdirect.ecommerce.data.dlv.FDReservationData;
import com.freshdirect.ecommerce.data.dlv.TimeslotData;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.logistics.delivery.model.EnumReservationClass;
import com.freshdirect.logistics.delivery.model.EnumReservationType;

public class DlvManagerDecoder {

	public static FDReservation decodeFDReservation(FDReservationData reservationData) {
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
		FDTimeslot timeSlot = new FDTimeslot();
		BeanUtils.copyProperties(timeslotData, timeSlot);
		Date startTime = new Date(timeslotData.getStartTime());
		timeSlot.setDlvStartTime(new TimeOfDay(startTime));
		Date endTime = new Date(timeslotData.getEndTime());
		timeSlot.setDlvEndTime(new TimeOfDay(endTime));
		timeSlot.setPremiumCutoffTime(new TimeOfDay(timeslotData.getCutoffDateTime()));
		return timeSlot;
	}

	
	
	private static ErpAddressModel decodeErpAddress(ErpAddressData erpAddressData) {
//		ErpAddressModel erpAddressModel = new ErpAddressModel(decodeAddress(erpAddressData.getAddress()));
		
		ErpAddressModel erpAddressModel = new ErpAddressModel();
		try {
//			BeanUtils.copyProperties(erpAddressData, erpAddressModel);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
//		if(erpAddressModel.getAltContactPhone()!=null)
//		erpAddressModel.setAltContactPhone(convertPhoneNumber( erpAddressModel.getAltContactPhone().getPhone(),erpAddressModel.getAltContactPhone().getExtension(),erpAddressModel.getAltContactPhone().getType()));
//		if(erpAddressModel.getAltPhone()!=null)
//		erpAddressModel.setAltPhone(convertPhoneNumber( erpAddressModel.getAltPhone().getPhone(),erpAddressModel.getAltPhone().getExtension(),erpAddressModel.getAltPhone().getType()));
		if(erpAddressModel.getAltDelivery()!=null)
			erpAddressModel.setAltDelivery(EnumDeliverySetting.getDeliverySetting(erpAddressData.getAltDeliverySetting()));
		if(erpAddressModel.getUnattendedDeliveryFlag()!=null)
			erpAddressModel.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.getEnum(erpAddressData.getUnattendedDeliveryFlag()));
		if(erpAddressModel.getWebServiceType()!=null)
			erpAddressModel.setWebServiceType(EnumWebServiceType.getEnum(erpAddressData.getWebServiceType()));
		encodeAddress(erpAddressData.getAddress());
		erpAddressModel.setAddress1(erpAddressData.getAddress().getAddress1());
		erpAddressModel.setApartment(erpAddressData.getAddress().getApartment());
		erpAddressModel.setAddress2(erpAddressData.getAddress().getAddress2());
		erpAddressModel.setAltFirstName(erpAddressData.getAddress().getFirstName());
		erpAddressModel.setLastName(erpAddressData.getAddress().getLastName());
		erpAddressModel.setCity(erpAddressData.getAddress().getCity());
		erpAddressModel.setState(erpAddressData.getAddress().getState());
		erpAddressModel.setZipCode(erpAddressData.getAddress().getZipCode());
		erpAddressModel.setAddressInfo(encodeAddress(erpAddressData.getAddress()));
		
		return erpAddressModel;

	}
	
	private static AddressInfo encodeAddress(AddressData addressData) {
//		AddressModel address = new AddressModel();
//		address.setAddress2(addressData.getAddress2());
//		address.setId(addressData.getId());
				AddressInfo addressInfo = new AddressInfo(addressData.getZoneCode(),  
						addressData.getLongitude(),
						addressData.getLatitude(),  
						addressData.getScrubbedStreet(),
						EnumAddressType.getEnum(addressData.getAddressType()), 
						addressData.getCountry(), 
						addressData.getBuildingId(),
						addressData.getLocationId());
//				address.setAddressInfo(addressInfo);
		return addressInfo;
	}

	
	

}
