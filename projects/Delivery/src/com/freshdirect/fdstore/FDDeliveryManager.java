/*
 * $Workfile:FDDeliveryManager.java$
 *
 * $Date:8/28/2003 3:23:16 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.common.pricing.MunicipalityInfoWrapper;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvAddressVerificationResponse;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.DlvZoneCapacityInfo;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.ExceptionAddress;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.GeographyRestriction;
import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimedLruCache;
import com.freshdirect.routing.ejb.RoutingGatewayHome;
import com.freshdirect.routing.ejb.RoutingGatewaySB;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;

/**
 *
 *
 * @version $Revision:23$
 * @author $Author:Viktor Szathmary$
 */
public class FDDeliveryManager {

	private final ServiceLocator serviceLocator;
	private static FDDeliveryManager dlvManager = null;

	/** cache zipCode -> DlvServiceSelectionResult */
	private static TimedLruCache zipCheckCache = new TimedLruCache(200, 60 * 60 * 1000);
	
	/** 1 hr cache zoneCode -> List of cutoff-times for next day (DlvZoneCutoffInfo)*/
	private static TimedLruCache zoneCutoffCache = new TimedLruCache(200, 60 * 60 * 1000);
	
	/** 5 min cache zoneCode -> remaining Capacity for next day (DlvZoneCapacityInfo)*/
	private static TimedLruCache zoneCapacityCache = new TimedLruCache(200, 5 * 60 * 1000);

	private DlvRestrictionsList dlvRestrictions = null;
	private long REFRESH_PERIOD = 1000 * 60 * 5; // 5 minutes
	private long lastRefresh = 0;	
	
	private List announcementList = null;
	private long ANN_REFRESH_PERIOD = 1000 * 60 * 10; //10 minutes
	private long lastAnnRefresh = 0;

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

	private synchronized void refreshRestrictionsCache() throws FDResourceException {
		if (System.currentTimeMillis() - lastRefresh > REFRESH_PERIOD) {
			try {
				DlvManagerSB sb = getDlvManagerHome().create();

				List l = sb.getDlvRestrictions();
				this.dlvRestrictions = new DlvRestrictionsList(l);

				lastRefresh = System.currentTimeMillis();

			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create SessionBean");
			} catch (DlvResourceException e) {
				throw new FDResourceException(e);
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to the SessionBean");
			}
		}
	}

	private synchronized void refreshSiteAnnouncementsCache() throws FDResourceException {
		if (System.currentTimeMillis() - lastAnnRefresh > ANN_REFRESH_PERIOD) {
			try {
				DlvManagerSB sb = getDlvManagerHome().create();
				List l = sb.getSiteAnnouncements();
				this.announcementList = Collections.unmodifiableList(l);

				lastAnnRefresh = System.currentTimeMillis();
			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create SessionBean");
			} catch (DlvResourceException e) {
				throw new FDResourceException(e);
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to the SessionBean");
			}
		}
	}

	public DlvRestrictionsList getDlvRestrictions() throws FDResourceException {
		this.refreshRestrictionsCache();
		return this.dlvRestrictions;
	}
	
	public List getGeographicDlvRestrictions(AddressModel address) throws FDResourceException {		
		try {
			DlvManagerSB sb = getDlvManagerHome().create();

			return sb.getGeographicDlvRestrictions(address);			

		} catch (CreateException e) {
			throw new FDResourceException(e, "Cannot create SessionBean");
		} catch (DlvResourceException e) {
			throw new FDResourceException(e);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to the SessionBean");
		}		
	}

	public List getSiteAnnouncement() throws FDResourceException {
		this.refreshSiteAnnouncementsCache();
		return this.announcementList;
	}

	public DlvAddressVerificationResponse scrubAddress(AddressModel address) throws FDResourceException {
		return this.scrubAddress(address, true);
	}

