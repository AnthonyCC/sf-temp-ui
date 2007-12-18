/*
 * 
 * DlvDepotEB.java
 * Date: Jul 23, 2002 Time: 8:22:05 PM
 */
package com.freshdirect.delivery.depot.ejb;

/**
 * 
 * @author knadeem
 */
import java.rmi.RemoteException;

import com.freshdirect.framework.core.*;
import com.freshdirect.delivery.depot.*;

public interface DlvDepotEB extends EntityBeanRemoteI{
	
	public ModelI getModel(java.util.Date effectiveDate) throws RemoteException;
	
	public void addLocation(DlvLocationModel location) throws RemoteException;
	
	public void updateLocation(DlvLocationModel location) throws RemoteException;
	
	public void updateFromModel(DlvDepotModel depotModel) throws RemoteException;
	
	public void deleteLocation(String locationId) throws RemoteException;

}
