package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.announcement.SiteAnnouncement;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.OrderContext;

public interface DlvManagerSB extends EJBObject {

	public void saveFutureZoneNotification(String email, String zip, String serviceType) throws RemoteException;
    public List<SiteAnnouncement> getSiteAnnouncements() throws FDResourceException, RemoteException;
	public List<MunicipalityInfo> getMunicipalityInfos() throws RemoteException;
	public void sendOrderSizeFeed() throws FDResourceException, RemoteException;
	public void sendLateOrderFeed() throws FDResourceException, RemoteException;
	public FDReservation reserveTimeslot(String timeslotId, String customerId,
			EnumReservationType type, ContactAddressModel address,
			boolean chefsTable, String ctDeliveryProfile, boolean isForced,
			TimeslotEvent event, boolean hasSteeringDiscount) throws RemoteException, ReservationException, FDResourceException;
	public void commitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1,
			TimeslotEvent event) throws RemoteException, ReservationException, FDResourceException;
	
}   
