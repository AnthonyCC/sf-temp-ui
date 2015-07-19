package com.freshdirect.fdlogistics.services.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.framework.util.EnumSearchType;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.controller.data.AddressExceptionData;
import com.freshdirect.logistics.controller.data.Cart;
import com.freshdirect.logistics.controller.data.DateRange;
import com.freshdirect.logistics.controller.data.Order;
import com.freshdirect.logistics.controller.data.request.AddressExceptionRequest;
import com.freshdirect.logistics.controller.data.request.CancelReservationRequest;
import com.freshdirect.logistics.controller.data.request.ConfirmReservationRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZipCodeRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZipRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZoneRequest;
import com.freshdirect.logistics.controller.data.request.PickupLocationsRequest;
import com.freshdirect.logistics.controller.data.request.ReserveTimeslotRequest;
import com.freshdirect.logistics.controller.data.request.SearchRequest;
import com.freshdirect.logistics.controller.data.request.SubscriptionRequest;
import com.freshdirect.logistics.controller.data.request.TimeslotIdRequest;
import com.freshdirect.logistics.controller.data.request.TimeslotRequest;
import com.freshdirect.logistics.controller.data.request.UpdateReservationRequest;
import com.freshdirect.logistics.controller.data.request.ValidateReservationRequest;
import com.freshdirect.logistics.controller.data.request.ZoneRequest;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.dto.Customer;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;
import com.freshdirect.logistics.delivery.model.EnumOrderAction;
import com.freshdirect.logistics.delivery.model.EnumOrderType;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.logistics.delivery.model.EnumReservationStatus;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.ExceptionAddress;
import com.freshdirect.logistics.delivery.model.OrderContext;
import com.freshdirect.logistics.delivery.model.Subscription;
import com.freshdirect.logistics.delivery.model.TimeslotContext;

public class LogisticsDataEncoder {

public static Address encodeAddress(ContactAddressModel model) {
		
		Address address = new Address(model.getId(), model.getAddress1(), model.getApartment(), model.getCity(), model.getState(), model.getZipCode());
		address.setServiceType(model.getServiceType().getName());
		address.setAddress2(model.getAddress2());
		address.setCompanyName(model.getCompanyName());
		return address;
		
	}

	public static Address encodeAddress(AddressModel model) {
		
		Address address = new Address(model.getId(), model.getAddress1(), model.getApartment(), model.getCity(), model.getState(), model.getZipCode());
		address.setServiceType(model.getServiceType().getName());
		address.setAddress2(model.getAddress2());
		address.setCompanyName(model.getCompanyName());
		return address;
		
	}

	public static DeliveryZoneRequest encodeDeliveryZoneRequest(AddressModel address, Date date,
			CustomerAvgOrderSize orderSize, EnumRegionServiceType serviceType) {
		DeliveryZoneRequest zoneRequest = new DeliveryZoneRequest();
		zoneRequest.setAddress(encodeAddress(address));
		zoneRequest.setDeliveryDate(date);
		zoneRequest.setOrderSize(orderSize);
		zoneRequest.setRegionServiceType((serviceType!=null)?serviceType.getName():null);
		return zoneRequest;
	}

	public static ZoneRequest encodeZoneRequest(String zoneCode, Date requestedDate) {
		ZoneRequest zoneRequest = new ZoneRequest();
		zoneRequest.setZoneCode(zoneCode);
		zoneRequest.setDeliveryDate(requestedDate);
		return zoneRequest;
	}

	public static DeliveryZipRequest encodeDeliveryZipRequest(EnumServiceType serviceType) {
		DeliveryZipRequest request = new DeliveryZipRequest();
		request.setServiceType(serviceType.getName());
		return request;
	}

	public static DeliveryZipCodeRequest encodeDeliveryZipCodeRequest(String zipCode) {
		DeliveryZipCodeRequest request = new DeliveryZipCodeRequest();
		request.setZipcode(zipCode);
		return request;
	}

