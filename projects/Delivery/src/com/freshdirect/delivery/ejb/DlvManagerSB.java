/*
 * DlvManagerSB.java
 *
 * Created on August 27, 2001, 7:00 PM
 */

package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumServiceType;
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
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.restriction.GeographyRestriction;
import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.StateCounty;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;

public interface DlvManagerSB extends EJBObject {
	
    public List<DlvTimeslotModel> getTimeslotForDateRangeAndZone(Date begDate, Date endDate, AddressModel address) throws InvalidAddressException, RemoteException;
	public List getTimeslotsForDepot(java.util.Date startDate, java.util.Date endDate, String regionId, String zoneCode) throws DlvResourceException , RemoteException;
	public List getAllTimeslotsForDateRange(java.util.Date startDate, java.util.Date endDate, AddressModel address) throws InvalidAddressException, RemoteException;

	/** @return List of DlvTimeslotCapacityInfo */
	public List getTimeslotCapacityInfo(java.util.Date date) throws RemoteException;

	public DlvReservationModel reserveTimeslot(DlvTimeslotModel dlvTimeslot, String customerId, long holdTime, EnumReservationType type, String addressId, boolean chefsTable,String ctDeliveryProfile) throws ReservationException, RemoteException;
    public void commitReservation(String rsvId, String customerId, String orderId,boolean pr1) throws ReservationException, RemoteException;
    public DlvAddressVerificationResponse scrubAddress(AddressModel address) throws RemoteException;
    public DlvAddressVerificationResponse scrubAddress(AddressModel address, boolean useApartment) throws RemoteException;
    public void addApartment(AddressModel address) throws RemoteException, InvalidAddressException;
    public boolean releaseReservation(String rsvId) throws FinderException, RemoteException;
    public DlvZoneModel findZoneById(String zoneId) throws FinderException, RemoteException;
	public DlvReservationModel getReservation(String reservationId) throws FinderException, RemoteException;
	
    public DlvTimeslotModel getTimeslotById(String timeslotId) throws FinderException, RemoteException;
	public List getAllZonesByRegion(String regionId)throws RemoteException;
	public DlvZoneInfoModel getZoneInfo(AddressModel address, Date date) throws InvalidAddressException, RemoteException;
	public List getCutoffInfo(String zoneCode, Date day) throws RemoteException;
	public DlvZoneCapacityInfo getZoneCapacity(String zoneCode, Date day) throws RemoteException;
	public void saveFutureZoneNotification(String email, String zip, String serviceType) throws RemoteException;
    
    public List getDeliverableZipCodes(EnumServiceType serviceType) throws RemoteException;
    public DlvServiceSelectionResult checkServicesForZipCode(String zipCode) throws RemoteException;
    public DlvServiceSelectionResult checkServicesForAddress(AddressModel address) throws InvalidAddressException, RemoteException;
    
    public ArrayList findSuggestionsForAmbiguousAddress(AddressModel address) throws InvalidAddressException, RemoteException;
    public DlvZoneInfoModel getZoneInfoForDepot(String regionId, String zoneCode, Date date) throws DlvResourceException, RemoteException;
    
    public Collection getZonesForRegionId(String regionId) throws DlvResourceException, RemoteException;
    
    public Collection getAllRegions() throws DlvResourceException, RemoteException;
    
    public DlvAddressGeocodeResponse geocodeAddress(AddressModel address) throws InvalidAddressException, RemoteException;
    
    public List findApartmentRanges(AddressModel address) throws InvalidAddressException, RemoteException;
    
    public boolean checkForAlcoholDelivery(String scrubbedAddress, String zipcode) throws RemoteException;
    
    public EnumRestrictedAddressReason checkAddressForRestrictions(AddressModel address) throws RemoteException;
    
    public List getDlvRestrictions() throws DlvResourceException, RemoteException;
    
    public List getGeographicDlvRestrictions(AddressModel address)throws DlvResourceException, RemoteException;
    
	public List getSiteAnnouncements() throws DlvResourceException, RemoteException;
	
	public List getReservationsForCustomer(String customerId) throws RemoteException;
	
	public void extendReservation(String rsvId, Date newExpTime) throws RemoteException;
	
	public void removeReservation(String reservationId) throws RemoteException;
	
	public boolean makeRecurringReservation(String customerId, int dayOfWeek, Date startTime, Date endTime, ContactAddressModel address) throws RemoteException;
	
	public void addExceptionAddress(ExceptionAddress ex) throws RemoteException;

	public void addGeocodeException(ExceptionAddress ex, String userId) throws RemoteException;
	
	public List searchGeocodeException(ExceptionAddress ex) throws RemoteException;
	
	public void deleteGeocodeException(ExceptionAddress ex) throws RemoteException;
	
	public String getCounty(String city, String state) throws RemoteException;
	
	public List getCountiesByState(String stateAbbrev) throws RemoteException;
	
	public List getMunicipalityInfos() throws RemoteException;

	public List searchExceptionAddresses(ExceptionAddress ex) throws RemoteException;
	
	public void deleteAddressException(String id) throws RemoteException;

	public StateCounty lookupStateCountyByZip(String zipcode) throws RemoteException;

	public List getCutoffTimesByDate(Date day) throws RemoteException;
	
	public List<java.util.List<IDeliverySlot>> getTimeslotForDateRangeAndZoneEx(List<FDTimeslot> timeSlots, ContactAddressModel address) throws RemoteException;
	
	public IDeliveryReservation reserveTimeslotEx(DlvReservationModel reservation,ContactAddressModel address , FDTimeslot timeslot) throws RemoteException;
	
	public void commitReservationEx(DlvReservationModel reservation,ContactAddressModel address, String previousOrderId) throws  RemoteException;
	
	public void releaseReservationEx(DlvReservationModel reservation,ContactAddressModel address) throws  RemoteException;
	
	public List<DlvReservationModel> getUnassignedReservations(Date _date) throws DlvResourceException, RemoteException;
	
	public void setUnassignedInfo(String reservationId,RoutingActivityType activity )throws  RemoteException;
	
	public void clearUnassignedInfo(String reservationId )throws  RemoteException;
	
	public List<DlvReservationModel> getExpiredReservations() throws DlvResourceException,RemoteException;
	
	public void expireReservations() throws DlvResourceException,RemoteException;
	
	public void updateReservationEx(DlvReservationModel reservation,ContactAddressModel address)throws  RemoteException;
	
}   
