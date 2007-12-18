/*
 * 
 * DlvDepotManagerSB.java
 * Date: Jul 29, 2002 Time: 7:48:15 PM
 */
package com.freshdirect.delivery.depot.ejb;

/**
 * 
 * @author knadeem
 */
import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;

public interface DlvDepotManagerSB extends EJBObject {
	
	public Collection getDepots() throws DlvResourceException, RemoteException;
	
	public DlvDepotModel getDepot(String name) throws DlvResourceException, RemoteException;
	
	public boolean checkAccessCode(String name, String accessCode) throws DlvResourceException, RemoteException;
	
	public DlvLocationModel getDepotLocation(String depotCode, String locationId) throws DlvResourceException, RemoteException;
	
	public void addDepotLocation(String depotId, DlvLocationModel location) throws DlvResourceException, RemoteException;
	
	public DlvLocationModel getLocationForDepotId(String depotId, String locationId) throws DlvResourceException, RemoteException; 
	
	public void updateDepotLocation(String depotId, DlvLocationModel location) throws DlvResourceException, RemoteException;
	
	public void deleteDepotLocation(String depotId, String locationId) throws DlvResourceException, RemoteException;
	
	public void addNewDepot(DlvDepotModel model) throws DlvResourceException, RemoteException;
	
	public void updateDepot(String depotId, DlvDepotModel depotModel) throws DlvResourceException, RemoteException;
	
	public void deleteDepot(String depotId) throws DlvResourceException, RemoteException;
	
	public Collection getZonesForRegionId(String regionId) throws DlvResourceException, RemoteException;
	
	public Collection getAllRegions() throws DlvResourceException, RemoteException;

}
