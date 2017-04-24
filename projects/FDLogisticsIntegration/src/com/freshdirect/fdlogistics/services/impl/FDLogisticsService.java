package com.freshdirect.fdlogistics.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.services.ILogisticsService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.analytics.model.LateIssueOrder;
import com.freshdirect.logistics.analytics.model.SessionEvent;
import com.freshdirect.logistics.controller.data.Depots;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.request.AddressExceptionRequest;
import com.freshdirect.logistics.controller.data.request.AddressScrubbingRequest;
import com.freshdirect.logistics.controller.data.request.AddressVerificationRequest;
import com.freshdirect.logistics.controller.data.request.CancelReservationRequest;
import com.freshdirect.logistics.controller.data.request.ConfirmReservationRequest;
import com.freshdirect.logistics.controller.data.request.DeleteAddressRequest;
import com.freshdirect.logistics.controller.data.request.DeliverySignatureRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZipCodeRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZipRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZoneRequest;
import com.freshdirect.logistics.controller.data.request.FdxDeliveryInfoRequest;
import com.freshdirect.logistics.controller.data.request.GeoLocationRequest;
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
import com.freshdirect.logistics.controller.data.request.UpdateOrderSizeRequest;
import com.freshdirect.logistics.controller.data.request.UpdateReservationRequest;
import com.freshdirect.logistics.controller.data.request.ValidateReservationRequest;
import com.freshdirect.logistics.controller.data.request.ZoneRequest;
import com.freshdirect.logistics.controller.data.response.AddressCheckResponse;
import com.freshdirect.logistics.controller.data.response.AddressExceptionResponse;
import com.freshdirect.logistics.controller.data.response.AddressScrubbingResponse;
import com.freshdirect.logistics.controller.data.response.AddressVerificationResponse;
import com.freshdirect.logistics.controller.data.response.DeleteReservationsResponse;
import com.freshdirect.logistics.controller.data.response.DeliveryETA;
import com.freshdirect.logistics.controller.data.response.DeliveryReservations;
import com.freshdirect.logistics.controller.data.response.DeliveryServices;
import com.freshdirect.logistics.controller.data.response.DeliveryTimeslots;
import com.freshdirect.logistics.controller.data.response.DeliveryZips;
import com.freshdirect.logistics.controller.data.response.DeliveryZoneCapacity;
import com.freshdirect.logistics.controller.data.response.DeliveryZoneCutoffs;
import com.freshdirect.logistics.controller.data.response.DeliveryZones;
import com.freshdirect.logistics.controller.data.response.Employees;
import com.freshdirect.logistics.controller.data.response.FulfillmentInfoResponse;
import com.freshdirect.logistics.controller.data.response.ListOfDates;
import com.freshdirect.logistics.controller.data.response.ListOfObjects;
import com.freshdirect.logistics.controller.data.response.ListOfStateCounty;
import com.freshdirect.logistics.controller.data.response.Timeslot;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.model.GeoLocation;
import com.freshdirect.logistics.delivery.model.RouteStopInfo;
import com.freshdirect.logistics.delivery.model.ShippingDetail;
import com.freshdirect.logistics.fdstore.StateCounty;
import com.freshdirect.logistics.fdx.controller.data.request.CreateOrderRequest;
import com.freshdirect.logistics.fdx.controller.data.request.DeliveryConfirmationRequest;

