package com.freshdirect.delivery.ejb;


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

/**
 *@deprecated Please use the DlvManagerController and DlvManagerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface DlvManagerSB extends EJBObject {
	@Deprecated public void saveFutureZoneNotification(String email, String zip, String serviceType) throws RemoteException;
	
	@Deprecated public List<SiteAnnouncement> getSiteAnnouncements() throws FDResourceException, RemoteException;
	@Deprecated public void logFailedFdxOrder(String orderId) throws FDResourceException, RemoteException;
	@Deprecated public List<MunicipalityInfo> getMunicipalityInfos() throws RemoteException;
	@Deprecated public void sendOrderSizeFeed() throws FDResourceException, RemoteException;
	@Deprecated public void sendLateOrderFeed() throws FDResourceException, RemoteException;
	@Deprecated public FDReservation reserveTimeslot(String timeslotId, String customerId,
			EnumReservationType type, Customer customer,
			boolean chefsTable, String ctDeliveryProfile, boolean isForced,
			TimeslotEvent event, boolean hasSteeringDiscount, String deliveryFeeTier) throws RemoteException, ReservationException, FDResourceException;
	@Deprecated public void commitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1,
			TimeslotEvent event) throws RemoteException, ReservationException, FDResourceException;
	@Deprecated public Set<StateCounty> getCountiesByState(String state) throws RemoteException, FDResourceException;
	@Deprecated public StateCounty lookupStateCountyByZip(String zipcode) throws RemoteException, FDResourceException;
	@Deprecated public ZipCodeAttributes lookupZipCodeAttributes(String zipcode) throws RemoteException, FDResourceException;
	@Deprecated public Map<String, DeliveryException> getCartonScanInfo() throws RemoteException, FDResourceException;
	@Deprecated public void queryForMissingFdxOrders() throws RemoteException;

	@Deprecated public int unlockInModifyOrders() throws RemoteException;
	
	@Deprecated public void recommitReservation(String rsvId, String customerId, OrderContext context,ContactAddressModel address,boolean pr1) throws RemoteException, ReservationException, FDResourceException;
}   
