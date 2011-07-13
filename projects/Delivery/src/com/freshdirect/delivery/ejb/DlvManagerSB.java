package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvAddressVerificationResponse;
import com.freshdirect.delivery.DlvApartmentRange;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.DlvTimeslotCapacityInfo;
import com.freshdirect.delivery.DlvZipInfoModel;
import com.freshdirect.delivery.DlvZoneCapacityInfo;
import com.freshdirect.delivery.DlvZoneCutoffInfo;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.ExceptionAddress;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.announcement.SiteAnnouncement;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.analytics.EventType;
import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.delivery.restriction.GeographyRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.fdstore.FDDynamicTimeslotList;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.StateCounty;
import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingNotificationModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public interface DlvManagerSB extends EJBObject {
	
    public List<DlvTimeslotModel> getTimeslotForDateRangeAndZone(Date begDate, Date endDate, AddressModel address) throws InvalidAddressException, RemoteException;
	public List<DlvTimeslotModel> getTimeslotsForDepot(java.util.Date startDate, java.util.Date endDate, String regionId, String zoneCode) throws DlvResourceException , RemoteException;
	public List<DlvTimeslotModel> getAllTimeslotsForDateRange(java.util.Date startDate, java.util.Date endDate, AddressModel address) throws InvalidAddressException, RemoteException;

	public List<DlvTimeslotCapacityInfo> getTimeslotCapacityInfo(java.util.Date date) throws RemoteException;

	public DlvReservationModel reserveTimeslot(DlvTimeslotModel dlvTimeslot, String customerId, long holdTime, EnumReservationType type, ContactAddressModel addressId, boolean chefsTable,String ctDeliveryProfile,boolean isForced, TimeslotEventModel event) throws ReservationException, RemoteException;
    public void commitReservation(String rsvId, String customerId, String orderId,boolean pr1) throws ReservationException, RemoteException;
    public DlvAddressVerificationResponse scrubAddress(AddressModel address) throws RemoteException;
    public DlvAddressVerificationResponse scrubAddress(AddressModel address, boolean useApartment) throws RemoteException;
    public void addApartment(AddressModel address) throws RemoteException, InvalidAddressException;
    public boolean releaseReservation(String rsvId) throws FinderException, RemoteException;
    public DlvZoneModel findZoneById(String zoneId) throws FinderException, RemoteException;
	public DlvReservationModel getReservation(String reservationId) throws FinderException, RemoteException;
	
    public DlvTimeslotModel getTimeslotById(String timeslotId) throws FinderException, RemoteException;
	public List<DlvZoneModel> getAllZonesByRegion(String regionId)throws RemoteException;
	public DlvZoneInfoModel getZoneInfo(AddressModel address, Date date) throws InvalidAddressException, RemoteException;
	public List<DlvZoneCutoffInfo> getCutoffInfo(String zoneCode, Date day) throws RemoteException;
	public DlvZoneCapacityInfo getZoneCapacity(String zoneCode, Date day) throws RemoteException;
	public void saveFutureZoneNotification(String email, String zip, String serviceType) throws RemoteException;
    
    public List<DlvZipInfoModel> getDeliverableZipCodes(EnumServiceType serviceType) throws RemoteException;
    public DlvServiceSelectionResult checkServicesForZipCode(String zipCode) throws RemoteException;
    public DlvServiceSelectionResult checkServicesForAddress(AddressModel address) throws InvalidAddressException, RemoteException;
    
    public List<AddressModel> findSuggestionsForAmbiguousAddress(AddressModel address) throws InvalidAddressException, RemoteException;
    public DlvZoneInfoModel getZoneInfoForDepot(String regionId, String zoneCode, Date date) throws DlvResourceException, RemoteException;
    
    public Collection<DlvZoneModel> getZonesForRegionId(String regionId) throws DlvResourceException, RemoteException;
    
    public Collection<DlvRegionModel> getAllRegions() throws DlvResourceException, RemoteException;
    
    public DlvAddressGeocodeResponse geocodeAddress(AddressModel address) throws InvalidAddressException, RemoteException;
    
    public List<DlvApartmentRange> findApartmentRanges(AddressModel address) throws InvalidAddressException, RemoteException;
    
    public boolean checkForAlcoholDelivery(String scrubbedAddress, String zipcode) throws RemoteException;
    
    public EnumRestrictedAddressReason checkAddressForRestrictions(AddressModel address) throws RemoteException;
    
    public List<RestrictionI> getDlvRestrictions() throws DlvResourceException, RemoteException;
    
    public List<GeographyRestriction> getGeographicDlvRestrictions(AddressModel address)throws DlvResourceException, RemoteException;
    
	public List<SiteAnnouncement> getSiteAnnouncements() throws DlvResourceException, RemoteException;
	
	public List<DlvReservationModel> getReservationsForCustomer(String customerId) throws RemoteException;
	
	public void extendReservation(String rsvId, Date newExpTime) throws RemoteException;
	
	public void removeReservation(String reservationId) throws RemoteException;
	
	public boolean makeRecurringReservation(String customerId, int dayOfWeek, Date startTime, Date endTime, ContactAddressModel address, boolean chefstable, TimeslotEventModel event) throws RemoteException;
	
	public void addExceptionAddress(ExceptionAddress ex) throws RemoteException;

	public void addGeocodeException(ExceptionAddress ex, String userId) throws RemoteException;
	
	public List<ExceptionAddress> searchGeocodeException(ExceptionAddress ex) throws RemoteException;
	
	public void deleteGeocodeException(ExceptionAddress ex) throws RemoteException;
	
	public String getCounty(String city, String state) throws RemoteException;
	
	public List<String> getCountiesByState(String stateAbbrev) throws RemoteException;
	
	public List<MunicipalityInfo> getMunicipalityInfos() throws RemoteException;

	public List<ExceptionAddress> searchExceptionAddresses(ExceptionAddress ex) throws RemoteException;
	
	public void deleteAddressException(String id) throws RemoteException;

	public StateCounty lookupStateCountyByZip(String zipcode) throws RemoteException;

	public List<Date> getCutoffTimesByDate(Date day) throws RemoteException;
	
	public FDDynamicTimeslotList getTimeslotForDateRangeAndZoneEx(List<FDTimeslot> timeSlots,TimeslotEventModel event, ContactAddressModel address) throws RemoteException;
	
	public IDeliveryReservation reserveTimeslotEx(DlvReservationModel reservation,ContactAddressModel address , FDTimeslot timeslot, TimeslotEventModel event) throws RemoteException;
	
	public void commitReservationEx(DlvReservationModel reservation,ContactAddressModel address, TimeslotEventModel event) throws  RemoteException;
	
	public void releaseReservationEx(DlvReservationModel reservation,ContactAddressModel address, TimeslotEventModel event) throws  RemoteException;
	
	public List<DlvReservationModel> getUnassignedReservations(Date _date) throws DlvResourceException, RemoteException;
	
	List<DlvReservationModel> getReRouteReservations() throws DlvResourceException, RemoteException;
	
	void clearReRouteReservations() throws DlvResourceException, RemoteException;
	
	public void setUnassignedInfo(String reservationId,RoutingActivityType activity )throws  RemoteException;
	
	public void clearUnassignedInfo(String reservationId )throws  RemoteException;
	
	public List<DlvReservationModel> getExpiredReservations() throws DlvResourceException,RemoteException;
	
	public void expireReservations() throws DlvResourceException,RemoteException;
	
	void updateReservationEx(DlvReservationModel reservation, ContactAddressModel address, FDTimeslot timeslot) throws  RemoteException;
	
	void updateReservationStatus(DlvReservationModel reservation, ContactAddressModel address, String erpOrderId) throws  RemoteException;
	
	List getTimeslotsForDate(java.util.Date startDate) throws DlvResourceException, RemoteException;
	
	List<IDeliveryWindowMetrics> retrieveCapacityMetrics(IRoutingSchedulerIdentity schedulerId, List<IDeliverySlot> slots, boolean purge) 
																throws DlvResourceException, RemoteException;
	
	int updateTimeslotsCapacity(List<DlvTimeslotModel> dlvTimeSlots ) throws DlvResourceException, RemoteException;
	
	List<IRoutingNotificationModel> retrieveNotifications() throws DlvResourceException, RemoteException;
	
	void processCancelNotifications(List<IRoutingNotificationModel> notifications, List<IRoutingNotificationModel> unUsedNotifications) throws DlvResourceException, RemoteException;
	
	List getActiveZoneCodes() throws RemoteException;
	List<DlvZoneModel> getActiveZones() throws RemoteException;
	
	void cancelRoutingReservation(DlvReservationModel reservation, ContactAddressModel address, TimeslotEventModel event) throws RemoteException;
	
	void setReservationMetricsStatus(String reservationId, EnumRoutingUpdateStatus status) throws DlvResourceException, RemoteException;
	void setReservationMetricsDetails(String reservationId, long noOfCartons, long noOfCases, long noOfFreezers
												, EnumRoutingUpdateStatus status) throws DlvResourceException, RemoteException;
	void setReservationReservedMetrics(String reservationId, double orderSize, double serviceTime
												, EnumRoutingUpdateStatus status)   throws RemoteException;
	void setReservationMetricsDetails(String reservationId, long noOfCartons, long noOfCases
												, long noOfFreezers, EnumOrderMetricsSource source)   throws RemoteException;
	void setReservationReservedMetrics(String reservationId, double orderSize, double serviceTime)  throws RemoteException;
	void setReservationMetricsDetails(String reservationId, long noOfCartons, long noOfCases, long noOfFreezers, EnumRoutingUpdateStatus status
												, EnumOrderMetricsSource source)  throws RemoteException;
	
	List<GeographyRestriction> getGeographicDlvRestrictionsForReservation(AddressModel address)throws DlvResourceException, RemoteException;
	List<GeographyRestriction> getGeographicDlvRestrictionsForAvailable(AddressModel address)throws DlvResourceException, RemoteException;
	Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> retrieveWaveInstanceTree(Date deliveryDate, EnumWaveInstanceStatus status) throws DlvResourceException, RemoteException;
	void synchronizeWaveInstance(IRoutingSchedulerIdentity schedulerId
			, Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> waveInstanceTree
			, Set<String> inSyncZones) throws DlvResourceException, RemoteException;
	List<Date> getFutureTimeslotDates() throws DlvResourceException, RemoteException;
	void purgeSchedulerByIdentity(IRoutingSchedulerIdentity schedulerId) throws DlvResourceException, RemoteException;
	
	Set<String> getInSyncWaveInstanceZones(Date deliveryDate) throws DlvResourceException, RemoteException;
	
	public void logTimeslots(DlvReservationModel reservation,IOrderModel order,
			EventType eventType,TimeslotEventModel event, 
			int responseTime, ContactAddressModel address) throws RemoteException;
	
	public void logTimeslots(DlvReservationModel reservation, IOrderModel order, 
			List<FDTimeslot> timeSlots,TimeslotEventModel event, 
			ContactAddressModel address, int responseTime) throws RemoteException;
	
	void fixDisassociatedTimeslots()throws RemoteException;
}   
