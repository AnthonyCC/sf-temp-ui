package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBObject;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.announcement.SiteAnnouncement;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.dto.Customer;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.OrderContext;
import com.freshdirect.logistics.fdstore.StateCounty;
import com.freshdirect.logistics.fdstore.ZipCodeAttributes;

public interface DlvManagerSB extends EJBObject {

	public void saveFutureZoneNotification(String email, String zip, String serviceType) throws RemoteException;
    public List<SiteAnnouncement> getSiteAnnouncements() throws FDResourceException, RemoteException;
    public void logFailedFdxOrder(String orderId) throws FDResourceException, RemoteException;
    public List<MunicipalityInfo> getMunicipalityInfos() throws RemoteException;
	public void sendOrderSizeFeed() throws FDResourceException, RemoteException;
	public void sendLateOrderFeed() throws FDResourceException, RemoteException;
	public FDReservation reserveTimeslot(String timeslotId, String customerId,
			EnumReservationType type, Customer customer,
			boolean chefsTable, String ctDeliveryProfile, boolean isForced,
			TimeslotEvent event, boolean hasSteeringDiscount, String deliveryFeeTier) throws RemoteException, ReservationException, FDResourceException;
	public void commitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1,
			TimeslotEvent event) throws RemoteException, ReservationException, FDResourceException;
	public Set<StateCounty> getCountiesByState(String state) throws RemoteException, FDResourceException;
	public StateCounty lookupStateCountyByZip(String zipcode) throws RemoteException, FDResourceException;
	public ZipCodeAttributes lookupZipCodeAttributes(String zipcode) throws RemoteException, FDResourceException;
	public Map<String, DeliveryException> getCartonScanInfo() throws RemoteException, FDResourceException;
	public void queryForMissingFdxOrders() throws RemoteException;

	public int unlockInModifyOrders() throws RemoteException;
	
	public void recommitReservation(String rsvId, String customerId, OrderContext context,ContactAddressModel address,boolean pr1) throws RemoteException, ReservationException, FDResourceException;
}   
