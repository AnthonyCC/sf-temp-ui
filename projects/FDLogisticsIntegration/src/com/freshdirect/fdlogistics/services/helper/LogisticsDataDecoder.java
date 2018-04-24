package com.freshdirect.fdlogistics.services.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdlogistics.model.FDDeliveryAddressCheckResponse;
import com.freshdirect.fdlogistics.model.FDDeliveryAddressGeocodeResponse;
import com.freshdirect.fdlogistics.model.FDDeliveryAddressVerificationResponse;
import com.freshdirect.fdlogistics.model.FDDeliveryApartmentRange;
import com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel;
import com.freshdirect.fdlogistics.model.FDDeliveryDepotModel;
import com.freshdirect.fdlogistics.model.FDDeliveryETAModel;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDDeliveryTimeslots;
import com.freshdirect.fdlogistics.model.FDDeliveryZipInfo;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdlogistics.model.FDTimeslotList;
import com.freshdirect.fdlogistics.model.FDZoneCutoffInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.controller.data.AddressExceptionData;
import com.freshdirect.logistics.controller.data.ApartmentRange;
import com.freshdirect.logistics.controller.data.DeliveryZipInfo;
import com.freshdirect.logistics.controller.data.Depots;
import com.freshdirect.logistics.controller.data.PickupData;
import com.freshdirect.logistics.controller.data.PickupLocationData;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.response.AddressCheckResponse;
import com.freshdirect.logistics.controller.data.response.AddressExceptionResponse;
import com.freshdirect.logistics.controller.data.response.AddressVerificationResponse;
import com.freshdirect.logistics.controller.data.response.AirclicCartonScanHistory;
import com.freshdirect.logistics.controller.data.response.AirclicTextMessages;
import com.freshdirect.logistics.controller.data.response.DeliveryETA;
import com.freshdirect.logistics.controller.data.response.DeliveryReservations;
import com.freshdirect.logistics.controller.data.response.DeliveryServices;
import com.freshdirect.logistics.controller.data.response.DeliverySignature;
import com.freshdirect.logistics.controller.data.response.DeliverySummary;
import com.freshdirect.logistics.controller.data.response.DeliveryTimeslots;
import com.freshdirect.logistics.controller.data.response.DeliveryZips;
import com.freshdirect.logistics.controller.data.response.DeliveryZone;
import com.freshdirect.logistics.controller.data.response.DeliveryZoneCapacity;
import com.freshdirect.logistics.controller.data.response.DeliveryZoneCutoff;
import com.freshdirect.logistics.controller.data.response.DeliveryZoneCutoffs;
import com.freshdirect.logistics.controller.data.response.DeliveryZones;
import com.freshdirect.logistics.controller.data.response.ListOfObjects;
import com.freshdirect.logistics.controller.data.response.Reservation;
import com.freshdirect.logistics.controller.data.response.RouteNextel;
import com.freshdirect.logistics.controller.data.response.RouteNextelResponse;
import com.freshdirect.logistics.controller.data.response.TextMessage;
import com.freshdirect.logistics.controller.data.response.Timeslot;
import com.freshdirect.logistics.controller.data.response.TimeslotList;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.model.AirclicCartonInfo;
import com.freshdirect.logistics.delivery.model.AirclicCartonScanDetails;
import com.freshdirect.logistics.delivery.model.AirclicTextMessage;
import com.freshdirect.logistics.delivery.model.DlvZoneCapacityInfo;
import com.freshdirect.logistics.delivery.model.DlvZoneDescriptor;
import com.freshdirect.logistics.delivery.model.DlvZoneModel;
import com.freshdirect.logistics.delivery.model.EnumAddressExceptionReason;
import com.freshdirect.logistics.delivery.model.EnumAddressType;
import com.freshdirect.logistics.delivery.model.EnumAddressVerificationResult;
import com.freshdirect.logistics.delivery.model.EnumApplicationException;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.logistics.delivery.model.EnumReservationClass;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.EnumZipCheckResponses;
import com.freshdirect.logistics.delivery.model.ExceptionAddress;
import com.freshdirect.sms.CrmSmsDisplayInfo;

public class LogisticsDataDecoder {


