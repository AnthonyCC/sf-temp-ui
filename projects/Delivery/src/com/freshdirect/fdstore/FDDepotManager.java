/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.delivery.depot.ejb.DlvDepotManagerHome;
import com.freshdirect.delivery.depot.ejb.DlvDepotManagerSB;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.NVL;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDDepotManager {

	private static FDDepotManager instance = null;

	private final ServiceLocator serviceLocator;

	private Map depotMap = new HashMap();

	private List depotList = new ArrayList();
	private List pickupDepotList = new ArrayList();
	private List corpDepotList = new ArrayList();

	private long REFRESH_PERIOD = 1000 * 60 * 480; // 8 hours

	private long lastRefresh = 0;

	private FDDepotManager() throws NamingException {
		this.serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
	}

	public synchronized static FDDepotManager getInstance() throws FDResourceException {
		if (FDDepotManager.instance == null) {
			try {
				FDDepotManager.instance = new FDDepotManager();
			} catch (NamingException e) {
				throw new FDResourceException(e);
			}
		}
		return FDDepotManager.instance;
	}

	private synchronized void refreshCacheMaps() throws FDResourceException {
		if (System.currentTimeMillis() - lastRefresh > REFRESH_PERIOD) {
			try {
				DlvDepotManagerSB sb = this.getDlvDepotManagerHome().create();

				Collection depots = sb.getDepots();

				HashMap newDepotMap = new HashMap();
				List newDepotList = new ArrayList();
				List newPickupDepotList = new ArrayList();
				List newCorpDepotList = new ArrayList();

				for (Iterator dIter = depots.iterator(); dIter.hasNext();) {
					DlvDepotModel d = (DlvDepotModel) dIter.next();
					newDepotMap.put(d.getDepotCode(), d);
					if (d.isPickup()) {
						newPickupDepotList.add(d);
					} else if (d.isCorporateDepot()) {
						newCorpDepotList.add(d);
					} else {
						newDepotList.add(d);
					}
				}

				if ((newDepotMap != null) && !newDepotMap.isEmpty()) {
					depotMap = Collections.unmodifiableMap(newDepotMap);
					this.depotList = Collections.unmodifiableList(newDepotList);
					this.pickupDepotList = Collections.unmodifiableList(newPickupDepotList);
					this.corpDepotList = Collections.unmodifiableList(newCorpDepotList);
				}

				lastRefresh = System.currentTimeMillis();

			} catch (CreateException ce) {
				throw new FDResourceException("Cannot create SessionBean " + ce.getMessage());
			} catch (DlvResourceException de) {
				throw new FDResourceException(de.getMessage());
			} catch (RemoteException re) {
				throw new FDResourceException("Cannot talk to the SessionBean " + re.getMessage());
			}
		}
	}

	protected Map getDepotMap() throws FDResourceException {
		this.refreshCacheMaps();
		return this.depotMap;
	}

	/**
	 * @return a collection of all DlvDepotModels
	 */
	public Collection getDepots() throws FDResourceException {
		this.refreshCacheMaps();
		return this.depotList;
	}

	public Collection getPickupDepots() throws FDResourceException {
		this.refreshCacheMaps();
		return this.pickupDepotList;
	}

	public List getCorporateDepots() throws FDResourceException {
		this.refreshCacheMaps();
		return this.corpDepotList;
	}

	/**
	 * @return DlvDepotModel
	 */
	public DlvDepotModel getDepot(String depotCode) throws FDResourceException {
		return (DlvDepotModel) this.getDepotMap().get(depotCode);
	}

	public boolean checkAccessCode(String depotCode, String accessCode) throws FDResourceException {
		DlvDepotModel d = this.getDepot(depotCode);
		if (d == null)
			return false;
		else
			return d.getRegistrationCode().equalsIgnoreCase(accessCode) && !d.isDeactivated();
	}

	public String checkCorpAccessCode(String accessCode) throws FDResourceException {

		for (Iterator i = this.getCorporateDepots().iterator(); i.hasNext();) {
			DlvDepotModel dm = (DlvDepotModel) i.next();
			if (dm.getRegistrationCode().equalsIgnoreCase(accessCode)) {
				return dm.getDepotCode();
			}
		}
		return null;
	}

	public boolean checkCorpLocation(AddressModel address, String depotCode, String locationId) throws FDResourceException {
		DlvLocationModel location = this.getDepotLocation(depotCode, locationId);
		if (location == null) {
			return false;
		}
		AddressModel locAddress = location.getAddress();
		return locAddress.getAddress1().equals(address.getScrubbedStreet())
			&& NVL.apply(locAddress.getApartment(), "").equals(NVL.apply(address.getApartment(), ""))
			&& locAddress.getCity().equals(address.getCity())
			&& locAddress.getState().equals(address.getState())
			&& locAddress.getZipCode().equals(address.getZipCode());
	}

	public String getCustomerServiceEmail(String depotCode) throws FDResourceException {
		DlvDepotModel depot = this.getDepot(depotCode);
		if (depot == null) {
			throw new FDResourceException("Cannot Find Depot for depotCode: " + depotCode);
		} else {
			return depot.getCustomerServiceEmail();
		}
	}

	/**
	 * method to get particular location for a given depotCode matching the location id
	 * 
	 * @param String depotCode for which to get the location
	 * @param String locationId of the location to return
	 * @return DlvLocationModel
	 * @throws FDResourceException if there are any problems talking to remote resources
	 */
	public DlvLocationModel getDepotLocation(String depotCode, String locationId) throws FDResourceException {
		DlvDepotModel d = this.getDepot(depotCode);
		if (d == null)
			return null;

		for (Iterator lIter = d.getLocations().iterator(); lIter.hasNext();) {
			DlvLocationModel l = (DlvLocationModel) lIter.next();
			if (l.getPK().getId().equalsIgnoreCase(locationId))
				return l;
		}

		return null;
	}

	public String getDepotFacility(String locationId) throws FDResourceException {
		DlvDepotModel d = this.getDepotByLocationId(locationId);
		if(d == null){
			return "";
		}
		
		DlvLocationModel l = d.getLocation(locationId);
		if(l != null){
			return l.getFacility();
		}
		return "";
	}

	public DlvDepotModel getDepotByLocationId(String locationId) throws FDResourceException {
		DlvDepotModel foundDepot = null;

		for (Iterator i = this.getDepotMap().values().iterator(); i.hasNext();) {
			DlvDepotModel depot = (DlvDepotModel) i.next();
			for (Iterator locationIter = depot.getLocations().iterator(); locationIter.hasNext();) {
				DlvLocationModel location = (DlvLocationModel) locationIter.next();
				if (location.getPK().getId().equals(locationId)) {
					foundDepot = depot;
					break;
				}
			}
		}
		return foundDepot;
	}

	private DlvDepotManagerHome getDlvDepotManagerHome() {
		try {
			return (DlvDepotManagerHome) serviceLocator.getRemoteHome(
				DlvProperties.getDlvDepotManagerHome());
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}

}