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
import com.freshdirect.logistics.controller.data.request.AddressScrubbingRequest;
import com.freshdirect.logistics.controller.data.request.CancelReservationRequest;
import com.freshdirect.logistics.controller.data.request.ConfirmReservationRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZipCodeRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZipRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZoneRequest;
import com.freshdirect.logistics.controller.data.request.FdxDeliveryInfoRequest;
import com.freshdirect.logistics.controller.data.request.PickupLocationsRequest;
import com.freshdirect.logistics.controller.data.request.ReconfirmReservationRequest;
import com.freshdirect.logistics.controller.data.request.RemoveStandingOrderRequest;
import com.freshdirect.logistics.controller.data.request.ReservationSearchRequest;
import com.freshdirect.logistics.controller.data.request.ReserveTimeslotRequest;
import com.freshdirect.logistics.controller.data.request.SOReserveTimeslotRequest;
import com.freshdirect.logistics.controller.data.request.SearchRequest;
import com.freshdirect.logistics.controller.data.request.SignatureRequest;
import com.freshdirect.logistics.controller.data.request.SubscriptionRequest;
import com.freshdirect.logistics.controller.data.request.TimeslotIdRequest;
import com.freshdirect.logistics.controller.data.request.TimeslotRequest;
import com.freshdirect.logistics.controller.data.request.UpdateReservationRequest;
import com.freshdirect.logistics.controller.data.request.ValidateReservationRequest;
import com.freshdirect.logistics.controller.data.request.ZoneRequest;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.dto.Customer;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;
import com.freshdirect.logistics.delivery.dto.ScrubbedAddress;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.logistics.delivery.model.EnumReservationStatus;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.ExceptionAddress;
import com.freshdirect.logistics.delivery.model.OrderContext;
import com.freshdirect.logistics.delivery.model.Subscription;
import com.freshdirect.logistics.delivery.model.TimeslotContext;
import com.freshdirect.logistics.fdx.controller.data.request.CreateOrderRequest;
import com.freshdirect.logistics.fdx.controller.data.request.DeliveryConfirmationRequest;

public class LogisticsDataEncoder {

	public static Address encodeAddress(ContactAddressModel model) {
		
		Address address = new Address(model.getId(), model.getAddress1(), model.getAddress2(), model.getApartment(), model.getCity(), model.getState(), model.getZipCode(),
				model.getCountry(), model.getFirstName(), model.getLastName(), model.getScrubbedStreet(), model.getLongitude(), model.getLatitude(), model.getServiceType().getName(),
				model.getCompanyName());
		return address;
		
	}

	public static Address encodeAddress(AddressModel model) {
		
		Address address = new Address(model.getId(), model.getAddress1(), model.getAddress2(), model.getApartment(), model.getCity(), model.getState(), model.getZipCode(),
				model.getCountry(), "", "", model.getScrubbedStreet(), model.getLongitude(), model.getLatitude(), model.getServiceType().getName(),
				model.getCompanyName());
		return address;
		
	}

	
	public static DeliveryConfirmationRequest encodeDeliveryConfirmationRequest(String orderId, 
			  String estimatedDeliveryTime)
	{
		DeliveryConfirmationRequest request = new DeliveryConfirmationRequest(orderId,estimatedDeliveryTime);
	return request;
	}
	
	public static SignatureRequest encodeCaptureSignatureRequest(String orderId, 
			String signature,String deliveredTo,String signatureTimestamp)
	{
		SignatureRequest request = new SignatureRequest(orderId, signature,deliveredTo,signatureTimestamp);
	return request;
	}
	
