package com.freshdirect.fdlogistics.services;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.logistics.analytics.model.LateIssueOrder;
import com.freshdirect.logistics.analytics.model.SessionEvent;
import com.freshdirect.logistics.controller.data.Depots;
import com.freshdirect.logistics.controller.data.OrderSizeRequest;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.request.AddressExceptionRequest;
import com.freshdirect.logistics.controller.data.request.CancelReservationRequest;
import com.freshdirect.logistics.controller.data.request.ConfirmReservationRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZipCodeRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZipRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryZoneRequest;
import com.freshdirect.logistics.controller.data.request.PickupLocationsRequest;
import com.freshdirect.logistics.controller.data.request.ReserveTimeslotRequest;
import com.freshdirect.logistics.controller.data.request.SOReserveTimeslotRequest;
import com.freshdirect.logistics.controller.data.request.SearchRequest;
import com.freshdirect.logistics.controller.data.request.SubscriptionRequest;
import com.freshdirect.logistics.controller.data.request.TimeslotIdRequest;
import com.freshdirect.logistics.controller.data.request.TimeslotRequest;
import com.freshdirect.logistics.controller.data.request.UpdateOrderSizeRequest;
import com.freshdirect.logistics.controller.data.request.UpdateReservationRequest;
import com.freshdirect.logistics.controller.data.request.ValidateReservationRequest;
import com.freshdirect.logistics.controller.data.request.ZoneRequest;
import com.freshdirect.logistics.controller.data.response.AddressCheckResponse;
import com.freshdirect.logistics.controller.data.response.AddressExceptionResponse;
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
import com.freshdirect.logistics.controller.data.response.Timeslot;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.model.GeoLocation;
import com.freshdirect.logistics.fdstore.StateCounty;
import com.freshdirect.logistics.fdx.controller.data.request.CreateOrderRequest;

public interface ILogisticsService {

	AddressVerificationResponse verifyAddress(Address address) throws FDInvalidAddressException, FDLogisticsServiceException;
	
	Result addAddressException(AddressExceptionRequest request) throws FDLogisticsServiceException;
	AddressExceptionResponse searchAddressException(AddressExceptionRequest request) throws FDLogisticsServiceException;
	Result deleteAddressException(String id) throws FDLogisticsServiceException;
	
	Result addGeocodeException(AddressExceptionRequest request) throws FDLogisticsServiceException;
	AddressExceptionResponse searchGeocodeException(AddressExceptionRequest request) throws FDLogisticsServiceException;
	Result deleteGeocodeAddress(AddressExceptionRequest request) throws FDLogisticsServiceException;
	
	AddressCheckResponse checkAddress(Address address) throws FDLogisticsServiceException;

	DeliveryETA getDeliveryETA(String orderId) throws FDLogisticsServiceException;
	
	DeliveryServices getDeliveryServices(Address address) throws FDLogisticsServiceException;
	DeliveryServices getDeliveryServices(DeliveryZipCodeRequest request) throws FDLogisticsServiceException;
	DeliveryZips getDeliverableZipCodes(DeliveryZipRequest request) throws FDLogisticsServiceException;

	DeliveryZoneCutoffs getZoneCutoffs(ZoneRequest request) throws FDLogisticsServiceException;
	ListOfDates getDateCutoffs(Date requestedDate) throws FDLogisticsServiceException;
	DeliveryZones getActiveZones() throws FDLogisticsServiceException;
	FulfillmentInfoResponse getFulfillmentInfo(String companycode, String zonecode) throws FDLogisticsServiceException;
	DeliveryZones getZone(DeliveryZoneRequest zoneRequest) throws FDLogisticsServiceException;
	DeliveryZoneCapacity getZoneCapacity(ZoneRequest zoneRequest) throws FDLogisticsServiceException;
	
	DeliveryReservations getReservationsByCriteria(SearchRequest request) throws FDLogisticsServiceException;
	
	Result deletesoTemplate(String templateId) throws FDLogisticsServiceException;
	
	DeliveryReservations reserveTimeslot(ReserveTimeslotRequest reservationRequest) throws FDLogisticsServiceException;
	
	Result confirmReservation(ConfirmReservationRequest reservationRequest) throws FDLogisticsServiceException;
	
	Result updateReservationSize(UpdateReservationRequest request) throws FDLogisticsServiceException;
	
	Result cancelReservation(CancelReservationRequest reservationRequest) throws FDLogisticsServiceException;
	
	DeliveryReservations validateReservation(ValidateReservationRequest reservationRequest) throws FDLogisticsServiceException;
	
	DeliveryTimeslots getTimeslots(TimeslotRequest timeslotRequest) throws FDInvalidAddressException, FDLogisticsServiceException;
	
	Timeslot getTimeslotById(TimeslotIdRequest timeslotIdRequest) throws FDLogisticsServiceException;
	
	
	StateCounty lookupStateCountyByZip(String zipcode) throws FDLogisticsServiceException;

	public Map<String, Set<StateCounty>> getCountiesByState() throws FDLogisticsServiceException;
	
	public Depots getPickupLocations(PickupLocationsRequest pkupRequest) throws FDLogisticsServiceException;

	public Employees getEmployees(String companycode) throws FDLogisticsServiceException;

	Result logSessionEvent(SessionEvent event) throws FDLogisticsServiceException;
	Result logLateIssueOrder(LateIssueOrder lateIssueOrder) throws FDLogisticsServiceException;

	DeleteReservationsResponse cancelReservations(SearchRequest request) throws FDLogisticsServiceException;
	
	Result addSubscription(SubscriptionRequest request) throws FDLogisticsServiceException;

	Result reservesoTemplate(String companycode, SOReserveTimeslotRequest request) throws FDLogisticsServiceException;	
	
	Result submitOrder(CreateOrderRequest request) throws FDLogisticsServiceException;

	Result modifyOrder(CreateOrderRequest request)throws FDLogisticsServiceException;

	Result addApartment(Address address)throws FDLogisticsServiceException;

	Result processAddress(Address encodeAddress)throws FDLogisticsServiceException;

	ListOfObjects<Address> findSuggestionsForAmbiguousAddress(
			Address encodeAddress)throws FDLogisticsServiceException;

	DeliveryZones getZoneByGeoLocation(GeoLocation geoLocation)
			throws FDLogisticsServiceException;

	AddressVerificationResponse geocodeAddress(Address address)
			throws FDLogisticsServiceException;

	Result uploadOrderSizeFeed(UpdateOrderSizeRequest request)throws FDLogisticsServiceException;
}
