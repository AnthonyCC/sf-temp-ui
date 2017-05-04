package com.freshdirect.payment.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.ecommerce.data.dlv.AddressData;
import com.freshdirect.ecommerce.data.dlv.CustomerAvgOrderSizeData;
import com.freshdirect.ecommerce.data.dlv.CustomerData;
import com.freshdirect.ecommerce.data.dlv.OrderContextData;
import com.freshdirect.ecommerce.data.dlv.OrderHistoryData;
import com.freshdirect.ecommerce.data.dlv.ProfileData;
import com.freshdirect.ecommerce.data.dlv.RoutingModelData;
import com.freshdirect.ecommerce.data.dlv.TimeslotEventData;
import com.freshdirect.ecommerce.data.dlv.TimeslotEventDetailData;
import com.freshdirect.logistics.analytics.model.RoutingModel;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.analytics.model.TimeslotEventDetail;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.dto.Customer;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;
import com.freshdirect.logistics.delivery.dto.OrderHistory;
import com.freshdirect.logistics.delivery.dto.Profile;
import com.freshdirect.logistics.delivery.model.OrderContext;

public class DlvManagerEncoder {

	public static AddressData encodeContactAddress(ContactAddressModel address) {
		AddressData data = new AddressData(address.getId(), address.getAddress1(), address.getAddress2(), address.getApartment(), address.getCity(), address.getState(), address.getZipCode(), address.getCountry(), address.getFirstName(), address.getLastName(), 
				address.getScrubbedStreet(), address.getLongitude(), address.getLatitude(), address.getServiceType().getName(), address.getCompanyName());
		return data;
	}

	public static OrderContextData encodeOrderContext(OrderContext context) {
		OrderContextData orderContextData = new OrderContextData();
		orderContextData.setAction(context.getAction().name());
		orderContextData.setOrderId(context.getOrderId());
		orderContextData.setType(context.getType().name());
		return orderContextData;
	}

	public static TimeslotEventData encodeTimeSlotEvent(TimeslotEvent event) {
		TimeslotEventData timeslotEvent = new TimeslotEventData(event.getTransactionSource(), 
				event.isDlvPassApplied(), event.getDeliveryCharge(), event.isDeliveryChargeWaived(), event.isZoneCtActive(), event.getFdUserId(), event.getCompanyCode());
		if(event.getEventType() !=null)
		timeslotEvent.setEventType(event.getEventType().name());
		timeslotEvent.setAddress(event.getAddress());
		timeslotEvent.setResponseTime(event.getResponseTime());
		timeslotEvent.setCustomerId(event.getCustomerId());
		timeslotEvent.setOrderId(event.getOrderId());
		timeslotEvent.setEventDate(event.getEventDate());
		timeslotEvent.setReservationId(event.getReservationId());
		timeslotEvent.setId(event.getId());
		timeslotEvent.setLogoutTime(event.getLogoutTime());
		timeslotEvent.setSector(event.getSector());
		timeslotEvent.setLatitude(event.getLatitude());
		timeslotEvent.setLongitude(event.getLongitude());
		timeslotEvent.setServiceType(event.getServiceType());
		timeslotEvent.setSameDay(event.getSameDay());
		List<TimeslotEventDetailData> timeSlotEventDetails = decodeTimeSlotEventDetail(event);
		timeslotEvent.setDetail(timeSlotEventDetails);
		return timeslotEvent;
	}

	private static List<TimeslotEventDetailData> decodeTimeSlotEventDetail(TimeslotEvent event) {
		
		List<TimeslotEventDetailData> timeslotEventDetails = new ArrayList<TimeslotEventDetailData>();
		if(event.getDetail()!=null)
		for (TimeslotEventDetail timeslotEventDetail : event.getDetail()) {
			TimeslotEventDetailData details = new TimeslotEventDetailData();
			BeanUtils.copyProperties(timeslotEventDetail, details);
			
			/*details.setWs_amount(timeslotEventDetail.getWs_amount());
			details.setAlcohol_restriction(timeslotEventDetail.isAlcohol_restriction());
			details.setHoliday_restriction(timeslotEventDetail.isHoliday_restriction());
			details.setEcofriendlyslot(timeslotEventDetail.isEcofriendlyslot());
			details.setNeighbourhoodslot(timeslotEventDetail.isNeighbourhoodslot());
			details.setTotalCapacity(timeslotEventDetail.getTotalCapacity());
			details.setCtCapacity(timeslotEventDetail.getCtCapacity());
			details.setStoreFrontAvailable(timeslotEventDetail.getStoreFrontAvailable());
			details.setCtAllocated(timeslotEventDetail.getCtAllocated());
			details.setTotalAllocated(timeslotEventDetail.getTotalAllocated());*/
			details.setRoutingModel(loadRountineModel(timeslotEventDetail.getRoutingModel()));
			/*details.setZoneCode(timeslotEventDetail.getZoneCode());
			details.setStartTime(timeslotEventDetail.getStartTime());
			details.setStopTime(timeslotEventDetail.getStopTime());
			details.setRoutingStartTime(timeslotEventDetail.getRoutingStartTime());
			details.setRoutingStopTime(timeslotEventDetail.getRoutingStopTime());
			details.setDeliveryDate(timeslotEventDetail.getDeliveryDate());
			details.setCutOff(timeslotEventDetail.getCutOff());
			details.setManuallyClosed(timeslotEventDetail.isManuallyClosed());
			details.setGeoRestricted(timeslotEventDetail.isGeoRestricted());
			details.setId(timeslotEventDetail.getId());
			details.setGrName(timeslotEventDetail.getGrName());
			details.setBoundaryCode(timeslotEventDetail.getBoundaryCode());*/
			timeslotEventDetails.add(details);
		}
		
		return timeslotEventDetails;
	}