	private static AddressModel decodeAddress(Address model) {
		
		AddressModel address = new AddressModel(model.getAddress1(), model.getApartment(), model.getCity(), model.getState(), model.getZipCode());
		address.setAddress2(model.getAddress2());
		address.setServiceType(EnumServiceType.getEnum(model.getServiceType()));
		AddressInfo addressInfo = new AddressInfo(model.getZoneCode(), model.getLongitude(), 
				model.getLatitude(), model.getScrubbedStreet(), 
				com.freshdirect.common.address.EnumAddressType.getEnum(model.getAddressType()), 
				model.getCounty(), model.getBuildingId(), model.getLocationId());
		addressInfo.setSsScrubbedAddress(model.getSsScrubbedAddress());
		address.setAddressInfo(addressInfo);
		
		if(StringUtils.isNotEmpty(model.getId())) address.setId(model.getId());
		return address;
		
	}
	
	public static void decodeResult(Result result) throws FDResourceException{
		if(result.getStatus() == null || (result.getStatus().equals("FAILED") && result.getErrorCode() == EnumApplicationException.ResourceException.getValue())){
			throw new FDResourceException(result.getErrors().toString());
		}
	}
	public static List<DlvZoneModel> decodeDeliveryZones(DeliveryZones zones) throws FDResourceException {
		decodeResult(zones);
		List<DlvZoneModel> result = new ArrayList<DlvZoneModel>();
		if(zones!=null && zones.getDeliveryZones()!=null){
			for (DeliveryZone zone : zones.getDeliveryZones()) {
				result.add(new DlvZoneModel(zone.getName(), zone.getZoneCode(), zone.getCompanyCode()));
			}
		}
		return result;
	}

	public static List<String> decodeDeliveryZoneCodes(DeliveryZones zones) throws FDResourceException {
		decodeResult(zones);
		List<String> result = new ArrayList<String>();
		if(zones!=null && zones.getDeliveryZones()!=null){		
			for (DeliveryZone zone : zones.getDeliveryZones()) {
				result.add(zone.getZoneCode());
			}
		}
		return result;
	}

	public static List<FDReservation> decodeReservations(
			DeliveryReservations d) throws FDResourceException {
		decodeResult(d);
		if(EnumApplicationException.FinderException.getValue() == d.getErrorCode()){
			return null;
		}
		List<FDReservation> fdReservations = new ArrayList<FDReservation>();
		if(d.getReservations()!=null){
			for (Reservation reservation : d.getReservations()) {
				fdReservations.add(decodeReservation(reservation));
			}
		}
		return fdReservations;
	}

	private static FDReservation decodeReservation(Reservation reservation) throws FDResourceException {
		
		return new FDReservation(new PrimaryKey(reservation
				.getId()), decodeTimeslot(reservation.getTimeslot()), reservation
				.getExpirationDateTime(), EnumReservationType
				.getEnum(reservation.getType()), reservation
				.getCustomerId(), reservation.getAddress().getId(), new ErpAddressModel(decodeAddress(reservation.getAddress())), reservation.isChefsTable(), reservation.getOrderId(), reservation
						.getStatusCode(), EnumReservationClass.getEnum(reservation.getRsvClass()), reservation.isSteeringDiscount(), 
						EnumRegionServiceType.getEnum(reservation.getRegionServiceType()), reservation.getDeliveryFeeTier());
	}

	public static List<FDTimeslot> decodeTimeslots(List<Timeslot> timeslots) {
		List<FDTimeslot> fdtList = new ArrayList<FDTimeslot>();
		if(timeslots!=null){
			for(Timeslot timeslot: timeslots){
				fdtList.add(decodeTimeslot(timeslot));
			}
		}
		return fdtList;
	}
	public static FDTimeslot decodeTimeslot(Timeslot timeslot) {
		
		FDTimeslot t = 
				new FDTimeslot(timeslot.getId(), timeslot.getDeliveryDate(), new TimeOfDay(timeslot.getStartTime()), new TimeOfDay(timeslot.getEndTime()), 
				new TimeOfDay(timeslot.getCutoffTime()), timeslot.getPremiumCutoffTime()!=null ? new TimeOfDay(timeslot.getPremiumCutoffTime()): null,
				timeslot.getCutoffDateTime(), timeslot.getPremiumCutoffDateTime(),
				timeslot.getZoneId(), timeslot.getZoneCode(), timeslot.isNormalAvailCapacity(), timeslot.isAvailCTCapacity(), timeslot.getGeoRestricted(), 
				timeslot.isTimeslotRestricted(), timeslot.isTimeslotRemoved(), timeslot.getStoreFrontAvailable(), timeslot.isUnavailable(), timeslot.getEcoFriendly(), 
				timeslot.isSoldOut(), timeslot.isDepot(), timeslot.isSameDaySlot(), timeslot.isFdxSlot(),
				timeslot.getTotalAvailable(), timeslot.getBaseAvailable(), timeslot.getChefsTableAvailble(), timeslot.isRadius(), timeslot.getTravelZone(), 
				timeslot.getModX(), timeslot.getModY(), timeslot.getAdditionalDistance(), EnumRegionServiceType.getEnum(timeslot.getRegionServiceType()),
				timeslot.getSoFirstDeliveryDate(),timeslot.getOriginalCutoffDateTime(), timeslot.getCapacityUtilizationPercentage());
		return t;
	}
	