	public static CancelReservationRequest encodeCancelReservationRequest(String rsvId,
			ContactAddressModel address, TimeslotEvent event, boolean restoreReservation) {
		CancelReservationRequest request = new CancelReservationRequest();
		request.setCart(encodeCart(event));
		request.setCustomer(encodeCustomer(address, null));
		request.setReservationId(rsvId);
		request.setRestoreReservation(restoreReservation);
		return request;
	}

	private static Customer encodeCustomer(ContactAddressModel address, String customerId) {
		Customer customer = new Customer();
		customer.setAddress(encodeAddress(address));
		String custId = (customerId == null)?address.getCustomerId():customerId;
		customer.setCustomerId(custId);
		customer.setAlternateCustomerId(custId);
		customer.setFirstName(address.getFirstName());
		customer.setLastName(address.getLastName());
		customer.setOrderSize(new CustomerAvgOrderSize(0,0,0));
		return customer;
	}
	
	public static Customer encodeCustomer(ContactAddressModel address, String customerId, CustomerAvgOrderSize orderSize) {
		Customer customer = encodeCustomer(address, customerId);
		customer.setOrderSize(orderSize);
		return customer;
	}

	private static Cart encodeCart(TimeslotEvent event) {
		Cart cart = new Cart();
		cart.setDlvchargewaived(event.isDeliveryChargeWaived());
		cart.setDlvfee(event.getDeliveryCharge());
		cart.setDlvpassapplied(event.isDlvPassApplied());
		return cart;
	}

	public static ConfirmReservationRequest encodeConfirmReservationRequest(String rsvId,
			String customerId, OrderContext context, ContactAddressModel address, boolean pr1,
			TimeslotEvent event) {
		ConfirmReservationRequest request = new ConfirmReservationRequest();
		request.setOrder(encodeOrder(rsvId, context, encodeCustomer(address, customerId), pr1,
				event));
		return request;
	}

	private static Order encodeOrder(String rsvId,
			OrderContext context , Customer customer, boolean pr1, TimeslotEvent event) {
		Order order = new Order();
		order.setReservationId(rsvId);
		order.setCustomer(customer);
		order.setOrderId(context.getOrderId());
		order.setContext(context);
		order.setPr1(pr1);
		return order;
	}

	public static ReserveTimeslotRequest encodeReserveTimeslotRequest(String timeslotId,
			String customerId, EnumReservationType type,
			ContactAddressModel address, boolean chefsTable,
			String ctDeliveryProfile, boolean isForced, TimeslotEvent event,
			boolean hasSteeringDiscount) {
		ReserveTimeslotRequest request = new ReserveTimeslotRequest(timeslotId, encodeCustomer(address, customerId),
				encodeCart(event), type.getName(), chefsTable, isForced, hasSteeringDiscount);
		return request;
		
	}

	public static TimeslotRequest encodeTimeslotRequest(List<com.freshdirect.framework.util.DateRange> dateranges,
			TimeslotEvent event, ContactAddressModel address,
			CustomerAvgOrderSize orderSize, boolean forceOrder, boolean deliveryInfo, OrderContext context) {
		List<DateRange> ranges = new ArrayList<DateRange>();
		for(com.freshdirect.framework.util.DateRange daterange: dateranges){
			DateRange range = new DateRange(daterange.getStartDate(), daterange.getEndDate());
			ranges = new ArrayList<DateRange>();
			ranges.add(range);
		}
		
		TimeslotRequest request = new TimeslotRequest(ranges, encodeCustomer(address, null, orderSize), 
				encodeCart(event), context, forceOrder, deliveryInfo , 
				(address.getServiceType()!=null)?address.getServiceType().getName():EnumServiceType.HOME.name(), encodeTimeslotContext());
		return request;
		
	}

	private static TimeslotContext encodeTimeslotContext() {
		return TimeslotContext.CHECK_AVAILABLE_TIMESLOTS;
	}
	
	public static TimeslotIdRequest encodeTimeslotRequest(String timeslotId,
			String buildingId, boolean checkPremium) {
		
		TimeslotIdRequest request = new TimeslotIdRequest();
		request.setTimeslotId(timeslotId);
		request.setBuildingId(buildingId);
		request.setCheckPremium(checkPremium);
		return request;
	}

