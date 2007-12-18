/*
 * DlvTemplateManagerSB.java
 *
 * Created on November 27, 2001, 11:05 AM
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

import javax.ejb.EJBObject;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.admin.DlvHistoricTimeslotData;
import com.freshdirect.delivery.model.DlvRegionModel;

public interface DlvTemplateManagerSB extends EJBObject {
	
	public DlvRegionModel getRegion(String name) throws DlvResourceException, RemoteException;
	public Collection getRegions() throws DlvResourceException, RemoteException;
	public DlvRegionModel getRegion(String name, Date startDate) throws DlvResourceException, RemoteException;
	public ArrayList getTimeslotForDateRangeAndZone(Date startDate, Date endDate, Date curTime, String zoneCode) throws DlvResourceException, RemoteException;
	public DlvHistoricTimeslotData getTimeslotForDateRangeAndRegion(Date startDate, Date endDate, String regionId) throws DlvResourceException, RemoteException; 
}

