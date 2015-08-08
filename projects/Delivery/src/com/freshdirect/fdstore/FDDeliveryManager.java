package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.common.pricing.MunicipalityInfoWrapper;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.announcement.SiteAnnouncement;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.ejb.DlvRestrictionManagerHome;
import com.freshdirect.delivery.ejb.DlvRestrictionManagerSB;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.erp.EnumStateCodes;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.model.DuplicateKeyException;
import com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason;
import com.freshdirect.fdlogistics.model.FDDeliveryAddressCheckResponse;
import com.freshdirect.fdlogistics.model.FDDeliveryAddressGeocodeResponse;
import com.freshdirect.fdlogistics.model.FDDeliveryAddressVerificationResponse;
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
import com.freshdirect.fdlogistics.model.FDZoneCutoffInfo;
import com.freshdirect.fdlogistics.services.IAirclicService;
import com.freshdirect.fdlogistics.services.ILogisticsService;
import com.freshdirect.fdlogistics.services.helper.LogisticsDataDecoder;
import com.freshdirect.fdlogistics.services.helper.LogisticsDataEncoder;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.TimedLruCache;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.analytics.model.SessionEvent;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.controller.data.Depots;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.request.SearchRequest;
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
import com.freshdirect.logistics.controller.data.response.ListOfDates;
import com.freshdirect.logistics.controller.data.response.ListOfObjects;
import com.freshdirect.logistics.controller.data.response.Timeslot;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;
import com.freshdirect.logistics.delivery.model.DlvZoneCapacityInfo;
import com.freshdirect.logistics.delivery.model.DlvZoneModel;
import com.freshdirect.logistics.delivery.model.EnumApplicationException;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.ExceptionAddress;
import com.freshdirect.logistics.delivery.model.GeoLocation;
import com.freshdirect.logistics.delivery.model.OrderContext;
import com.freshdirect.logistics.fdstore.StateCounty;

/**
 * @version $Revision:23$
 * @author $Author:Viktor Szathmary$
 */

public class FDDeliveryManager {

	private final ServiceLocator serviceLocator;
	private static FDDeliveryManager dlvManager = null;

	/** cache zipCode -> FDDeliveryServiceSelectionResult */
	private static TimedLruCache<String, FDDeliveryServiceSelectionResult> zipCheckCache = new TimedLruCache<String,FDDeliveryServiceSelectionResult>(200, 60 * 60 * 1000);
	
	/** 1 hr cache state -> List of state county */
	private static TimedLruCache<String,Set<StateCounty>>  countiesByState = new TimedLruCache<String,Set<StateCounty>>(50, 60 * 60 * 1000);

	/** 1 hr cache zoneCode -> List of cutoff-times for next day (FDZoneCutoffInfo)*/
	private static TimedLruCache<String,List<FDZoneCutoffInfo>> zoneCutoffCache = new TimedLruCache<String,List<FDZoneCutoffInfo>>(200, 60 * 60 * 1000);

	/** 5 min cache zoneCode -> remaining Capacity for next day (DlvZoneCapacityInfo)*/
	private static TimedLruCache<String,DlvZoneCapacityInfo> zoneCapacityCache = new TimedLruCache<String,DlvZoneCapacityInfo>(200, 5 * 60 * 1000);
	
	/* 5 hr delivery zip codes cache*/
	private static TimedLruCache<EnumServiceType, List<FDDeliveryZipInfo>> zipCodesCache = new TimedLruCache<EnumServiceType, List<FDDeliveryZipInfo>>(100, 5 * 60 * 60 * 1000);
	
	/* 5 hr delivery eta cache*/
	private static TimedLruCache<String, FDDeliveryETAModel> deliveryETACache = new TimedLruCache<String, FDDeliveryETAModel>(300, 60 * 60 * 1000);
	
	/* 5 hr cutoff times cache*/
	private static TimedLruCache<Date, List<Date>> cutoffTimesCache = new TimedLruCache<Date, List<Date>>(100, 5 * 60 * 60 * 1000);
	
	/* State County by Zip cache */
	private static TimedLruCache<String, StateCounty> stateCountyByZip = new TimedLruCache<String, StateCounty>(100, 60 * 60 * 60 * 1000);


	private DlvRestrictionsList dlvRestrictions = null;
	private long REFRESH_PERIOD = 1000 * 60 * 5; // 5 minutes
	private long lastRefresh = 0;

	private List<SiteAnnouncement> announcementList = null;
	private long ANN_REFRESH_PERIOD = 1000 * 60 * 10; //10 minutes
	private long lastAnnRefresh = 0;
	