public class FDLogisticsService extends AbstractLogisticsService implements ILogisticsService {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDLogisticsService.class);

	private static final String ACTIVE_ZONES_API ="/delivery/zones/active";
	private static final String ALL_ACTIVE_ZONES_API = "/delivery/zones/active/all";
	private static final String ZONE_CUTOFF_API ="/delivery/zone/cutoffs";
	
	private static final String GET_ZONEINFO ="/delivery/zone";
	
	private static final String GET_ZONE_CAPACITY ="/delivery/zone/capacity";
	
	private static final String GET_ZIPS_BYSERVICETYPE ="/delivery/zips";
	
	private static final String GET_SERVICETYPES_BYZIP ="/delivery/zip/servicetype";

	private static final String GET_SERVICETYPES_BYADDRESS ="/delivery/address/servicetype";

	private static final String DATE_CUTOFF_API ="/delivery/date/cutoffs";
	private static final String STATECOUNTY_BYZIP_API ="/address/county/";
	private static final String STATECOUNTY__API ="/address/county";
	
	private static final String GEOCODEEXCEPTION_ADD_API ="/address/geocode/exception/add";
	private static final String GEOCODEEXCEPTION_DEL_API ="/address/geocode/exception/delete";
	private static final String GEOCODEEXCEPTION_GET_API ="/address/geocode/exception/get";
	private static final String ADDRESSEXCEPTION_ADD_API ="/address/exception/add";
	private static final String ADDRESSEXCEPTION_DEL_API ="/address/exception/delete";
	private static final String ADDRESSEXCEPTION_GET_API ="/address/exception/get";
	
	private static final String ADDRESSVERIFY_API ="/address/verify";
	private static final String ADDRESSGEOCODE_API ="/address/geocode";
	private static final String ADDRESSCHECK_API ="/address/check";
	
	private static final String RESERVATION_GET_API ="/reservation/get";
	
	private static final String DELIVERY_ETA_API ="/delivery/eta";
	
	private static final String RESERVATION_SIZE_UPDATE_API ="/reservation/update/erpId";
	
	private static final String RESERVATION_CANCEL_API ="/reservation/cancel";
	private static final String RESERVATION_BULKCANCEL_API ="/reservation/bulkcancel";
	
	private static final String RESERVATION_CONFIRM_API ="/reservation/confirm";
	
	private static final String RESERVE_TIMESLOT_API ="/reservation/reserve";
	
	private static final String VALIDATE_RESERVATION_API ="/reservation/validate";
	
	private static final String GET_TIMESLOT_API ="/timeslots/get";
	
	private static final String GET_TIMESLOT_BYID_API ="/timeslots/get/";
	
	private static final String GET_RESERVATION_BYID_API ="/reservation/get/";
	
	private static final String GET_PICKUPLOCATIONS_API ="/delivery/pickuplocations";
	
	private static final String GET_EMPLOYEES_API ="/delivery/resource/employees";
	private static final String LOG_SESSION_EVENT_API ="/event/session";
	
	private static final String LOG_LATEORDER_API ="/event/lateissue";
	
	private static final String ADD_SUBSCRIPTION_API ="/delivery/messaging/subscription/add";
	private static final String FULFILLMENTINFO_API ="/delivery/fulfillmentinfo/";
	
	private static final String RESERVE_SOTEMPLATE_API ="/reservation/sotemplate/reserve";
	private static final String CANCEL_SOTEMPLATE_API ="/reservation/sotemplate/cancel/";
	private static final String ACTIVATE_SOTEMPLATE_API ="/reservation/sotemplate/activate/";
	private static final String REMOVE_SOTEMPLATE_API ="/reservation/sotemplate/delete";
	
	private static final String ADD_APARTMENT_API ="/address/apartment/add";
	private static final String ADD_ADDRESS_API ="/address/add";
	private static final String ADDRESS_SUGGESTIONS_API ="/address/suggestions";
	
	private static final String ZONE_BYGEOLOC_API ="/delivery/zone/bygeoloc";
	//fdx
	
	private static final String CREATE_FDX_ORDER_API ="/order/create";
	private static final String MODIFY_FDX_ORDER_API ="/order/modify";
	private static final String CANCEL_FDX_ORDER_API ="/order/cancel";
	private static final String STORE_DELIVERY_CONFIRM_FDX ="/delivery/deliveryconfirm";
	private static final String STORE_SIGNATURE_FDX ="/delivery/signature";
	private static final String STORE_NEXT_STOP_FDX ="/delivery/nextstop";
	private static final String STORE_DELIVERY_INFO_FDX ="/delivery/fdxdeliveryinfo";
	private static final String STORE_DELIVERY_EVENT ="/delivery/event/";

	private static final String ACTUAL_ORDERSIZE_API ="/reservation/ordersize/update";
	private static final String STATUS_FDX_ORDER_DISPATCH_API="/order/orderdispatch/";
	
	private static final String ADDRESS_SCRUBBING_API ="/address/scrubbing";
	
	private static final String GET_TRUCK_DETAILS_API ="/delivery/trucks/";
	private static final String RESERVATION_RECONFIRM_API ="/reservation/reconfirm";
	private static final String ROUTE_STOP_INFO ="/order/routestopInfo/";


	@Override
	public AddressVerificationResponse verifyAddress(Address address) throws FDLogisticsServiceException {
		AddressVerificationRequest request = new AddressVerificationRequest();
	    request.setAddress(address);
		String inputJson = buildRequest(request);
		LOGGER.info(inputJson);
		AddressVerificationResponse response =  getData(inputJson, getEndPoint(ADDRESSVERIFY_API), AddressVerificationResponse.class);
		return response;		
	}
	
	@Override
	public AddressVerificationResponse geocodeAddress(Address address) throws FDLogisticsServiceException {
		AddressVerificationRequest request = new AddressVerificationRequest();
		request.setAddress(address);
		String inputJson = buildRequest(request);
		AddressVerificationResponse response =  getData(inputJson, getEndPoint(ADDRESSGEOCODE_API), AddressVerificationResponse.class);
		return response;		
	}
	
	@Override
	public Result addApartment(Address address) throws FDLogisticsServiceException {
		AddressVerificationRequest request = new AddressVerificationRequest();
		request.setAddress(address);
		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(ADD_APARTMENT_API), Result.class);
		return response;	
	}
	
	@Override
	public Result captureDeliveryConfirmation(DeliveryConfirmationRequest deliveryConfirmationRequest) throws FDLogisticsServiceException {
		String inputJson = buildRequest(deliveryConfirmationRequest);
		Result response = getData(inputJson, getEndPoint(STORE_DELIVERY_CONFIRM_FDX), Result.class);
		return response;	
	}
	
	@Override
	public Result captureSignature(SignatureRequest signatureRequest) throws FDLogisticsServiceException {
		String inputJson = buildRequest(signatureRequest);
		Result response = getData(inputJson, getEndPoint(STORE_SIGNATURE_FDX), Result.class);
		return response;	
	}
	
	
	@Override
	public Result captureFdxDeliveryInfo(FdxDeliveryInfoRequest fdxDeliveryInfo) throws FDLogisticsServiceException {
		String inputJson = buildRequest(fdxDeliveryInfo);
		Result response = getData(inputJson, getEndPoint(STORE_DELIVERY_INFO_FDX), Result.class);
		return response;	
	}
	
	

	@Override
	public Result addAddressException(AddressExceptionRequest request) throws FDLogisticsServiceException {
	
		String inputJson = buildRequest(request);
		AddressExceptionResponse response =  getData(inputJson, getEndPoint(ADDRESSEXCEPTION_ADD_API), AddressExceptionResponse.class);
		return response;	
	}

	@Override
	public Result cancelOrder(CreateOrderRequest request) throws FDLogisticsServiceException{
		String inputJson = buildRequest(request);
		Result response = getData(inputJson, getEndPoint(CANCEL_FDX_ORDER_API), Result.class);
		return response;
	}
	
	@Override
	public AddressExceptionResponse searchAddressException(AddressExceptionRequest request) throws FDLogisticsServiceException {

		String inputJson = buildRequest(request);
		AddressExceptionResponse response =  getData(inputJson, getEndPoint(ADDRESSEXCEPTION_GET_API), AddressExceptionResponse.class);
		return response;
		
	}

	
	@Override
	public Result removeOrdersfromLogistics(RemoveStandingOrderRequest request)throws FDLogisticsServiceException {

		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(REMOVE_SOTEMPLATE_API), AddressExceptionResponse.class);
		return response;
		
	}
	
	
	@Override
	public Result deleteAddressException(String id) throws FDLogisticsServiceException {

		DeleteAddressRequest request = new DeleteAddressRequest();
		request.setId(id);
		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(ADDRESSEXCEPTION_DEL_API), Result.class);
		return response;		
	
	}

	@Override
	public Result addGeocodeException(AddressExceptionRequest request) throws FDLogisticsServiceException {

		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(GEOCODEEXCEPTION_ADD_API), Result.class);
		return response;
		
		
	}

	@Override
	public AddressExceptionResponse searchGeocodeException(AddressExceptionRequest request) throws FDLogisticsServiceException {

		String inputJson = buildRequest(request);
		AddressExceptionResponse response =  getData(inputJson, getEndPoint(GEOCODEEXCEPTION_GET_API), AddressExceptionResponse.class);
		return response;
	
	}

	@Override
	public Result deleteGeocodeAddress(AddressExceptionRequest request) throws FDLogisticsServiceException {

		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(GEOCODEEXCEPTION_DEL_API), Result.class);
		return response;
		
	}
	
	@Override
	public DeliveryETA getDeliveryETA(String orderId) throws FDLogisticsServiceException{


		DeliverySignatureRequest request = new DeliverySignatureRequest();
		request.setOrderId(orderId);
		String inputJson = buildRequest(request);
		DeliveryETA response =  getData(inputJson, getEndPoint(DELIVERY_ETA_API), DeliveryETA.class);
		return response;
	}

	@Override
	public AddressCheckResponse checkAddress(Address address) throws FDLogisticsServiceException {
		
		String inputJson = buildRequest(address);
		AddressCheckResponse response =  getData(inputJson, getEndPoint(ADDRESSCHECK_API), AddressCheckResponse.class);
		return response;
		
	}

	@Override
	public DeliveryServices getDeliveryServices(Address address) throws FDLogisticsServiceException {

		String inputJson = buildRequest(address);
		DeliveryServices response =  getData(inputJson, getEndPoint(GET_SERVICETYPES_BYADDRESS), DeliveryServices.class);
		return response;
	
		
	}

	@Override
	public DeliveryServices getDeliveryServices(DeliveryZipCodeRequest request) throws FDLogisticsServiceException {
		
		String inputJson = buildRequest(request);
		DeliveryServices response =  getData(inputJson, getEndPoint(GET_SERVICETYPES_BYZIP), DeliveryServices.class);
		return response;
	
	}

	@Override
	public DeliveryZips getDeliverableZipCodes(DeliveryZipRequest request) throws FDLogisticsServiceException {
		
		String inputJson = buildRequest(request);
		DeliveryZips response =  getData(inputJson, getEndPoint(GET_ZIPS_BYSERVICETYPE), DeliveryZips.class);
		return response;	
	
	}

	@Override
	public DeliveryZoneCutoffs getZoneCutoffs(ZoneRequest request) throws FDLogisticsServiceException {
		String inputJson = buildRequest(request);
		DeliveryZoneCutoffs response =  getData(inputJson, getEndPoint(ZONE_CUTOFF_API), DeliveryZoneCutoffs.class);
		return response;
		
	}
	
	@Override
	public ListOfDates getDateCutoffs(Date requestedDate) throws FDLogisticsServiceException {
		ZoneRequest request = new ZoneRequest();
		request.setDeliveryDate(requestedDate);
		String inputJson = buildRequest(request);
		ListOfDates response =  getData(inputJson, getEndPoint(DATE_CUTOFF_API), ListOfDates.class);
		return response;
		
	}

	@Override
	public DeliveryZones getActiveZones() throws FDLogisticsServiceException {
		
		DeliveryZones response =  getData(null, getEndPoint(ACTIVE_ZONES_API), DeliveryZones.class);
		return response;
	}
	
	@Override
	public DeliveryZones getAllActiveZones() throws FDLogisticsServiceException {
		
		DeliveryZones response =  getData(null, getEndPoint(ALL_ACTIVE_ZONES_API), DeliveryZones.class);

		return response;
		
		}

	@Override
	public FulfillmentInfoResponse getFulfillmentInfo(String companycode,
			String zonecode) throws FDLogisticsServiceException {
		FulfillmentInfoResponse response =  getData(null, getEndPoint(FULFILLMENTINFO_API)+zonecode, FulfillmentInfoResponse.class);
		return response;
	}

	@Override
	public DeliveryZones getZone(DeliveryZoneRequest request) throws FDLogisticsServiceException {

		String inputJson = buildRequest(request);
		DeliveryZones response =  getData(inputJson, getEndPoint(GET_ZONEINFO), DeliveryZones.class);
		return response;
	}

	@Override
	public DeliveryZoneCapacity getZoneCapacity(ZoneRequest request) throws FDLogisticsServiceException {
		
		String inputJson = buildRequest(request);
		DeliveryZoneCapacity response =  getData(inputJson, getEndPoint(GET_ZONE_CAPACITY), DeliveryZoneCapacity.class);
		return response;
	}
	
	@Override
	public DeliveryReservations getReservationById(String rsvId)
			throws FDLogisticsServiceException {

		DeliveryReservations response =  getData(null, getEndPoint(GET_RESERVATION_BYID_API+rsvId), DeliveryReservations.class);
		return response;
	
	}
	
	@Override
	public DeliveryReservations getReservationsByCriteria(SearchRequest request) throws FDLogisticsServiceException {

		String inputJson = buildRequest(request);
		LOGGER.info(inputJson);
		DeliveryReservations response =  getData(inputJson, getEndPoint(RESERVATION_GET_API), DeliveryReservations.class);
		return response;	
	
	}

	@Override
	public Result reservesoTemplate(
			SOReserveTimeslotRequest request) throws FDLogisticsServiceException {
		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(RESERVE_SOTEMPLATE_API), Result.class);
		return response;
	}
	
	
	
	@Override
	public Result deletesoTemplate(String templateId) throws FDLogisticsServiceException {
		Result response =  getData(null, getEndPoint(CANCEL_SOTEMPLATE_API)+templateId, Result.class);
		return response;
	
	}
	
	@Override
	public Result activateSOTemplate(String templateId) throws FDLogisticsServiceException {
		Result response =  getData(null, getEndPoint(ACTIVATE_SOTEMPLATE_API)+templateId, Result.class);
		return response;
	
	}
	
	

	@Override
	public DeliveryReservations reserveTimeslot(ReserveTimeslotRequest request) throws FDLogisticsServiceException {
		String inputJson = buildRequest(request);
		DeliveryReservations response =  getData(inputJson, getEndPoint(RESERVE_TIMESLOT_API), DeliveryReservations.class);
		return response;
	}

	@Override
	public Result confirmReservation(ConfirmReservationRequest request) throws FDLogisticsServiceException {
		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(RESERVATION_CONFIRM_API), Result.class);
		return response;	
	}

	@Override
	public Result updateReservationSize(UpdateReservationRequest request) throws FDLogisticsServiceException {
	
		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(RESERVATION_SIZE_UPDATE_API), Result.class);
		return response;

	}

	@Override
	public Result cancelReservation(CancelReservationRequest request) throws FDLogisticsServiceException {
		
		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(RESERVATION_CANCEL_API), Result.class);
		return response;
	}

	@Override
	public Result uploadOrderSizeFeed(UpdateOrderSizeRequest request) throws FDLogisticsServiceException {

		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(ACTUAL_ORDERSIZE_API), Result.class);
		return response;	
	}

	@Override
	public DeliveryReservations validateReservation(ValidateReservationRequest request) throws FDLogisticsServiceException {
		
		String inputJson = buildRequest(request);
		DeliveryReservations response =  getData(inputJson, getEndPoint(VALIDATE_RESERVATION_API), DeliveryReservations.class);
		return response;
	}

	@Override
	public DeliveryTimeslots getTimeslots(TimeslotRequest request) throws FDLogisticsServiceException {
		
		String inputJson = buildRequest(request);
		DeliveryTimeslots response =  getData(inputJson, getEndPoint(GET_TIMESLOT_API), DeliveryTimeslots.class);
		return response;
	}

	@Override
	public StateCounty lookupStateCountyByZip(String zipcode)
			throws FDLogisticsServiceException {
		
		StateCounty response =  getData(null, getEndPoint(STATECOUNTY_BYZIP_API+zipcode), StateCounty.class);
		return response;
	
	}

	@Override
	public Map<String, Set<StateCounty>> getCountiesByState()
			throws FDLogisticsServiceException {
		
		ListOfStateCounty response =  getData(null, getEndPoint(STATECOUNTY__API), ListOfStateCounty.class);
		List<StateCounty> result = response.getData();
		Map<String, Set<StateCounty>> countiesByState = new HashMap<String, Set<StateCounty>>();
		for(StateCounty sc: result){
			if(!countiesByState.containsKey(sc.getState())){
				countiesByState.put(sc.getState(), new HashSet<StateCounty>());
			}
			countiesByState.get(sc.getState()).add(sc);
		}
	
		return countiesByState;
	}

	@Override
	public Timeslot getTimeslotById(TimeslotIdRequest timeslotIdRequest)
			throws FDLogisticsServiceException {

		String inputJson = buildRequest(timeslotIdRequest);
		Timeslot response =  getData(null, getEndPoint(GET_TIMESLOT_BYID_API+timeslotIdRequest.getTimeslotId()), Timeslot.class);
		return response;
	
	}

	@Override
	public Depots getPickupLocations(PickupLocationsRequest pkupRequest)
			throws FDLogisticsServiceException {

		String inputJson = buildRequest(pkupRequest);
		Depots response =  getData(inputJson, getEndPoint(GET_PICKUPLOCATIONS_API), Depots.class);
		return response;
	}

	@Override
	public Employees getEmployees(String companycode)
			throws FDLogisticsServiceException {

		Employees response =  getData(null, getEndPoint(GET_EMPLOYEES_API), Employees.class);
		return response;
	}

	@Override
	public Result logSessionEvent(SessionEvent event)
			throws FDLogisticsServiceException {
		
		String inputJson = buildRequest(event);
		Result response =  getData(inputJson, getEndPoint(LOG_SESSION_EVENT_API), Result.class);
		return response;
		
	}

	@Override
	public Result logLateIssueOrder(LateIssueOrder lateIssueOrder)
			throws FDLogisticsServiceException {
		
		String inputJson = buildRequest(lateIssueOrder);
		Result response =  getData(inputJson, getEndPoint(LOG_LATEORDER_API), Result.class);
		return response;
		
	}

	@Override
	public DeleteReservationsResponse cancelReservations(ReservationSearchRequest request)
			throws FDLogisticsServiceException {
		
		String inputJson = buildRequest(request);
		DeleteReservationsResponse response =  getData(inputJson, getEndPoint(RESERVATION_BULKCANCEL_API), DeleteReservationsResponse.class);
		return response;
		
	}
	
	@Override
	public Result addSubscription(SubscriptionRequest request) throws FDLogisticsServiceException {
		String inputJson = buildRequest(request);
		Result response = getData(inputJson, getEndPoint(ADD_SUBSCRIPTION_API), Result.class);
		return response;
	}
	
	@Override
	public Result submitOrder(CreateOrderRequest request) throws FDLogisticsServiceException{
		Result response =null;
		try
		{
		String inputJson = buildRequest(request);
		 response = getData(inputJson, getEndPoint(CREATE_FDX_ORDER_API), Result.class);
		}catch(FDLogisticsServiceException e){
			throw new FDLogisticsServiceException(e.getMessage());
			//response.addErrorMessage(e.getMessage());
			//LOsGGER.info("Exception converting {} to ListOfObjects "+response);
			
		}
		return response;
	}
	
	@Override
	public Result modifyOrder(CreateOrderRequest request) throws FDLogisticsServiceException{
		String inputJson = buildRequest(request);
		Result response = getData(inputJson, getEndPoint(MODIFY_FDX_ORDER_API), Result.class);
		return response;
	}

	@Override
	public Result processAddress(Address address)
			throws FDLogisticsServiceException {
		AddressVerificationRequest request = new AddressVerificationRequest();
		request.setAddress(address);
		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(ADD_ADDRESS_API), Result.class);
		return response;	
	}

	@Override
	public ListOfObjects<Address> findSuggestionsForAmbiguousAddress(
			Address address) throws FDLogisticsServiceException {

		AddressVerificationRequest request = new AddressVerificationRequest();
		request.setAddress(address);
		String inputJson = buildRequest(request);
		String response =  getData(inputJson, getEndPoint(ADDRESS_SUGGESTIONS_API), String.class);
		ListOfObjects<Address> info = new ListOfObjects<Address>();
		try{
			info = getMapper().readValue(response, new TypeReference<ListOfObjects<Address>>() { });
		}catch(Exception e){
			LOGGER.info("Exception converting {} to ListOfObjects "+response);
		}
		return info;	
	
	}
	
	@Override
	public DeliveryZones getZoneByGeoLocation(GeoLocation geoLocation) throws FDLogisticsServiceException {

		GeoLocationRequest request = new GeoLocationRequest();
		request.setGeoloc(geoLocation);
		String inputJson = buildRequest(request);
		DeliveryZones zones =  getData(inputJson, getEndPoint(ZONE_BYGEOLOC_API), DeliveryZones.class);
		return zones;	
	
	}

	@Override
	public AddressScrubbingResponse scrubbAddresses(
			AddressScrubbingRequest request) throws FDLogisticsServiceException {
		String inputJson = buildRequest(request);
		AddressScrubbingResponse addressScrubbingResponse = getData(inputJson, getEndPoint(ADDRESS_SCRUBBING_API), AddressScrubbingResponse.class);
		
		return addressScrubbingResponse;
	}
	
	@Override
	public Result isDispatched(String orderId, String companyCode) throws FDLogisticsServiceException{
		Result response = getData(null, isDispatchedEndPoint(STATUS_FDX_ORDER_DISPATCH_API+orderId, companyCode), Result.class);
		return response;
	}

	@Override
	public Result captureDeliveryEventNotification(String carrier, String event)
			throws FDLogisticsServiceException {
		
		Result response = getData(event, getEndPoint(STORE_DELIVERY_EVENT+carrier), Result.class);
		return response;	
	
	}

	@Override
	public ListOfObjects<ShippingDetail> getTrucks() throws FDLogisticsServiceException {
		
		String response =  httpGetData(getEndPoint(GET_TRUCK_DETAILS_API), String.class);
		ListOfObjects<ShippingDetail> info = new ListOfObjects<ShippingDetail>();
		try{
			info = getMapper().readValue(response, new TypeReference<ListOfObjects<ShippingDetail>>() { });
		}catch(Exception e){
			LOGGER.info("Exception converting {} to ListOfObjects while getting truck details "+response);
		}
		return info;	
	}
	
	@Override
	public Result reconfirmReservation(ReconfirmReservationRequest request) throws FDLogisticsServiceException {
		String inputJson = buildRequest(request);
		Result response =  getData(inputJson, getEndPoint(RESERVATION_RECONFIRM_API), Result.class);
		return response;	
	}
	
	@Override
	public RouteStopInfo getRouteStopInfo(String orderId) throws FDLogisticsServiceException {
		RouteStopInfo response =  getData(null, getEndPoint(ROUTE_STOP_INFO+orderId), RouteStopInfo.class);
		return response;
	}
	
	
}