	private static RoutingModelData loadRountineModel(RoutingModel routingModel) {
//		RoutingModelData  routingModelData = new RoutingModelData();
//		BeanUtils.copyProperties(routingModel, routingModelData)
		
		RoutingModelData  routingModelData = new RoutingModelData(routingModel.getAdditionalDistance(), routingModel.getAdditionalRunTime(), routingModel.getAdditionalStopCost(), routingModel.getCostPerMile(), routingModel.getFixedRouteSetupCost(),
				routingModel.getMaxRunTime(), routingModel.getOvertimeHourlyWage(), routingModel.getPrefRunTime(), routingModel.getRegularHourlyWage(), routingModel.getRegularWageDurationSeconds(), routingModel.getRouteId(), routingModel.getStopSequence(), routingModel.getTotalDistance(), routingModel.getTotalRouteCost(), 
				routingModel.getTotalRunTime(), routingModel.getTotalServiceTime(), routingModel.getTotalTravelTime(), routingModel.getTotalWaitTime(), routingModel.isAvailable(), routingModel.isFiltered(), routingModel.isMissedTW(), routingModel.getWaveVehicles(), routingModel.getWaveVehiclesInUse(), routingModel.getWaveStartTime(), routingModel.getUnavailabilityReason(),
				routingModel.getWaveOrdersTaken(), routingModel.getTotalQuantities(), routingModel.isNewRoute(), routingModel.getCapacities());
		return routingModelData;
	}

	public static CustomerData encodeCustomer(Customer customer) {
		CustomerData customerData = new CustomerData();
		BeanUtils.copyProperties(customer, customerData);
		CustomerAvgOrderSize customerAvgOrderSize = customer.getOrderSize();
		if(customerAvgOrderSize !=null){
			CustomerAvgOrderSizeData customerAvgOrderSizeData = new CustomerAvgOrderSizeData();
			try {
				BeanUtils.copyProperties(customerAvgOrderSize,customerAvgOrderSizeData);
			} catch (Exception e) {
				// TODO: handle exception
			}
//			CustomerAvgOrderSize customerAvgOrderSize = new CustomerAvgOrderSize(customerAvgOrderSizeData.getNoOfCartons(), customerAvgOrderSizeData.getNoOfFreezers(), customerAvgOrderSizeData.getNoOfCases());
			customerData.setOrderSize(customerAvgOrderSizeData);
		}
		Address address = customer.getAddress();
		if(address !=null){
			AddressData addressData = decodeAddress(address);
			customerData.setAddress(addressData);
		}
		
		Profile profile = customer.getProfile();
		if(profile !=null){
			ProfileData profileData = new ProfileData();
			try {
				BeanUtils.copyProperties(profile, profileData);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
//			profile.setAttributes(profileData.getAttributes());
			customerData.setProfile(profileData);
		}
		OrderHistory orderHistory = customer.getOrderHistory();
		if(orderHistory !=null){
			OrderHistoryData orderHistoryData = new OrderHistoryData();
			try {
				BeanUtils.copyProperties(orderHistory, orderHistoryData);
			} catch (Exception e) {
				// TODO: handle exception
			}
//			orderHistory.setSettledOrderCount(orderHistoryData.getSettledOrderCount());
			customerData.setOrderHistory(orderHistoryData);
		}
		return customerData;
	}

	private static AddressData decodeAddress(Address address) {
		AddressData addressData = new AddressData();
	/*	AddressData addressData = new AddressData(address.getId(), address.getAddress1(), address.getAddress2(), address.getApartment(), address.getCity(), address.getState(), address.getZipCode(), address.getCountry(), 
				address.getFirstName(), address.getLastName(), address.getScrubbedStreet(), address.getLongitude(), address.getLatitude(), address.getServiceType(), address.getCompanyName());
	*/	try {
			BeanUtils.copyProperties(address, addressData);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return addressData;
	}

}