	public static FdxDeliveryInfoRequest encodeFdxDeliveryInfoRequest(String erpOrderId, String confirmationTimestamp,
			String nextStopId,String estDeliveryTime)
	{
		FdxDeliveryInfoRequest request = new FdxDeliveryInfoRequest(erpOrderId,confirmationTimestamp,nextStopId,estDeliveryTime);
		return request;
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

	public static Customer encodeCustomer(ContactAddressModel address, String customerId) {
		Customer customer = new Customer();
		customer.setAddress(encodeAddress(address));
		String custId = (customerId == null)?address.getCustomerId():customerId;
		customer.setCustomerId(custId);
		customer.setAlternateCustomerId(custId);
		customer.setFirstName(address.getFirstName());
		customer.setLastName(address.getLastName());
		customer.setServiceType(address.getServiceType().getName());
		customer.setOrderSize(new CustomerAvgOrderSize(0,0,0));
		return customer;
	}
	
	public static Customer encodeCustomer(ContactAddressModel address, String customerId, CustomerAvgOrderSize orderSize) {
		Customer customer = encodeCustomer(address, customerId);
		customer.setOrderSize(orderSize);
		return customer;
	}
	
	public static Customer encodeCustomer( String customerId) {
		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		return customer;
	}
	
	private static Cart encodeCart(TimeslotEvent event) {
		Cart cart = new Cart();
		cart.setDlvchargewaived(event.isDeliveryChargeWaived());
		cart.setDlvfee(event.getDeliveryCharge());
		cart.setDlvpassapplied(event.isDlvPassApplied());
		cart.setReservationId(event.getReservationId());
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
			Customer customer, boolean chefsTable,
			String ctDeliveryProfile, boolean isForced, TimeslotEvent event,
			boolean hasSteeringDiscount, String deliveryFeeTier) {
		ReserveTimeslotRequest request = new ReserveTimeslotRequest(timeslotId, customer,
				encodeCart(event), type.getName(), chefsTable, isForced, hasSteeringDiscount, deliveryFeeTier);
		request.setApplicationId(event.getTransactionSource());
		return request;
		
	}

	public static TimeslotRequest encodeTimeslotRequest(List<com.freshdirect.framework.util.DateRange> dateranges,
			TimeslotEvent event, Customer customer, boolean forceOrder, boolean deliveryInfo, 
			OrderContext context, TimeslotContext timeslotContext,boolean isNewSO3Enabled) {
		List<DateRange> ranges = new ArrayList<DateRange>();
		for(com.freshdirect.framework.util.DateRange daterange: dateranges){
			DateRange range = new DateRange(daterange.getStartDate(), daterange.getEndDate());
			ranges.add(range);
		}
		
		TimeslotRequest request = new TimeslotRequest(ranges, customer, 
				encodeCart(event), context, forceOrder, deliveryInfo , 
				(customer.getAddress().getServiceType()!=null)?customer.getAddress().getServiceType():EnumServiceType.HOME.name(),
						timeslotContext, event.isLogged(),isNewSO3Enabled);
		request.setApplicationId(event.getTransactionSource());
		return request;
		
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

	public static SearchRequest encodeReservationByIdRequest(String applicationId, String rsvId) {
		SearchRequest request = new SearchRequest();
		request.setApplicationId(applicationId);
		request.setSearchType(EnumSearchType.RESERVATION_SEARCH.getName());
		request.setCriteriaMap("id", rsvId);
		return request;
	}
	public static SearchRequest encodeReservationByCustomerRequest(String applicationId, String customerId) {
		SearchRequest request = new SearchRequest();
		request.setApplicationId(applicationId);
		request.setSearchType(EnumSearchType.RESERVATION_SEARCH.getName());
		request.setCriteriaMap("customerId", customerId);
		request.setCriteriaMap("statusCode", EnumReservationStatus.RESERVED.getCode());
		return request;
	}
	
	public static SOReserveTimeslotRequest encodeReservesoTemplateRequest(String templateId, 
			String timeslotId,String dayOfWeek,
			CustomerAvgOrderSize orderSize,
			String customerId,ErpAddressModel address,boolean isNewSo) {
		SOReserveTimeslotRequest request = new SOReserveTimeslotRequest();
		request.setTemplateId(templateId);
		request.setTimeslotId(timeslotId);
		request.setDayOfWeek(dayOfWeek);
		request.setCustomer(encodeCustomer(address,customerId,orderSize));
		request.setNewSo(isNewSo);
		return request;
	}
	
	public static RemoveStandingOrderRequest encodeRemovesoTemplateRequest(List<String> soIds) {
		RemoveStandingOrderRequest requests = new RemoveStandingOrderRequest();
	List<SOReserveTimeslotRequest> requestss = new ArrayList<SOReserveTimeslotRequest>();
		for(String soId:soIds)
		{
		SOReserveTimeslotRequest request = new SOReserveTimeslotRequest();
		request.setTemplateId(soId);
		request.setTimeslotId(null);
		request.setDayOfWeek(null);
		request.setCustomer(null);
		requestss.add(request);
		}
		requests.setSoIds(requestss);
		return requests;
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
	
	public static ReservationSearchRequest encodeReservationByCriteriaRequest(
			GenericSearchCriteria resvCriteria, String initiator, String notes) {
		ReservationSearchRequest request = new ReservationSearchRequest();
		request.setSearchType(EnumSearchType.RESERVATION_SEARCH.getName());
		Iterator it = resvCriteria.getCriteriaMap().entrySet().iterator();
		while (it.hasNext()) {
		      Map.Entry entry = (Map.Entry)it.next();
		      request.setCriteriaMap(entry.getKey(), entry.getValue());
		}
		
		request.setInitiator(initiator);
		request.setNotes(notes);
		
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
			String offers, String partnerMessages, Date receivedDate, String companyCode) {
		SubscriptionRequest request = new SubscriptionRequest();
		
		Subscription subscription = new Subscription();
		subscription.setCustomerId(customerId);
		subscription.setMobileNumber(mobileNumber);
		subscription.setOrderExceptionNotification(orderExceptions);
		subscription.setOrderNotification(orderNotices);
		subscription.setPartnerMessageNotification(partnerMessages);
		subscription.setSmsOffersAlert(offers);
		subscription.setSmsOptinDate(receivedDate);
		subscription.setCompanyCode(companyCode);
		
		request.setSubscription(subscription);
		return request;
	}

	public static CreateOrderRequest encodeUpdateOrderRequest(String orderId,
			String parentOrderId, double tip, String reservationId,String firstName,String lastName, 
			String deliveryInstructions,String serviceType, String unattendedInstr,String orderMobileNumber,String erpOrderId) {
		CreateOrderRequest request = new CreateOrderRequest(orderId, parentOrderId, tip, reservationId,
				firstName,lastName, deliveryInstructions,serviceType,unattendedInstr,orderMobileNumber,erpOrderId);
		return request;
	}
	
	public static CreateOrderRequest encodeCancelFDXOrderRequest(String orderId) {
		CreateOrderRequest request = new CreateOrderRequest();
		request.setOrderId(orderId);
		return request;
	}
	
	public static CreateOrderRequest encodeDispatchtOrderRequest(String orderId) {
		CreateOrderRequest request = new CreateOrderRequest();
		request.setOrderId(orderId);
		return request;
	}
	
	/**
	 * @param addressModels
	 * @return
	 */
	public static AddressScrubbingRequest encodeAddressScrubbingRequest(List<AddressModel> addressModels){
		
		AddressScrubbingRequest addressScrubbingRequest = new AddressScrubbingRequest();
		List<ScrubbedAddress> addresses = new ArrayList<ScrubbedAddress>();
		for(AddressModel addressModel : addressModels){
			ScrubbedAddress scrubbedAddress = new ScrubbedAddress();
			
			if(addressModel.getId() != null && !"".equals(addressModel.getId())){
				scrubbedAddress.setId(addressModel.getId());
			}
			scrubbedAddress.setCity(addressModel.getCity());
			if(addressModel.getZipCode() != null){
				scrubbedAddress.setZipCode(addressModel.getZipCode());
			}
			scrubbedAddress.setAddress1(addressModel.getAddress1());
			if( "".equals(addressModel.getAddress2())){
				scrubbedAddress.setAddress2(addressModel.getAddress2());
			}
			if( "".equals(addressModel.getApartment())){
				scrubbedAddress.setApartment(addressModel.getApartment());
			}
			scrubbedAddress.setState(addressModel.getState());
			addresses.add(scrubbedAddress);
		}
		addressScrubbingRequest.setScrubAddress(addresses);
		return addressScrubbingRequest;
	}
	
	public static ReconfirmReservationRequest encodeReconfirmReservationRequest(String rsvId,
			String customerId, OrderContext context, ContactAddressModel address, boolean pr1) {
		ReconfirmReservationRequest request = new ReconfirmReservationRequest();
		request.setOrder(encodeOrder(rsvId, context, encodeCustomer(address,customerId),pr1,null));
		
		return request;
	}
}
