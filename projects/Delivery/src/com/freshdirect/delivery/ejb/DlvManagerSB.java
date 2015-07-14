package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.delivery.announcement.SiteAnnouncement;
import com.freshdirect.fdstore.FDResourceException;

public interface DlvManagerSB extends EJBObject {

	public void saveFutureZoneNotification(String email, String zip, String serviceType) throws RemoteException;
    public List<SiteAnnouncement> getSiteAnnouncements() throws FDResourceException, RemoteException;
	public List<MunicipalityInfo> getMunicipalityInfos() throws RemoteException;
	public void sendOrderSizeFeed() throws FDResourceException, RemoteException;
	public void sendLateOrderFeed() throws FDResourceException, RemoteException;
	
}   