	public static PickupLocationsRequest encodePickupLocationRequest() {
		// TODO Auto-generated method stub
		return new PickupLocationsRequest();
	}

	public static SearchRequest encodeReservationByIdRequest(String rsvId) {
		SearchRequest request = new SearchRequest();
		request.setSearchType(EnumSearchType.RESERVATION_SEARCH.getName());
		request.setCriteriaMap("id", rsvId);
		return request;
	}
	public static SearchRequest encodeReservationByCustomerRequest(String customerId) {
		SearchRequest request = new SearchRequest();
		request.setSearchType(EnumSearchType.RESERVATION_SEARCH.getName());
		request.setCriteriaMap("customerId", customerId);
		request.setCriteriaMap("statusCode", EnumReservationStatus.RESERVED.getCode());
		return request;
	}

	public static SearchRequest encodeReservationByCriteriaRequest(
			GenericSearchCriteria resvCriteria) {
		SearchRequest request = new SearchRequest();
		request.setSearchType(EnumSearchType.RESERVATION_SEARCH.getName());
		Iterator it = resvCriteria.getCriteriaMap().entrySet().iterator();
		while (it.hasNext()) {
		      Map.Entry entry = (Map.Entry)it.next();
		      request.setCriteriaMap(entry.getKey(), entry.getValue());
		}
		return request;
	}

	public static UpdateReservationRequest encodeUpdateReservation(String reservationId, String sapOrderId, Address address) {
		
		UpdateReservationRequest request = new UpdateReservationRequest();
		request.setReservationId(reservationId);
		request.setSapOrderId(sapOrderId);
		request.setAddress(address);
		return request;
	}

	public static AddressExceptionRequest encodeAddressExceptionRequest(
			ExceptionAddress ex) {
		AddressExceptionRequest request = new AddressExceptionRequest();
		request.setAddress(encodeExceptionAddress(ex));
		return request;
	}

	private static AddressExceptionData encodeExceptionAddress(
			ExceptionAddress address) {
		
		return new AddressExceptionData(address.getStreetAddress(), address.getAptNumLow(), address.getAptNumHigh(),
						address.getZip(), (address.getAddressType()!=null)?address.getAddressType().getName():null,
						(address.getReason()!=null)?address.getReason().getName():null, address.getCounty(), address.getState(),
						address.getUserId(), address.getCity(), address.getScrubbedAddress(), address.getLatitude(),
						address.getLongitude());
	}

	public static AddressExceptionRequest encodeAddressExceptionRequest(
			ExceptionAddress ex, String userId) {
		AddressExceptionRequest request = encodeAddressExceptionRequest(ex);
		request.setUserId(userId);
		return request;
	}

	public static ValidateReservationRequest encodeValidateReservation(
			CustomerAvgOrderSize orderSize, FDReservation reservation,
			ErpAddressModel address, TimeslotEvent event) {
		ValidateReservationRequest request = new ValidateReservationRequest();
		request.setCart(encodeCart(event));
		request.setReservationId(reservation.getId());
		request.setAddressDeleted(address == null);
		request.setCustomer(encodeCustomer(address, null, orderSize));
		return request;
	}
	
	public static SubscriptionRequest encodeAddSubscriptionRequest(
			String customerId, String mobileNumber, String textOffers,
			String textDelivery, String orderNotices, String orderExceptions,
			String offers, String partnerMessages, Date receivedDate) {
		SubscriptionRequest request = new SubscriptionRequest();
		
		Subscription subscription = new Subscription();
		subscription.setCustomerId(customerId);
		subscription.setMobileNumber(mobileNumber);
		subscription.setOrderExceptionNotification(orderExceptions);
		subscription.setOrderNotification(orderNotices);
		subscription.setPartnerMessageNotification(partnerMessages);
		subscription.setSmsOffersAlert(textOffers);
		subscription.setSmsOptinDate(receivedDate);
		
		request.setSubscription(subscription);
		return request;
	}
	
}