	public void addApartment(AddressModel address) throws FDResourceException, FDInvalidAddressException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			sb.addApartment(address);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (InvalidAddressException.GeocodingException geoe) {
			throw new FDInvalidAddressException.GeocodingException(geoe.getMessage());
		} catch (InvalidAddressException e) {
			throw new FDInvalidAddressException(e.getMessage());
		}
	}

	public DlvAddressVerificationResponse scrubAddress(AddressModel address, boolean useApartment) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.scrubAddress(address, useApartment);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public boolean checkForAlcoholDelivery(AddressModel address) throws FDResourceException {
		try {
			MunicipalityInfo muni = this.getMunicipalityInfos().getMunicipalityInfo(address.getState(), getCounty(address), address.getCity());
			if(muni != null && muni.isAlcoholRestricted()){
				return false;
			}
			
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.checkForAlcoholDelivery(address.getScrubbedStreet(), address.getZipCode());
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public EnumRestrictedAddressReason checkAddressForRestrictions(AddressModel address) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.checkAddressForRestrictions(address);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public ArrayList findSuggestionsForAmbiguousAddress(AddressModel address) throws FDResourceException, FDInvalidAddressException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.findSuggestionsForAmbiguousAddress(address);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (InvalidAddressException.GeocodingException geoe) {
			throw new FDInvalidAddressException.GeocodingException(geoe.getMessage());
		} catch (InvalidAddressException iae) {
			throw new FDInvalidAddressException(iae.getMessage());
		}
	}

	public DlvAddressGeocodeResponse geocodeAddress(AddressModel address) throws FDResourceException, FDInvalidAddressException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.geocodeAddress(address);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (InvalidAddressException.GeocodingException geoe) {
			throw new FDInvalidAddressException.GeocodingException(geoe.getMessage()); 
		} catch (InvalidAddressException iae) {
			throw new FDInvalidAddressException(iae.getMessage());
		}
	}

	public DlvZoneInfoModel getZoneInfo(AddressModel address, Date date) throws FDResourceException, FDInvalidAddressException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.getZoneInfo(address, date);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (InvalidAddressException.GeocodingException geoe) {
			throw new FDInvalidAddressException.GeocodingException(geoe.getMessage()+">"+address.getAddress1()+">"+address.getZipCode());
		} catch (InvalidAddressException iae) {
			throw new FDInvalidAddressException(iae.getMessage());
		}
	}
	
	public DlvZoneInfoModel getZoneInfoForDepot(String regionId, String zoneCode, Date date) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.getZoneInfoForDepot(regionId, zoneCode, date);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (DlvResourceException de) {
			throw new FDResourceException(de);
		}
	}
	
	public List getCutofftimeForZone(String zoneCode, Date day) throws FDResourceException {
		
		if("".equals(zoneCode)) {
			return Collections.EMPTY_LIST;
		}
		List times = (List) zoneCutoffCache.get(zoneCode);
		if(times == null) {
			try {
				DlvManagerSB sb = getDlvManagerHome().create();
				times = sb.getCutoffInfo(zoneCode, day);
				zoneCutoffCache.put(zoneCode, times);
			} catch (RemoteException e) {
				throw new FDResourceException(e);
			} catch (CreateException e) {
				throw new FDResourceException(e);
			}
		}
		
		return times;
	}
	
	public DlvZoneCapacityInfo getZoneCapacity(String zoneCode, Date day) throws FDResourceException {

		DlvZoneCapacityInfo capacity = (DlvZoneCapacityInfo) zoneCapacityCache.get(zoneCode);
		
		if(capacity == null) {
			try {
				DlvManagerSB sb = getDlvManagerHome().create();
				capacity = sb.getZoneCapacity(zoneCode, day);
				zoneCapacityCache.put(zoneCode, capacity);
			} catch (RemoteException e) {
				throw new FDResourceException(e);
			} catch (CreateException e) {
				throw new FDResourceException(e);
			}
			
			
		}
		
		return capacity;
	}

	public List getDeliverableZipCodes(EnumServiceType serviceType) throws FDResourceException {

		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.getDeliverableZipCodes(serviceType);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public DlvServiceSelectionResult checkZipCode(String zipCode) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			DlvServiceSelectionResult result = (DlvServiceSelectionResult) zipCheckCache.get(zipCode);
			if (result == null) {
				result = sb.checkServicesForZipCode(zipCode);
				zipCheckCache.put(zipCode, result);
			}
			return result;
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public DlvServiceSelectionResult checkAddress(AddressModel address) throws FDResourceException, FDInvalidAddressException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.checkServicesForAddress(address);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (InvalidAddressException.GeocodingException geoe) {
			throw new FDInvalidAddressException.GeocodingException(geoe.getMessage());
		} catch (InvalidAddressException iae) {
			throw new FDInvalidAddressException(iae.getMessage());
		}
	}

	public ArrayList<FDTimeslot> getTimeslotsForDateRangeAndZone(Date begDate, Date endDate, ContactAddressModel address) throws FDResourceException {
		try {
			ArrayList<FDTimeslot> retLst = new ArrayList<FDTimeslot>();
			DlvManagerSB sb = getDlvManagerHome().create();
			List<DlvTimeslotModel> timeslots = sb.getTimeslotForDateRangeAndZone(begDate, endDate, address);
			for (Iterator<DlvTimeslotModel> i = timeslots.iterator(); i.hasNext();) {
				DlvTimeslotModel timeslot =  i.next();
				retLst.add(new FDTimeslot(timeslot));
			}
			
			if(FDStoreProperties.isDynamicRoutingEnabled()) 
				RoutingUtil.getInstance().sendDateRangeAndZoneForTimeslots(retLst, address);
			return retLst;
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (InvalidAddressException iae) {
			throw new FDResourceException(iae);
		}
	}
	
	

	public ArrayList getAllTimeslotsForDateRange(Date startDate, Date endDate, AddressModel address) throws FDResourceException {
		try {
			ArrayList retLst = new ArrayList();
			DlvManagerSB sb = getDlvManagerHome().create();
			List timeslots = sb.getAllTimeslotsForDateRange(startDate, endDate, address);
			for (Iterator i = timeslots.iterator(); i.hasNext();) {
				DlvTimeslotModel timeslot = (DlvTimeslotModel) i.next();
				retLst.add(new FDTimeslot(timeslot));
			}
			return retLst;
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (InvalidAddressException iae) {
			throw new FDResourceException(iae);
		}
	}

	
	public List getTimeslotCapacityInfo(java.util.Date date) {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.getTimeslotCapacityInfo(date);
		} catch (RemoteException re) {
			throw new FDRuntimeException(re);
		} catch (CreateException ce) {
			throw new FDRuntimeException(ce);
		}
	}
	
	public FDTimeslot getTimeslotsById(String timeslotId) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			DlvTimeslotModel model = sb.getTimeslotById(timeslotId);
			return new FDTimeslot(model);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (FinderException fe) {
			throw new FDResourceException("Cannot find timeslot for id: " + timeslotId + "\n" + fe.getMessage());
		}
	}

	public List getTimeslotsForDepot(Date begDate, Date endDate, String regionId, String zoneCode, ContactAddressModel address) throws FDResourceException {

		try {
			List retLst = new ArrayList();
			DlvManagerSB sb = getDlvManagerHome().create();
			List timeslots = sb.getTimeslotsForDepot(begDate, endDate, regionId, zoneCode);
			for (Iterator i = timeslots.iterator(); i.hasNext();) {
				DlvTimeslotModel timeslot = (DlvTimeslotModel) i.next();
				retLst.add(new FDTimeslot(timeslot));
			}
			
			if(FDStoreProperties.isDynamicRoutingEnabled()) 
				RoutingUtil.getInstance().sendDateRangeAndZoneForTimeslots(retLst, address);
			return retLst;
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (DlvResourceException de) {
			throw new FDResourceException(de);
		}
	}

	public FDReservation reserveTimeslot(
		FDTimeslot timeslot,
		String customerId,
		long holdTime,
		EnumReservationType type,
		ContactAddressModel address,
		boolean chefsTable) throws FDResourceException, ReservationException {

		try {
			
			
			
			DlvManagerSB sb = getDlvManagerHome().create();
			DlvReservationModel dlvReservation = sb
				.reserveTimeslot(timeslot.getDlvTimeslot(), customerId, holdTime, type, address.getId(), chefsTable);

			FDReservation reservation = new FDReservation(
				dlvReservation.getPK(),
				timeslot,
				dlvReservation.getExpirationDateTime(),
				dlvReservation.getReservationType(),
				dlvReservation.getCustomerId(),
				address.getId(),
				dlvReservation.isChefsTable(),dlvReservation.getUnassignedActivityType()!=null, dlvReservation.getOrderId());
			if(FDStoreProperties.isDynamicRoutingEnabled()) {
				if(dlvReservation.getUnassignedActivityType()==null ||RoutingActivityType.RESERVE_TIMESLOT.equals(dlvReservation.getUnassignedActivityType()))
					RoutingUtil.getInstance().sendTimeslotReservationRequest(reservation,address);
			}
			return reservation;

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public FDReservation extendReservation(FDReservation reservation) throws FDResourceException {

		try {
			Calendar newExpTime = Calendar.getInstance();
			newExpTime.setTime(reservation.getExpirationDateTime());
			newExpTime.add(Calendar.HOUR, 1);
			DlvManagerSB sb = getDlvManagerHome().create();

			sb.extendReservation(reservation.getPK().getId(), newExpTime.getTime());

			return new FDReservation(reservation.getPK(), reservation.getTimeslot(), newExpTime.getTime(), reservation
				.getReservationType(), reservation.getCustomerId(), reservation.getAddressId(), reservation.isChefsTable()
				,reservation.isUnassigned()
				, reservation.getOrderId());

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public void removeReservation(String reservationId,ContactAddressModel address) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			sb.removeReservation(reservationId);
			if(FDStoreProperties.isDynamicRoutingEnabled()) {
				DlvReservationModel reservation=sb.getReservation(reservationId);
				if(reservation.getUnassignedActivityType()==null ||RoutingActivityType.CANCEL_TIMESLOT.equals(reservation.getUnassignedActivityType())) {
					RoutingUtil.getInstance().sendReleaseReservationRequest(reservation,address);
				} else {
					sb.clearUnassignedInfo(reservationId);
				}
			}

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (FinderException e) {
			throw new FDResourceException(e);
		}
	}

	public List getResevervationsForCustomer(String customerId) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			List reservations = sb.getReservationsForCustomer(customerId);

			if (reservations.isEmpty()) {
				return Collections.EMPTY_LIST;
			}

			List rsvLst = new ArrayList();
			for (Iterator i = reservations.iterator(); i.hasNext();) {
				DlvReservationModel dlvRsv = (DlvReservationModel) i.next();
				FDTimeslot timeslot = this.getTimeslotsById(dlvRsv.getTimeslotId());
				rsvLst.add(new FDReservation(
					dlvRsv.getPK(),
					timeslot,
					dlvRsv.getExpirationDateTime(),
					dlvRsv.getReservationType(),
					dlvRsv.getCustomerId(),
					dlvRsv.getAddressId(),
					dlvRsv.isChefsTable(),
					dlvRsv.isUnassigned(), dlvRsv.getOrderId()));
			}
			return rsvLst;

		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}
	}

	public void commitReservation(String rsvId, String customerId, String orderId, ContactAddressModel address) throws ReservationException, FDResourceException {
		try {
			
			DlvManagerSB sb = getDlvManagerHome().create();
			DlvReservationModel oldReserve = sb.getReservation(rsvId);
			System.out.println("oldReserve >>"+oldReserve.getOrderId());
			sb.commitReservation(rsvId, customerId, orderId);
			if(FDStoreProperties.isDynamicRoutingEnabled()) {
				DlvReservationModel reservation=sb.getReservation(rsvId);
				if(reservation.getUnassignedActivityType()==null ||RoutingActivityType.CONFIRM_TIMESLOT.equals(reservation.getUnassignedActivityType()))
					RoutingUtil.getInstance().sendCommitReservationRequest(reservation, address, oldReserve.getOrderId());
			}
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}catch (FinderException e) {
			throw new FDResourceException(e);
		}

	}

	public boolean releaseReservation(String rsvId, ContactAddressModel address) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			 boolean status=sb.releaseReservation(rsvId);
			 if(FDStoreProperties.isDynamicRoutingEnabled()) {
					DlvReservationModel reservation=sb.getReservation(rsvId);
					if(reservation.getUnassignedActivityType()==null ||RoutingActivityType.CANCEL_TIMESLOT.equals(reservation.getUnassignedActivityType()))
						RoutingUtil.getInstance().sendReleaseReservationRequest(reservation,address);
			 }
			 return status;
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (FinderException e) {
			throw new FDResourceException(e);
		}
	}

	public FDReservation getReservation(String rsvId) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			DlvReservationModel dlvRsv = sb.getReservation(rsvId);
			FDTimeslot timeslot = this.getTimeslotsById(dlvRsv.getTimeslotId());

			FDReservation fdRes = new FDReservation(dlvRsv.getPK(), timeslot, dlvRsv.getExpirationDateTime(), dlvRsv
				.getReservationType(), dlvRsv.getCustomerId(), dlvRsv.getAddressId(), dlvRsv.isChefsTable(),dlvRsv.isUnassigned()
				, dlvRsv.getOrderId());

			return fdRes;
		} catch (ObjectNotFoundException ex) {
			return null;
		} catch (FinderException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public DlvZoneModel findZoneById(String zoneId) throws FDResourceException, FDZoneNotFoundException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.findZoneById(zoneId);
		} catch (FinderException fe) {
			throw new FDZoneNotFoundException(fe);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
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
	}

	public List findApartmentRanges(AddressModel address) throws FDResourceException, InvalidAddressException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.findApartmentRanges(address);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public void addExceptionAddress(ExceptionAddress ex) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			sb.addExceptionAddress(ex);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}
	
	public List searchExceptionAddresses(ExceptionAddress ex) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.searchExceptionAddresses(ex);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}
	
	public void deleteAddressException(String id) throws FDResourceException {
		try{
			DlvManagerSB sb = getDlvManagerHome().create();
			sb.deleteAddressException(id);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}
	
	public String getCounty(String city, String state) throws FDResourceException{
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.getCounty(city, state);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
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
	
	
	public List getCountiesByState(String stateAbbrev) throws FDResourceException{
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.getCountiesByState(stateAbbrev);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	private DlvManagerHome getDlvManagerHome() {
		try {
			return (DlvManagerHome) serviceLocator.getRemoteHome(FDStoreProperties.getDeliveryManagerHome(), DlvManagerHome.class);
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}

	
	public List searchGeocodeException(ExceptionAddress ex) throws FDResourceException {

		DlvManagerSB sb;
		try {
			sb = getDlvManagerHome().create();
			return sb.searchGeocodeException(ex);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}

	}
	
	public void deleteGeocodeException(ExceptionAddress ex) throws FDResourceException {

		DlvManagerSB sb;
		try {
			sb = getDlvManagerHome().create();
			sb.deleteGeocodeException(ex);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}

	}

	public void addGeocodeException(ExceptionAddress ex, String userId) throws FDResourceException {
		DlvManagerSB sb;
		try {
			sb = getDlvManagerHome().create();
			sb.addGeocodeException(ex, userId);
		} catch (RemoteException e) {						
			throw new FDResourceException(e, e.getMessage());
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}

	}
	
	/* municipality info stuff.. not sure if it should really live here, but its a start  */
	private MunicipalityInfoWrapper municipalityInfos = null;
	private long MUNI_REFRESH_PERIOD = 1000 * 60 * 5; // 5 minutes
	private long muni_lastRefresh = 0;

	public MunicipalityInfoWrapper getMunicipalityInfos() throws FDResourceException {
		this.refreshMunicipalityInfos();
		return this.municipalityInfos;
	}
	private void refreshMunicipalityInfos() throws FDResourceException {
		if (System.currentTimeMillis() - muni_lastRefresh > MUNI_REFRESH_PERIOD) {
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
	
	private Map stateCountyByZip = new HashMap();
	private StateCounty lookupStateCountyByZip(String zipcode) throws FDResourceException{		
		try {
			StateCounty sc = (StateCounty) stateCountyByZip.get(zipcode);
			if(sc == null){
				DlvManagerSB sb = getDlvManagerHome().create();
				sc = sb.lookupStateCountyByZip(zipcode);
				if(sc != null){
					stateCountyByZip.put(zipcode, sc);
				}
			}
			return sc;
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
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

	public List getCutofftimesByDate(Date day) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.getCutoffTimesByDate(day);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}
	}
	
	public List<java.util.List<IDeliverySlot>> getTimeslotsForDateRangeAndZoneEx(List<FDTimeslot> timeSlots, ContactAddressModel address) throws FDResourceException {
		try {
			
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.getTimeslotForDateRangeAndZoneEx(timeSlots, address);
		
		} catch (RemoteException re) {
			re.printStackTrace();
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			ce.printStackTrace();
			throw new FDResourceException(ce);
		} 
	}

	public IDeliveryReservation reserveTimeslotEx(FDReservation reservation,ContactAddressModel address) throws FDResourceException {

			try {
				
				
				DlvManagerSB sb = getDlvManagerHome().create();
				return sb.reserveTimeslotEx(reservation, address);

			} catch (RemoteException re) {
				throw new FDResourceException(re);
			} catch (CreateException ce) {
				throw new FDResourceException(ce);
			} 
		}	

	public void commitReservationEx(DlvReservationModel reservation,ContactAddressModel address, String previousOrderId) throws FDResourceException{
		try {
			
			
			DlvManagerSB sb = getDlvManagerHome().create();
			sb.commitReservationEx(reservation,address, previousOrderId);

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} 
	}
	
	public void releaseReservationEx(DlvReservationModel reservation,ContactAddressModel address) throws FDResourceException{
		try {
			
			
			DlvManagerSB sb = getDlvManagerHome().create();
			sb.releaseReservationEx(reservation,address);

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} 
	}
	
	
	public List<DlvReservationModel> getUnassignedReservations() throws FDResourceException {
		
		try {
			
			
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.getUnassignedReservations();
			
			

		} catch (RemoteException re) {
			re.printStackTrace();
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (DlvResourceException e) {
			throw new FDResourceException(e);
		} 
	}
	
	public void setUnassignedInfo(String reservationId,RoutingActivityType activity )throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			sb.setUnassignedInfo(reservationId, activity);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	public void clearUnassignedInfo(String reservationId ) throws FDResourceException {
		try {
			DlvManagerSB sb = getDlvManagerHome().create();
			sb.clearUnassignedInfo(reservationId);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	/*public List<DlvReservationModel> getUnassignedReservations() throws FDResourceException {
		
		try {
			
			
			DlvManagerSB sb = getDlvManagerHome().create();
			return sb.getUnassignedReservations();
			

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (DlvResourceException e) {
			throw new FDResourceException(e);
		} 
	}*/
	
	
	
	/*public boolean makeRecurringReservation(String customerId, int dayOfWeek, Date startTime, Date endTime, AddressModel address) {
		try {
			Date startDate = getDateByNextDayOfWeek(dayOfWeek);
			Date endDate = DateUtil.addDays(startDate, 1);

			List lst = this.getTimeslotForDateRangeAndZone(startDate, endDate, address);
			List messages = new ArrayList();
			
			try {
				if(lst != null) {
					List geographicRestrictions = this.getGeographicDlvRestrictions( address);
					
					if(geographicRestrictions != null && geographicRestrictions.size() > 0) {
						for(Iterator i = lst.iterator(); i.hasNext();) {						
							FDTimeslot ts = new FDTimeslot((DlvTimeslotModel)i.next());
							if (GeographyRestriction.isTimeSlotGeoRestricted(geographicRestrictions, ts, messages, null)) {
								// filter off empty timeslots (unless they must be retained)
								i.remove();
							}
						}
					}
				}
			} catch (DlvResourceException dlvResException) {
				LOGGER.info("Failed to load Geography Restriction "+messages);
			}
			
			DlvTimeslotModel foundTimeslot = null;
			for (Iterator i = lst.iterator(); i.hasNext();) {
				DlvTimeslotModel t = (DlvTimeslotModel) i.next();
				if (t.isMatching(startDate, startTime, endTime)) {
					foundTimeslot = t;
					break;
				}
			}

			if (foundTimeslot == null) {
				logActivity(EnumTransactionSource.SYSTEM, EnumAccountActivityType.MAKE_PRE_RESERVATION,"SYSTEM", customerId,
								"Failed to make recurring reservation for customer - no timeslot found");
				LOGGER.info("Failed to make recurring reservation for customer " + customerId + " - no timeslot found");
				return false;
			}
			
			List reservations = getAllReservationsByCustomerAndTimeslot(customerId, foundTimeslot.getPK().getId());
			for (Iterator i = reservations.iterator(); i.hasNext(); ) {
				DlvReservationModel rsv = (DlvReservationModel) i.next();
				if (!EnumReservationType.STANDARD_RESERVATION.equals(rsv.getReservationType())) {
					logActivity(EnumTransactionSource.SYSTEM, EnumAccountActivityType.MAKE_PRE_RESERVATION,"SYSTEM", customerId,
									"Failed to make recurring reservation for customer - already reserved");
					LOGGER.info("Failed to make recurring reservation for customer " + customerId + " - already reserved");
					return false;
				}
			}

			long duration = foundTimeslot.getCutoffTimeAsDate().getTime()
				- System.currentTimeMillis()
				- (FDStoreProperties.getPreReserveHours() * DateUtil.HOUR);
			if (duration < 0) {
				duration = Math.min(foundTimeslot.getCutoffTimeAsDate().getTime() - System.currentTimeMillis(), DateUtil.HOUR);
			}
			//recurring timeslot should not go to chefs table capacity
			this.reserveTimeslot(
				foundTimeslot.getPK().getId(),
				customerId,
				duration,
				EnumReservationType.RECURRING_RESERVATION,
				address.getPK().getId(), false);
			
			logActivity(EnumTransactionSource.SYSTEM, EnumAccountActivityType.MAKE_PRE_RESERVATION,"SYSTEM", customerId,
								"Made recurring reservation");
			
			LOGGER.info("Made recurring reservation for " + customerId);

			return true;
			
		} catch (Exception e) {
			
			LOGGER.warn("Could not Reserve a Weekly recurring timeslot for customer id: "+customerId, e);
			return false;
		} 
	}*/
	
}