	public static FDDeliveryAddressVerificationResponse decodeAddressVerificationResponse(
			AddressModel addressModel, AddressVerificationResponse response) throws FDResourceException,
			FDInvalidAddressException {

		if (response.getStatus() == null
				|| response.getStatus().equalsIgnoreCase("FAILED")) {
			throw new FDInvalidAddressException("Invalid Address entered");
		}
		decodeAddressExceptions(response);
		FDDeliveryAddressVerificationResponse result = 
				new FDDeliveryAddressVerificationResponse(
						copyScrubbedAddress(addressModel, decodeAddress(response.getAddress())),
						decodeActionResult(response),
						EnumAddressVerificationResult.getEnum(response.getScrubResult()),
						decodeDeliveryServices(response.getServices()),
						response.getGeocodeResult());
		return result;
		
		
	}

	private static ActionResult decodeActionResult(
			Result response) {
		ActionResult result = new ActionResult();
		if(response.getErrors()!=null){
			for(Entry<String, String>  entry : response.getErrors().entrySet())
				result.addError(true, entry.getKey(), entry.getValue());
		}
		if(response.getWarnings()!=null){
			for(Entry<String, String>  entry : response.getWarnings().entrySet())
				result.addWarning(true, entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static FDDeliveryAddressGeocodeResponse decodeAddressGeocodeResponse(
			AddressModel addressModel, AddressVerificationResponse response) throws FDResourceException, FDInvalidAddressException {
		decodeAddressExceptions(response);
		FDDeliveryAddressGeocodeResponse result = new FDDeliveryAddressGeocodeResponse(copyScrubbedAddress(addressModel, decodeAddress(response.getAddress())), response.getGeocodeResult());
		return result;
	}

	public static FDDeliveryZoneInfo decodeDeliveryZoneInfo(
			DeliveryZones zones) throws FDResourceException, FDInvalidAddressException {
		decodeAddressExceptions(zones);
		if(zones!=null && zones.getDeliveryZones()!=null){
			Iterator<DeliveryZone> it = zones.getDeliveryZones().iterator();
			if(it.hasNext()){
				DeliveryZone zone = it.next();
				FDDeliveryZoneInfo model = new FDDeliveryZoneInfo(zone.getZoneCode(), zone.getZoneId(), 
						zone.getRegionId(), EnumZipCheckResponses.getEnum(zone.getResponse()), zone.isUnattended(), 
						zone.isCosEnabled(), zone.isCtActive(), EnumRegionServiceType.getEnum(zone.getRegionServiceType()), 
						zone.getFulfillmentInfo());
				return model;
			}
		}
		return null;
	}

	public static List<FDZoneCutoffInfo> decodeDeliveryZoneCutoffs(
			DeliveryZoneCutoffs response) throws FDResourceException {
		decodeResult(response);
		List<FDZoneCutoffInfo> cutoffs = new ArrayList<FDZoneCutoffInfo>();
		if(response!=null && response.getCutoffs()!=null){
			for(DeliveryZoneCutoff cutoff : response.getCutoffs()){
				cutoffs.add(decodeDeliveryZoneCutoff(cutoff));
			}
		}
		return cutoffs;
	}

	private static FDZoneCutoffInfo decodeDeliveryZoneCutoff(
			DeliveryZoneCutoff cutoff) {
		FDZoneCutoffInfo result = null;
		if(cutoff!=null){
	
			result = new FDZoneCutoffInfo(cutoff.getZoneCode(), cutoff.getDay(), 
				new TimeOfDay(cutoff.getStartTime()), 
				new TimeOfDay(cutoff.getEndTime()), new TimeOfDay(cutoff.getCutoffTime()));
		}
		return result;
	}

	public static DlvZoneCapacityInfo decodeDeliveryZoneCapacity(
			DeliveryZoneCapacity response) throws FDResourceException {
		decodeResult(response);
		DlvZoneCapacityInfo info = null;
		if(response!=null){
			 info = new DlvZoneCapacityInfo(response.getZoneCode(), 
				response.getTotalCapacity(), response.getRemainingCapacity());
		}
		return info;
	}

	public static List<FDDeliveryZipInfo> decodeDeliveryZips(DeliveryZips response) throws FDResourceException {
		decodeResult(response);
		List<FDDeliveryZipInfo> zips = new ArrayList<FDDeliveryZipInfo>();
		if(response!=null && response.getDeliveryZips()!=null){
			for(DeliveryZipInfo zip : response.getDeliveryZips()){
				zips.add(decodeDeliveryZip(zip));
			}
		}
		return zips;
	
	}

	private static FDDeliveryZipInfo decodeDeliveryZip(DeliveryZipInfo zip) {
		FDDeliveryZipInfo info = null;
		if(zip!=null)
			info = new FDDeliveryZipInfo(zip.getZipCode(), zip.getStartDate(), zip.getCoverage());
		return info;
	}

	public static FDDeliveryServiceSelectionResult decodeDeliveryServices(
			DeliveryServices response) throws FDResourceException, FDInvalidAddressException {
		decodeResult(response);
		FDDeliveryServiceSelectionResult result = new FDDeliveryServiceSelectionResult();
		if(response!=null){
			decodeAddressExceptions(response);
			result.setEbtAccepted(response.isEbtAccepted());
			if(response.getServiceStatus()!=null){
				for(Entry<String, String> entry: response.getServiceStatus().entrySet()){
					result.addServiceStatus(EnumServiceType.getEnum(entry.getKey()), EnumDeliveryStatus.getEnum(entry.getValue()));
				}
			}
		}
		return result;
	}

	public static FDDeliveryTimeslots decodeDeliveryTimeslots(
			DeliveryTimeslots t) throws FDResourceException {
		decodeResult(t);
		FDDeliveryTimeslots result = new FDDeliveryTimeslots();

		
			result.setTimeslotList(decodeTimeslotList(t.getTimeslotList()));
			result.setCtActive(t.isCtActive());
			result.setHasCapacity(t.isHasCapacity());
			result.setGeoRestrictionmessages(t.getGeoRestrictionmessages());
			result.setComments(t.getComments());
			result.setEcoFriendlySlots(t.getEcoFriendlySlots());
			result.setNeighbourhoodSlots(t.getNeighbourhoodSlots());
			result.setTotalSlots(t.getTotalSlots());
			result.setSoldoutSlots(t.getSoldoutSlots());
			result.setPreselectedTimeslotId(t.getPreselectedTimeslotId());
			result.setZones(decodeDeliveryZones(t.getZones()));
			result.setSameDayCutoff(t.getSameDayCutoff());
			result.setCtSlots(t.getCtSlots());
		
		return result;
	}
	
	private static Map<String, DlvZoneModel> decodeDeliveryZones(
			Map<String, DeliveryZone> zonesMap) {
		Map<String, DlvZoneModel> result = new HashMap<String, DlvZoneModel>();
		if(zonesMap!=null && zonesMap.entrySet()!=null){
			for(Entry<String, DeliveryZone> entry : zonesMap.entrySet()){
				DeliveryZone value = entry.getValue();
				result.put(entry.getKey(), new DlvZoneModel(value.getZoneId(), value.getName(), null, false, false, 0,0, 
						new DlvZoneDescriptor(value.getZoneCode(), value.isUnattended())));
			}
		}
		return result;
	}

	private static List<FDTimeslotList> decodeTimeslotList(List<TimeslotList> timeslotList) {
		List<FDTimeslotList> result = new ArrayList<FDTimeslotList>();
		if(timeslotList!=null){
			for(TimeslotList tsList: timeslotList){
				FDTimeslotList model = new FDTimeslotList();
				model.setTimeslots(decodeTimeslots(tsList.getTimeslots()));
				model.setResponseTime(tsList.getResponseTime());
				model.setError(tsList.getError());	
				model.setRange(new DateRange(tsList.getRange().getStartDate(), tsList.getRange().getEndDate()));
				model.setEventPk(tsList.getEventPk());
				result.add(model);
			}
		}
		return result;
		
	}

	public static FDDeliveryAddressCheckResponse decodeAddressCheckResponse(
			AddressModel addressModel, AddressCheckResponse response) throws FDResourceException, FDInvalidAddressException  {
		decodeAddressExceptions(response);
		FDDeliveryAddressCheckResponse result = new FDDeliveryAddressCheckResponse();
		if(response.getAddress() != null) {
			result.setAddress(copyScrubbedAddress(addressModel, decodeAddress(response.getAddress()))); // this is required to retain some of the properties in ErpAddressModel.
		}
		result.setAddressOk(response.getAddressOk());
		result.setAptRanges(decodeDeliveryApartmentRange(response.getAptRanges()));
		result.setAvailServices(decodeServiceTypes(response.getAvailServices()));
		result.setCounty(response.getCounty());
		result.setDeliveryStatus(response.getDeliveryStatus());
		result.setGeocodeOk(response.getGeocodeOk());
		result.setSuggestions(decodeAddressList(response.getSuggestions()));
		result.setZoneInfo(decodeDeliveryZoneInfo(response.getZoneInfo()));
		result.setVerifyResult(EnumAddressVerificationResult.getEnum(response.getScrubResult()));
		result.setGeocodeResult(response.getGeocodeResult());
		return result;
	}

	public static AddressModel copyScrubbedAddress(AddressModel address, AddressModel scrubbedAddress){
		address.setAddress1(scrubbedAddress.getAddress1());
		address.setAddress2(scrubbedAddress.getAddress2());
		address.setApartment(scrubbedAddress.getApartment());
		address.setCity(scrubbedAddress.getCity());
		address.setState(scrubbedAddress.getState());
		address.setZipCode(scrubbedAddress.getZipCode());
		address.setAddressInfo(scrubbedAddress.getAddressInfo());
		//address.setServiceType(scrubbedAddress.getServiceType());
		return address;
	}
	private static List<FDDeliveryZoneInfo> decodeDeliveryZoneInfo(
			List<DeliveryZone> zones) {
		List<FDDeliveryZoneInfo> result = new ArrayList<FDDeliveryZoneInfo>();
		if(zones!=null){
			for(DeliveryZone zone: zones){
				result.add(new FDDeliveryZoneInfo(zone.getZoneCode(), zone.getZoneId(), 
					zone.getRegionId(), EnumZipCheckResponses.getEnum(zone.getResponse()), 
					zone.isUnattended(), zone.isCosEnabled(), zone.isCtActive(), 
					EnumRegionServiceType.getEnum(zone.getRegionServiceType()), zone.getFulfillmentInfo()));
			}
		}
		return result;
	}

	public static List<AddressModel> decodeAddressList(
			List<Address> suggestions) {
		List<AddressModel> result = new ArrayList<AddressModel>();
		
		if(suggestions!=null){
			for(Address suggestion : suggestions){
				result.add(decodeAddress(suggestion));
			}
		}
		return result;
	}

	private static Set<EnumServiceType> decodeServiceTypes(
			Set<com.freshdirect.logistics.delivery.model.EnumServiceType> availServices) {
		Set<EnumServiceType> result = new HashSet<EnumServiceType>();
		
		if(availServices!=null){
			for(com.freshdirect.logistics.delivery.model.EnumServiceType availService: availServices){
				result.add(EnumServiceType.getEnum(availService.getName()));
			}
		}
		return result;
	}

	private static List<FDDeliveryApartmentRange> decodeDeliveryApartmentRange(
			List<ApartmentRange> aptRanges) {
		List<FDDeliveryApartmentRange> result = new ArrayList<FDDeliveryApartmentRange>();
		if(aptRanges!=null){
			for(ApartmentRange range : aptRanges){
				result.add(new FDDeliveryApartmentRange(range.getLow(), range.getHigh(), range.getAddressType()));
			}
		}
		return result;
	}

	public static FDDeliveryETAModel decodeDeliveryETA(DeliveryETA eta) throws FDResourceException {
		decodeResult(eta);
		if(EnumApplicationException.FinderException.getValue() == eta.getErrorCode()){
				return null;
		}
		return new FDDeliveryETAModel(eta.getOrderId(), eta.getStartTime(), eta.getEndTime(), 
				eta.isEmailETAenabled(), eta.isManifestETAenabled(), eta.isSmsETAenabled());
		
	}
	

	public static List<ExceptionAddress> decodeAddressExceptions(AddressExceptionResponse response) throws FDResourceException {
		decodeResult(response);
		List<AddressExceptionData> list = response.getExceptionAddress();
		List<ExceptionAddress> exceptions = new ArrayList<ExceptionAddress>();
		
		if (list != null) {
			for(AddressExceptionData address : list){
				ExceptionAddress exceptionAddress = new ExceptionAddress(address.getId(), address.getStreetAddress(), address.getAptNumLow(), address.getAptNumHigh(),
						address.getZip(), EnumAddressType.getEnum(address.getAddressType()),
						EnumAddressExceptionReason.getEnum(address.getReason()), address.getCounty(), address.getState(),
						address.getUserId(), address.getCity(), address.getScrubbedAddress(), address.getLatitude(),
						address.getLongitude());
				exceptionAddress.setId(address.getId());
				exceptions.add(exceptionAddress);
			}
		}
		return exceptions;
	}

	private static void decodeAddressExceptions(Result result) throws FDInvalidAddressException, FDResourceException {
		
		if(result.getStatus() == null || "FAILED".equals(result.getStatus())){
	
			if(EnumApplicationException.GeocodingException.getValue() == result.getErrorCode()){
				throw new FDInvalidAddressException.GeocodingException("Unable to geocode this address");
			}else if(EnumApplicationException.InvalidAddressException.getValue() == result.getErrorCode()){
				throw new FDInvalidAddressException("Provided address is invalid");
			}else if(EnumApplicationException.ResourceException.getValue() == result.getErrorCode()){
				throw new FDResourceException("Exception while verifying the address");
			}
			
		}
	}

	public static List<FDDeliveryDepotModel> decodePickupLocations(
			Depots pickups) throws FDResourceException {
		decodeResult(pickups);
		List<FDDeliveryDepotModel> depots = new ArrayList<FDDeliveryDepotModel>();
		for(PickupData p: pickups.getPickupDepots()){
			depots.add(new FDDeliveryDepotModel(p.getName(), p.getRegistrationCode(), p.getRegionId(), 
					decodePickupLocationAddresses(p.getDepotLocations()), p.getDepotCode(), 
					p.getCustServiceEmail(), p.isRequireEmployeeId(), p.isPickup(), p.isCorporateDepot(), p.isDeactivated()));
		}
		return depots;
	}

	private static List<FDDeliveryDepotLocationModel> decodePickupLocationAddresses(
			List<PickupLocationData> depotLocations) {
		List<FDDeliveryDepotLocationModel> result = new ArrayList<FDDeliveryDepotLocationModel>();
		for(PickupLocationData l: depotLocations){
			result.add(new FDDeliveryDepotLocationModel(new PrimaryKey(l.getAddress().getId()),
					l.getStartDate(), l.getEndDate(), l.getFacility(), decodeAddress(l.getAddress()),
							l.getInstructions(), l.getZoneCode(), l.getDeliveryChargeWaived()));
		}
		return result;
	}

	public static FDReservation decodeValidateReservation(DeliveryReservations response) throws FDResourceException {
		decodeResult(response);
		if(response.getReservations()!=null 
				&& !response.getReservations().isEmpty()
				&& response.getReservations().get(0) !=null)
			return decodeReservation(response.getReservations().get(0));
		return null;
	}

	public static com.freshdirect.logistics.delivery.model.DeliverySummary decodDeliverySummary(DeliverySummary response) throws FDResourceException {
		decodeResult(response);
		com.freshdirect.logistics.delivery.model.DeliverySummary summary = new com.freshdirect.logistics.delivery.model.DeliverySummary(response.getDeliveryAttempts(), response.getDeliveryStatus(),
				response.isOrderDelivered(), response.getEstimatedDlvTime(), decodeTextMessages(response.getMessages()), response.getCartonExceptions(), 
				response.getDeliveryETAStart(), response.getDeliveryETAEnd(),response.getCustomerContactStatus());
		return summary;
		
	}

	public static List<AirclicTextMessage> decodeTextMessages(AirclicTextMessages airclicTextMessages) throws FDResourceException {
		decodeResult(airclicTextMessages);
		return decodeTextMessages(airclicTextMessages.getAirclicTextMessages());
	}

	private static List<AirclicTextMessage> decodeTextMessages(List<TextMessage> airclicTextMessages) throws FDResourceException {
		List<AirclicTextMessage> list = new ArrayList<AirclicTextMessage>();
		if(airclicTextMessages!=null){
			for(TextMessage message: airclicTextMessages){
				AirclicTextMessage msg = new AirclicTextMessage(message.getDeliveryDate(), message.getRoute(), message.getStop(), message.getMessage(), 
						message.getSource(), message.getSender(), message.getOrderId(), message.getCustomerId());
				msg.setCreateDate(message.getCreateDate());
				msg.setSentToAirclic(message.getSentToAirclic());
				list.add(msg);
				
			}
		}
		return list;
	}
	
	public static List<com.freshdirect.logistics.delivery.model.RouteNextel> decodeRouteNextels(
			RouteNextelResponse routeNextelResponse) throws FDResourceException {
		decodeResult(routeNextelResponse);
		List<com.freshdirect.logistics.delivery.model.RouteNextel> result = new ArrayList<com.freshdirect.logistics.delivery.model.RouteNextel>();
		
		if(routeNextelResponse.getRouteNextels()!=null){
			for(RouteNextel r : routeNextelResponse.getRouteNextels()){
				result.add(new com.freshdirect.logistics.delivery.model.RouteNextel(r.getNextel(), r.getEmployee(), r.getEmpId()));
			}
		}
		return result;
	}

	public static List<AirclicCartonInfo> decodeAirclicCartonInfo(AirclicCartonScanHistory airclicCartonScanHistory) throws FDResourceException {
		decodeResult(airclicCartonScanHistory);
		List<AirclicCartonInfo> result = new ArrayList<AirclicCartonInfo>();
		
		if(airclicCartonScanHistory.getAirclicCartonInfo()!=null){
			for(com.freshdirect.logistics.controller.data.response.AirclicCartonInfo c : airclicCartonScanHistory.getAirclicCartonInfo()){
				result.add(new AirclicCartonInfo(c.getOrderNumber(), c.getCartonNumber(), c.getCartonType(), decodeAirclicCartonDetails(c.getDetails())));
			}
		}
		return result;
	}

	private static List<AirclicCartonScanDetails> decodeAirclicCartonDetails(
			List<com.freshdirect.logistics.controller.data.response.AirclicCartonScanDetails> cartonDetail) {
		List<AirclicCartonScanDetails> result = new ArrayList<AirclicCartonScanDetails>();
		for(com.freshdirect.logistics.controller.data.response.AirclicCartonScanDetails c : cartonDetail){
			result.add(new AirclicCartonScanDetails(c.getScanDate(), c.getAction(), c.getCartonStatus(), c.getEmployee(), c.getRoute(),
					c.getStop(), c.getNextel(), c.getUserId(), c.getDeliveredTo(), c.getReturnReason(), c.getMotDriverName()));
		}
		return result;
	}

	public static com.freshdirect.logistics.delivery.model.DeliverySignature decodeDeliverySignature(
			DeliverySignature s) throws FDResourceException {
		decodeResult(s);
		return new com.freshdirect.logistics.delivery.model.DeliverySignature(s.getOrderNo(), s.getDeliveredTo(), 
				s.getRecipient(), s.isContainsAlcohol(), s.getSignatureTime());
	}

	public static List<CrmSmsDisplayInfo> decodeSmsInfo(
			ListOfObjects<com.freshdirect.logistics.delivery.sms.model.CrmSmsDisplayInfo> listOfObjects) throws FDResourceException {
		decodeResult(listOfObjects);
		List<CrmSmsDisplayInfo> infoList = new ArrayList<CrmSmsDisplayInfo>();
		if(listOfObjects.getData()!=null){
			for(com.freshdirect.logistics.delivery.sms.model.CrmSmsDisplayInfo object : listOfObjects.getData()){
				CrmSmsDisplayInfo info = new CrmSmsDisplayInfo();
				info.setAlertType(object.getAlertType());
				info.setMessage(object.getMessage());
				info.setMobileNumber(object.getMobileNumber());
				info.setStatus(object.getStatus());
				info.setTimeSent(object.getTimeSent());
				infoList.add(info);
			}
		}
		return infoList;
	}

}