	private Map<String, FDDeliveryDepotModel> depotMap = new HashMap<String, FDDeliveryDepotModel>();
	private List<FDDeliveryDepotModel> depotList = new ArrayList<FDDeliveryDepotModel>();
	private List<FDDeliveryDepotModel> pickupDepotList = new ArrayList<FDDeliveryDepotModel>();
	private List<FDDeliveryDepotModel> corpDepotList = new ArrayList<FDDeliveryDepotModel>();
	private long lastDepotRefresh = 0;

	private static final Category LOGGER = LoggerFactory.getInstance(FDDeliveryManager.class);

	/**
	 * private constructor to ensure single instance of FDDeliveryManager
	 */
	private FDDeliveryManager() throws NamingException {
		this.serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());

	}

	public static FDDeliveryManager getInstance() {
		if (dlvManager == null) {
			try {
				dlvManager = new FDDeliveryManager();
			} catch (NamingException e) {
				throw new FDRuntimeException(e);
			}
		}
		return dlvManager;
	}

	public DlvRestrictionManagerHome getDlvRestrictionManagerHome() {
		try {
			return (DlvRestrictionManagerHome) serviceLocator.getRemoteHome(FDStoreProperties.getDlvRestrictionManagerHome());
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}


	public DlvManagerHome getDlvManagerHome() {
		try {
			return (DlvManagerHome) serviceLocator.getRemoteHome(FDStoreProperties.getDeliveryManagerHome());
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}
	private synchronized void refreshRestrictionsCache() throws FDResourceException {
		if (System.currentTimeMillis() - lastRefresh > REFRESH_PERIOD) {
			try {
				DlvRestrictionManagerSB sb = getDlvRestrictionManagerHome().create();

				List<RestrictionI> l = sb.getDlvRestrictions();
				this.dlvRestrictions = new DlvRestrictionsList(l);

				lastRefresh = System.currentTimeMillis();

			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create SessionBean");
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to the SessionBean");
			}
		}
	}

	private synchronized void refreshSiteAnnouncementsCache() throws FDResourceException {
		if (System.currentTimeMillis() - lastAnnRefresh > ANN_REFRESH_PERIOD) {
			try {
				DlvManagerSB sb = getDlvManagerHome().create();
				List<SiteAnnouncement> l = sb.getSiteAnnouncements();
				this.announcementList = Collections.unmodifiableList(l);

				lastAnnRefresh = System.currentTimeMillis();
			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create SessionBean");
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to the SessionBean");
			}
		}
	}


	private long getRefreshInterval(){
		return FDStoreProperties.getDepotCacheRefreshPeriod() * 1000 * 60;
	}
	
	private synchronized void refreshDepots() throws FDResourceException {
		if (System.currentTimeMillis() - lastDepotRefresh > getRefreshInterval()) {
			
				HashMap<String, FDDeliveryDepotModel> newDepotMap = new HashMap<String, FDDeliveryDepotModel>();
				List<FDDeliveryDepotModel> newDepotList = new ArrayList<FDDeliveryDepotModel>();
				List<FDDeliveryDepotModel> newPickupDepotList = new ArrayList<FDDeliveryDepotModel>();
				List<FDDeliveryDepotModel> newCorpDepotList = new ArrayList<FDDeliveryDepotModel>();
				List<FDDeliveryDepotModel> depots = getAllDepots();
				for (Iterator<FDDeliveryDepotModel> dIter = depots.iterator(); dIter.hasNext();) {
					FDDeliveryDepotModel d = dIter.next();
					newDepotMap.put(d.getDepotCode(), d);
					if (d.isPickup()) {
						newPickupDepotList.add(d);
					} else if (d.isCorporateDepot()) {
						newCorpDepotList.add(d);
					} else {
						newDepotList.add(d);
					}
				}

				if ((newDepotMap != null) && !newDepotMap.isEmpty()) {
					depotMap = Collections.unmodifiableMap(newDepotMap);
					this.depotList = Collections.unmodifiableList(newDepotList);
					this.pickupDepotList = Collections.unmodifiableList(newPickupDepotList);
					this.corpDepotList = Collections.unmodifiableList(newCorpDepotList);
				}

				lastDepotRefresh = System.currentTimeMillis();
		}
	}

	protected Map<String, FDDeliveryDepotModel> getDepotMap() throws FDResourceException {
		this.refreshDepots();
		return this.depotMap;
	}

	public List<FDDeliveryDepotModel> getDepots() throws FDResourceException {
		this.refreshDepots();
		return this.depotList;
	}

	public List<FDDeliveryDepotModel> getPickupDepots() throws FDResourceException {
		this.refreshDepots();
		return this.pickupDepotList;
	}

	public List<FDDeliveryDepotModel> getCorporateDepots() throws FDResourceException {
		this.refreshDepots();
		return this.corpDepotList;
	}

	/**
	 * @return DlvDepotModel
	 */
	public FDDeliveryDepotModel getDepot(String depotCode) throws FDResourceException {
		return this.getDepotMap().get(depotCode);
	}

	
	public DlvRestrictionsList getDlvRestrictions() throws FDResourceException {
		this.refreshRestrictionsCache();
		return this.dlvRestrictions;
	}
	
	public List<SiteAnnouncement> getSiteAnnouncement() throws FDResourceException {
		this.refreshSiteAnnouncementsCache();
		return this.announcementList;
	}

	public FDDeliveryAddressVerificationResponse scrubAddress(AddressModel address) throws FDResourceException, FDInvalidAddressException {
		return this.scrubAddress(address, true);
	}

	public FDDeliveryAddressVerificationResponse scrubAddress(AddressModel addressModel, boolean useApartment) throws FDResourceException, FDInvalidAddressException {
		try {
			
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Address address = LogisticsDataEncoder.encodeAddress(addressModel);
			AddressVerificationResponse response = logisticsService.verifyAddress(address);
			
			FDDeliveryAddressVerificationResponse verifyResponse =  
					LogisticsDataDecoder.decodeAddressVerificationResponse(addressModel, response);
			
			return verifyResponse;
		
		} catch (FDLogisticsServiceException ce) {
			throw new FDResourceException(ce);
		} 
	}
	
	public void addApartment(AddressModel addressModel) throws FDResourceException, FDInvalidAddressException {

		try {
			
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Address address = LogisticsDataEncoder.encodeAddress(addressModel);
			Result response = logisticsService.addApartment(address);
			LogisticsDataDecoder.decodeResult(response);
			if(EnumApplicationException.GeocodingException.getValue() == response.getErrorCode()){
				throw new FDInvalidAddressException.GeocodingException("Unable to geocode this address");
			}else if(EnumApplicationException.InvalidAddressException.getValue() == response.getErrorCode()){
				throw new FDInvalidAddressException("Provided address is invalid");
			}
		} catch (FDLogisticsServiceException ce) {
			throw new FDResourceException(ce);
		} 
	
	}
	
	public FDDeliveryAddressCheckResponse checkAddress(AddressModel addressModel) throws FDResourceException, FDInvalidAddressException {
		try {

			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Address address = LogisticsDataEncoder.encodeAddress(addressModel);
			AddressCheckResponse response = logisticsService.checkAddress(address);
			return LogisticsDataDecoder.decodeAddressCheckResponse(addressModel, response);
		
		} catch (FDLogisticsServiceException ce) {
			throw new FDResourceException(ce);
		}
	}
	
	public boolean checkForAlcoholDelivery(String state, String county, String zipCode) throws FDResourceException {
		MunicipalityInfo muni = this.getMunicipalityInfos().getMunicipalityInfo(state, county, null);
		if(muni != null && muni.isAlcoholRestricted()){
			return true;
		} 
		return false;
	}
	
	public boolean checkForAlcoholDelivery(AddressModel address) throws FDResourceException {
		try {
			MunicipalityInfo muni = this.getMunicipalityInfos().getMunicipalityInfo(address.getState(), getCounty(address), address.getCity());
			if(muni != null && muni.isAlcoholRestricted()){
				return false;
			}

			DlvRestrictionManagerSB sb = getDlvRestrictionManagerHome().create();
			return sb.checkForAlcoholDelivery(address.getScrubbedStreet(), address.getZipCode(), address.getApartment());
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public EnumRestrictedAddressReason checkAddressForRestrictions(AddressModel address) throws FDResourceException {
		try {
			DlvRestrictionManagerSB sb = getDlvRestrictionManagerHome().create();
			return sb.checkAddressForRestrictions(address);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}



	public FDDeliveryAddressGeocodeResponse geocodeAddress(AddressModel addressModel) throws FDResourceException, FDInvalidAddressException {

		try {

			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Address address = LogisticsDataEncoder.encodeAddress(addressModel);
			AddressVerificationResponse response = logisticsService.geocodeAddress(address);
			FDDeliveryAddressGeocodeResponse geocode = LogisticsDataDecoder.decodeAddressGeocodeResponse(addressModel, response);
			return geocode;
		} catch (FDLogisticsServiceException ce) {
			throw new FDResourceException(ce);
		} 
	}

	public FDDeliveryZoneInfo getZoneInfo(AddressModel address, Date date, CustomerAvgOrderSize orderSize, EnumRegionServiceType serviceType) throws FDResourceException, FDInvalidAddressException {
		try {
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeliveryZones response = logisticsService.getZone(LogisticsDataEncoder.encodeDeliveryZoneRequest(address, date, orderSize, serviceType));
			return LogisticsDataDecoder.decodeDeliveryZoneInfo(response);
		
		} catch (FDLogisticsServiceException ce) {
			throw new FDResourceException(ce);
		} 
	}
	
	public List<FDZoneCutoffInfo> getCutofftimeForZone(String zoneCode, Date requestedDate) throws FDResourceException {

		if("".equals(zoneCode)) {
			return Collections.<FDZoneCutoffInfo>emptyList();
		}
		List<FDZoneCutoffInfo> times = zoneCutoffCache.get(zoneCode);
		if(times == null) {
			try {
				ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
				DeliveryZoneCutoffs response = logisticsService.getZoneCutoffs(LogisticsDataEncoder.encodeZoneRequest(zoneCode, requestedDate));
				times = LogisticsDataDecoder.decodeDeliveryZoneCutoffs(response);
				if(!(times == null || times.isEmpty())){
					zoneCutoffCache.put(zoneCode, times);
				}
			} catch (FDLogisticsServiceException e) {
				throw new FDResourceException(e);
			} 
		}

		return times;
	}

	public DlvZoneCapacityInfo getZoneCapacity(String zoneCode, Date requestedDate) throws FDResourceException {

		DlvZoneCapacityInfo capacity = zoneCapacityCache.get(zoneCode);

		if(capacity == null) {
			try {
				ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
				DeliveryZoneCapacity response = logisticsService.getZoneCapacity(LogisticsDataEncoder.encodeZoneRequest(zoneCode, requestedDate));
				capacity = LogisticsDataDecoder.decodeDeliveryZoneCapacity(response);
				if(capacity!=null)
					zoneCapacityCache.put(zoneCode, capacity);
			} catch (FDLogisticsServiceException e) {
				throw new FDResourceException(e);
			} 
		}

		return capacity;
	}

	public List<FDDeliveryZipInfo> getDeliverableZipCodes(EnumServiceType serviceType) throws FDResourceException {
		
		List<FDDeliveryZipInfo>  result = zipCodesCache.get(serviceType);
		try {
			if(result == null){
				ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
				DeliveryZips response = logisticsService.getDeliverableZipCodes(LogisticsDataEncoder.encodeDeliveryZipRequest(serviceType));
				result = LogisticsDataDecoder.decodeDeliveryZips(response);
				if(result!=null && !result.isEmpty()){
					zipCodesCache.put(serviceType, result);
				}
			}
		
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
		return result;
	}

	public FDDeliveryServiceSelectionResult getDeliveryServicesByZipCode(String zipCode) throws FDResourceException {
		try {
			FDDeliveryServiceSelectionResult result = zipCheckCache.get(zipCode);
			if (result == null) {
				ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
				DeliveryServices response = logisticsService.getDeliveryServices(LogisticsDataEncoder.encodeDeliveryZipCodeRequest(zipCode));
				result = LogisticsDataDecoder.decodeDeliveryServices(response);
				zipCheckCache.put(zipCode, result);
			}
			return result;
		} catch (FDInvalidAddressException e) {
			// eat it.
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
		return null; 
	}
	
	public boolean isZipCodeEbtAccepted(String zipCode) throws FDResourceException {
		boolean isEBTAccepted = false;
		if (zipCode != null) {
			FDDeliveryServiceSelectionResult serviceResult =FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode);		
			isEBTAccepted = null != serviceResult ? serviceResult.isEbtAccepted() : false;
		}
		return isEBTAccepted;
	}

	public FDDeliveryServiceSelectionResult getDeliveryServicesByAddress(AddressModel address) throws FDResourceException, FDInvalidAddressException {
		try {
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeliveryServices response = logisticsService.getDeliveryServices(LogisticsDataEncoder.encodeAddress(address));
			FDDeliveryServiceSelectionResult result = LogisticsDataDecoder.decodeDeliveryServices(response);
			
			DlvRestrictionManagerSB sb = getDlvRestrictionManagerHome().create();
			result.setRestrictionReason(sb.checkAddressForRestrictions(address));
			return result;
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		} 
	}
	
	public FDDeliveryTimeslots getTimeslotsForDateRangeAndZone(List<DateRange> dateranges,  TimeslotEvent event,
			ContactAddressModel address, CustomerAvgOrderSize orderSize, OrderContext context) throws FDResourceException {
		
		return getTimeslotsForDateRangeAndZone(dateranges, event, address, orderSize, false, false, context);
	}

	public FDDeliveryTimeslots getTimeslotsForDateRangeAndZone(List<DateRange> dateranges,  TimeslotEvent event,
			ContactAddressModel address, CustomerAvgOrderSize orderSize, boolean forceOrder, boolean deliveryInfo, OrderContext context) throws FDResourceException{
		return getTimeslotsForDateRangeAndZone(dateranges, event,
			 address, orderSize, null, forceOrder, deliveryInfo, context);
	}

	public FDDeliveryTimeslots getTimeslotsForDateRangeAndZone(List<DateRange> dateranges,  TimeslotEvent event,
			ContactAddressModel address, CustomerAvgOrderSize orderSize, List<FDReservation> reservations, boolean forceOrder, boolean deliveryInfo,
			OrderContext context) throws FDResourceException {
		try {			
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeliveryTimeslots response = logisticsService.getTimeslots(LogisticsDataEncoder.encodeTimeslotRequest(dateranges, event,
					 address, orderSize, forceOrder, deliveryInfo, context));
			FDDeliveryTimeslots result = LogisticsDataDecoder.decodeDeliveryTimeslots(response);
			return result;
			
		} catch (FDInvalidAddressException iae) {
			iae.printStackTrace();
			throw new FDResourceException(iae);
		}catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		} 
	}


	public FDTimeslot getTimeslotsById(String timeslotId,String buildingId, boolean checkPremium) throws FDResourceException {
		
		try{
		ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
		Timeslot response = logisticsService.getTimeslotById(LogisticsDataEncoder.encodeTimeslotRequest(timeslotId, buildingId, checkPremium));
		return LogisticsDataDecoder.decodeTimeslot(response);
	}catch (FDLogisticsServiceException ex) {
		throw new FDResourceException(ex);
	}
	}
	
	public FDDeliveryETAModel getETAWindowBySaleId(String saleId) throws FDResourceException {
		FDDeliveryETAModel result = deliveryETACache.get(saleId);
		
		try {
			if(result == null){
			IAirclicService logisticsService = LogisticsServiceLocator.getInstance().getAirclicService();
			DeliveryETA response = logisticsService.getDeliveryETA(saleId);
			result = LogisticsDataDecoder.decodeDeliveryETA(response);
			if(result!=null){
				deliveryETACache.put(saleId, result);
			}
			}
		} catch (FDLogisticsServiceException ex) {
			throw new FDResourceException(ex);
		}
		return result;
	}

	public FDReservation reserveTimeslot(
		String timeslotId,
		String customerId,
		EnumReservationType type,
		ContactAddressModel address,
		boolean chefsTable,
		String ctDeliveryProfile,
		boolean isForced, TimeslotEvent event, boolean hasSteeringDiscount) throws FDResourceException, ReservationException {
		

		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.reserveTimeslot(
					 timeslotId,
					 customerId,
					 type,
					 address,
					 chefsTable,
					 ctDeliveryProfile,
					 isForced,  event, hasSteeringDiscount);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}	
	
	}

	public List<FDReservation> getReservationsForCustomer(String customerId) throws FDResourceException {

		try {

			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeliveryReservations response = logisticsService.getReservationsByCriteria(LogisticsDataEncoder.encodeReservationByCustomerRequest(customerId));
			return LogisticsDataDecoder.decodeReservations(response);			
		}  catch (FDLogisticsServiceException ex) {
			throw new FDResourceException(ex);
		}
		
	}
	
	public List<FDReservation> getReservationsByCriteria(SearchRequest request) throws FDResourceException {

		try {

			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeliveryReservations response = logisticsService.getReservationsByCriteria(request);
			return LogisticsDataDecoder.decodeReservations(response);			
		}  catch (FDLogisticsServiceException ex) {
			throw new FDResourceException(ex);
		}

	
	}


	public void commitReservation(String rsvId, String customerId, OrderContext context, ContactAddressModel address,boolean pr1, TimeslotEvent event) throws ReservationException, FDResourceException {

		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			sb.commitReservation(rsvId, customerId, context, address, pr1, event);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}	
	}

	public boolean releaseReservation(String rsvId, ContactAddressModel addressModel, TimeslotEvent event, boolean restoreReservation) throws FDResourceException {
		try {

			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Result response = logisticsService.cancelReservation(LogisticsDataEncoder.encodeCancelReservationRequest(rsvId, addressModel, event, restoreReservation));
			LogisticsDataDecoder.decodeResult(response);	
			if(response!=null && response.getDebug()!=null && response.getDebug().containsKey("restored")){
				return Boolean.getBoolean(response.getDebug().get("restored"));
			}
			return false;
		}  catch (FDLogisticsServiceException ex) {
			throw new FDResourceException(ex);
		}
	}
	
	public void updateReservationStatus(String rsvId, ContactAddressModel addressModel, String erpOrderId) throws FDResourceException {
		
		try{
		ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
		Result response = logisticsService.updateReservationSize(
						LogisticsDataEncoder.encodeUpdateReservation(rsvId, erpOrderId, 
						LogisticsDataEncoder.encodeAddress(addressModel)));
		LogisticsDataDecoder.decodeResult(response);	
		
	} catch (FDLogisticsServiceException ex) {
		throw new FDResourceException(ex);
	}
	}

	public FDReservation getReservation(String rsvId, String orderId) throws FDResourceException {
		try {
			

			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeliveryReservations response = logisticsService.getReservationById(rsvId);
			List<FDReservation> fdReservations =  LogisticsDataDecoder.decodeReservations(response);
			FDReservation fdReservation = null;
			if(fdReservations!=null && fdReservations.size()>0){
				fdReservation = fdReservations.get(0);
				fdReservation.setDeliveryETA(getETAWindowBySaleId(orderId));
			}
			
			return fdReservation;
		} catch (FDLogisticsServiceException ex) {
			throw new FDResourceException(ex);
		}
	}

	public void saveFutureZoneNotification(String email, String zip,EnumServiceType serviceType) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			sb.saveFutureZoneNotification(email, zip,serviceType.getName());
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	
		// table will be moved to cust schema DLV.ZONENOTIFICATION
	}

	public void addExceptionAddress(ExceptionAddress ex) throws FDResourceException {
		
		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Result result = logisticsService.addAddressException(LogisticsDataEncoder.encodeAddressExceptionRequest(ex));
			LogisticsDataDecoder.decodeResult(result);
			
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}
	
	
	}

	public List<ExceptionAddress> searchExceptionAddresses(ExceptionAddress ex) throws FDResourceException {
		
		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			AddressExceptionResponse result = logisticsService.searchAddressException(
					LogisticsDataEncoder.encodeAddressExceptionRequest(ex));
			return LogisticsDataDecoder.decodeAddressExceptions(result);
			
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}

	
	}

	public void deleteAddressException(String id) throws FDResourceException {

		
		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Result result = logisticsService.deleteAddressException(id);
			LogisticsDataDecoder.decodeResult(result);
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}
	
	}

	public String getCounty(String city, String state) throws FDResourceException{

		try{
			Set<StateCounty> stateCounty = countiesByState.get(state);
			if(stateCounty == null){
				DlvManagerSB sb = getDlvManagerHome().create();
				stateCounty =  sb.getCountiesByState(state);
				if(stateCounty!=null){
					countiesByState.put(state, stateCounty);
				}
			}
			if(stateCounty!=null){
				for(StateCounty sc : stateCounty){
					if(sc!=null && sc.getCity()!=null && sc.getCity().equalsIgnoreCase(city)){
						return sc.getCounty();
					}
				}
			}
		}catch (RemoteException re) {
			throw new FDResourceException(re);
		}catch (CreateException ce) {
			throw new FDResourceException(ce);
		}	
		return null;
	
	}

	public String getCounty(AddressModel address) throws FDResourceException{
		String county = "";
		if(address != null){
			AddressInfo info = address.getAddressInfo();
			if(info == null){
				info = new AddressInfo();
				address.setAddressInfo(info);
			}

			county = info.getCounty() == null ? getCounty(address.getCity(), address.getState()) : info.getCounty();
			info.setCounty(county);
		}
		return county;
	}

	public List<String> getCountiesByState(EnumStateCodes ec) throws FDResourceException{
	    return getCountiesByState(ec.getId());
	}

	public List<String> getCountiesByState(String state) throws FDResourceException{
		
		try{
			Set<StateCounty> stateCounty = countiesByState.get(state);
			if(stateCounty == null){
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Map<String, Set<StateCounty>> scMap = logisticsService.getCountiesByState();
			stateCounty = scMap.get(state);
			if(stateCounty!=null){
				countiesByState.put(state, stateCounty);
			}
			}
			Set<String> counties = new HashSet<String>();
			if(stateCounty!=null){
				for(StateCounty sc : stateCounty){
					if(sc!=null && sc.getCounty()!=null){
						counties.add(sc.getCounty());
					}
				}
			}
			return new ArrayList<String>(counties);
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}

	
	}


	public List<ExceptionAddress> searchGeocodeException(ExceptionAddress ex) throws FDResourceException {
		
		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			AddressExceptionResponse result = logisticsService.searchGeocodeException(
					LogisticsDataEncoder.encodeAddressExceptionRequest(ex));
			return LogisticsDataDecoder.decodeAddressExceptions(result);
			
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}

	
	}

	public void deleteGeocodeException(ExceptionAddress ex) throws FDResourceException {

		
		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Result result = logisticsService.deleteGeocodeAddress(
					LogisticsDataEncoder.encodeAddressExceptionRequest(ex));
			LogisticsDataDecoder.decodeResult(result);
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}

	}

	public void addGeocodeException(ExceptionAddress ex, String userId) throws FDInvalidAddressException, FDResourceException, DuplicateKeyException {
		
		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Result result = logisticsService.addGeocodeException(
					LogisticsDataEncoder.encodeAddressExceptionRequest(ex, userId));
			LogisticsDataDecoder.decodeResult(result);
			if(EnumApplicationException.InvalidAddressException.getValue() == result.getErrorCode()){
				throw new FDInvalidAddressException();
			}else if(EnumApplicationException.DuplicateKeyException.getValue() == result.getErrorCode()){
				throw new DuplicateKeyException();
			}
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}
	
	
	}

	/* municipality info stuff.. not sure if it should really live here, but its a start  */
	private MunicipalityInfoWrapper municipalityInfos = null;
	private long MUNI_REFRESH_PERIOD = 1000 * 60 * 5; // 5 minutes
	private long muni_lastRefresh = 0;

	public MunicipalityInfoWrapper getMunicipalityInfos() throws FDResourceException {
		return this.getMunicipalityInfos(false);
	}
	
	public MunicipalityInfoWrapper getMunicipalityInfos(boolean forceReload) throws FDResourceException {
		this.refreshMunicipalityInfos(forceReload);
		return this.municipalityInfos;
	}
	
	private void refreshMunicipalityInfos(boolean forceReload) throws FDResourceException {
		if (forceReload || System.currentTimeMillis() - muni_lastRefresh > MUNI_REFRESH_PERIOD) {
			try {
				DlvManagerSB sb = getDlvManagerHome().create();
				this.municipalityInfos = new MunicipalityInfoWrapper(sb.getMunicipalityInfos());
				muni_lastRefresh = System.currentTimeMillis();
			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create SessionBean");
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to the SessionBean");
			}
		}

	}

	public StateCounty lookupStateCountyByZip(String zipcode) throws FDResourceException{
		if(zipcode!=null){
			try {
				StateCounty sc = stateCountyByZip.get(zipcode);
				if(sc == null){
					DlvManagerSB sb = getDlvManagerHome().create();
					sc =  sb.lookupStateCountyByZip(zipcode);
					if(sc != null){
						stateCountyByZip.put(zipcode, sc);
					}
				}
				return sc;
			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create SessionBean");
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to the SessionBean");
			}
		}
		return null;
	}

	public String lookupStateByZip(String zipcode) throws FDResourceException{
		StateCounty sc = lookupStateCountyByZip(zipcode);
		if(sc != null){
			return sc.getState();
		}

		return null;
	}

	public String lookupCountyByZip(String zipcode) throws FDResourceException{
		StateCounty sc = lookupStateCountyByZip(zipcode);
		if(sc != null){
			return sc.getCounty();
		}

		return null;
	}

	public List<Date> getCutofftimesByDate(Date day) throws FDResourceException {
		List<Date> result = cutoffTimesCache.get(day);
		
		try{
			if(result==null){
				ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
				ListOfDates dates = logisticsService.getDateCutoffs(day);
				LogisticsDataDecoder.decodeResult(dates);
				result = dates.getCutoffs();
				if(result!=null){
					cutoffTimesCache.put(day, result);
				}
			}
			
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}
		return result;
	}
	
	public List<String> getActiveZoneCodes() throws FDResourceException{

		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeliveryZones zones = logisticsService.getActiveZones();
			return LogisticsDataDecoder.decodeDeliveryZoneCodes(zones);
			
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}
	
	}
	
	public  List<DlvZoneModel> getActiveZones() throws FDResourceException{
		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeliveryZones zones = logisticsService.getActiveZones();
			return LogisticsDataDecoder.decodeDeliveryZones(zones);
			
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}
	}

	public String getDepotFacility(String depotLocationId) throws FDResourceException {
		FDDeliveryDepotModel d = getDepotByLocationId(depotLocationId);
		if(d == null){
			return "";
		}
		
		FDDeliveryDepotLocationModel l = d.getLocation(depotLocationId);
		if(l != null){
			return l.getFacility();
		}
		return "";
	}

	public List<FDDeliveryDepotModel> getAllDepots() throws FDResourceException {
		try{
				
		ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
		Depots pickups = logisticsService.getPickupLocations(LogisticsDataEncoder.encodePickupLocationRequest());
		return LogisticsDataDecoder.decodePickupLocations(pickups);
		
	}catch(FDLogisticsServiceException e){
		throw new FDResourceException(e);
	}
	}

		
	public FDDeliveryDepotModel getDepotByLocationId(
			String locationId) throws FDResourceException {
		
		Map<String, FDDeliveryDepotModel> depotMap = getDepotMap();
			for(FDDeliveryDepotModel pickup: depotMap.values()){
				for(FDDeliveryDepotLocationModel location : pickup.getLocations()){
					if(location.getPK()!=null && 
							location.getPK().getId().equals(locationId))
						return pickup;
				}
			}
		return null;
	}

	public FDReservation getReservationById(String reservationId) throws FDResourceException {
		try {
				

				ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
				DeliveryReservations response = logisticsService.getReservationsByCriteria(LogisticsDataEncoder.encodeReservationByIdRequest(reservationId));
				List<FDReservation> fdReservations =  LogisticsDataDecoder.decodeReservations(response);
				FDReservation fdReservation = null;
				if(fdReservations!=null && fdReservations.size()>0){
					fdReservation = fdReservations.get(0);
				}
			
				return fdReservation;
			} catch (FDLogisticsServiceException ex) {
				throw new FDResourceException(ex);
			}
	}
	
	public void logSessionEvent(SessionEvent event) throws FDResourceException{

		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Result result = logisticsService.logSessionEvent(event);
			LogisticsDataDecoder.decodeResult(result);
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}
	
	
	
	}
	
	public FDReservation validateReservation(CustomerAvgOrderSize orderSize, 
			FDReservation reservation, ErpAddressModel address, TimeslotEvent event) throws FDResourceException {
		
		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeliveryReservations result = logisticsService.validateReservation(
					LogisticsDataEncoder.encodeValidateReservation(orderSize, 
					reservation, address, event));
			LogisticsDataDecoder.decodeValidateReservation(result);
			
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}
		return reservation;
	
	}

	public String getCustomerServiceEmail(String depotCode) throws FDResourceException {
		FDDeliveryDepotModel depot = this.getDepot(depotCode);
		if (depot == null) {
			throw new FDResourceException("Cannot Find Depot for depotCode: " + depotCode);
		} else {
			return depot.getCustomerServiceEmail();
		}
	}

	public StateCounty getStateCountyByZipcode(String zipCode){
		try{
			return lookupStateCountyByZip(zipCode);
		}catch(FDResourceException e){
			LOGGER.info("State information not found for zipcode:"+zipCode); 
		}
		return null;
	}

	public int cancelReservations(GenericSearchCriteria resvCriteria,
			String initiator, String notes) throws FDResourceException {

		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeleteReservationsResponse result = logisticsService.cancelReservations(LogisticsDataEncoder.encodeReservationByCriteriaRequest(resvCriteria));
			LogisticsDataDecoder.decodeResult(result);
			return result.getUpdateCount();
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}

	}
	
	public void addSubscriptions(String customerId, String mobileNumber,
			String textOffers, String textDelivery, String orderNotices,
			String orderExceptions, String offers, String partnerMessages,
			Date receivedDate)
			throws FDResourceException {
		try {
			ILogisticsService logisticsService = LogisticsServiceLocator
					.getInstance().getLogisticsService();
			Result result = logisticsService
					.addSubscription(LogisticsDataEncoder
							.encodeAddSubscriptionRequest(customerId,
									mobileNumber, textOffers, textDelivery,
									orderNotices, orderExceptions, offers,
									partnerMessages, receivedDate));
			LogisticsDataDecoder.decodeResult(result);
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}

	public void sendShippingAddress(ErpAddressModel address)  throws FDResourceException {


		if(!FDStoreProperties.isUPSBlackholeEnabled()) {
			processAddress(address);
		}	

	
	}

	public boolean checkAccessCode(String depotCode, String accessCode) throws FDResourceException {

		FDDeliveryDepotModel d = this.getDepot(depotCode);
		if (d == null)
			return false;
		else
			return d.getRegistrationCode().equalsIgnoreCase(accessCode) && !d.isDeactivated();
	}

	public List<String> getScanReportedLates() throws FDResourceException {

		try{
			IAirclicService airclicService = LogisticsServiceLocator.getInstance().getAirclicService();
			ListOfObjects<String> result = airclicService.getScanReportedLates();
			LogisticsDataDecoder.decodeResult(result);
			return result.getData();
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}

		
	}

	public List<AddressModel> findSuggestionsForAmbiguousAddress(AddressModel address) throws FDResourceException, FDInvalidAddressException {
		try {
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			ListOfObjects<Address> result = logisticsService.findSuggestionsForAmbiguousAddress(LogisticsDataEncoder.encodeAddress(address));
			LogisticsDataDecoder.decodeResult(result);
			return LogisticsDataDecoder.decodeAddressList(result.getData());
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}
	


	public void processAddress(AddressI address) throws FDResourceException {

		
		try {
			LOGGER.debug("Sending Address For Routing System# " + address.getAddress1()+" : "+address.getZipCode());		
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Result result = logisticsService.processAddress(LogisticsDataEncoder.encodeAddress((AddressModel)address));
			LogisticsDataDecoder.decodeResult(result);
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	
	}
	
	public FDDeliveryZoneInfo getZoneByGeoLocation(Double latitude, Double longitude) throws FDResourceException, FDInvalidAddressException {

		
		try {
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			DeliveryZones result = logisticsService.getZoneByGeoLocation(new GeoLocation(latitude, longitude));
			return LogisticsDataDecoder.decodeDeliveryZoneInfo(result);
			
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	
	}
	
}
