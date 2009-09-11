package com.freshdirect.webapp.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DepotCapacityCache implements Runnable {

	private static Category LOGGER = LoggerFactory.getInstance(DepotCapacityCache.class);
	private static DepotCapacityCache instance;

	private final static long DELAY = 1000 * 60 * 10; //10 minutes

	/** String depot code -> Boolean */
	private final Map availability = new HashMap();

	public synchronized static DepotCapacityCache getInstance() {
		if (instance == null) {
			instance = new DepotCapacityCache();
		}
		return instance;
	}

	private DepotCapacityCache() {
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	public synchronized boolean isAvailable(String depotCode) {
		Boolean av = (Boolean) this.availability.get(depotCode);
		if (av == null) {
			av = Boolean.TRUE;
			this.availability.put(depotCode, av);
		}
		return av.booleanValue();
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep(DELAY);
				this.refreshCache();
			}
		} catch (InterruptedException e) {
		}
	}

	private void refreshCache() {
		LOGGER.debug("Refreshing cache...");
		for (Iterator i = this.availability.keySet().iterator(); i.hasNext();) {
			boolean available = false;
			String depotCode = (String) i.next();
			try {
				DlvDepotModel depot = FDDepotManager.getInstance().getDepot(depotCode);
				if (depot != null) {

					Calendar begCal = Calendar.getInstance();
					begCal.add(Calendar.DATE, 1);
					Calendar endCal = Calendar.getInstance();
					endCal.add(Calendar.DATE, 8);

					for (Iterator j = depot.getLocations().iterator(); j.hasNext();) {
						DlvLocationModel location = (DlvLocationModel) j.next();
						List timeslots =
							FDDeliveryManager.getInstance().getTimeslotsForDepot(
								begCal.getTime(),
								endCal.getTime(),
								depot.getRegionId(),
								location.getZoneCode(), getContactAddress(location));
						for (int idx = 0, size = timeslots.size(); idx < size; idx++) {
							FDTimeslot timeslot = (FDTimeslot) timeslots.get(idx);
							if (timeslot.getTotalAvailable() > 0) {
								available = true;
								break;
							}
						}
						if (available) {
							break;
						}
					}
					LOGGER.debug("Depot " + depot.getDepotCode() + " availability: " + available);
					this.availability.put(depotCode, new Boolean(available));
				}
			} catch (FDResourceException fe) {
				LOGGER.warn("FDResourceException while trying to Referesh Cache", fe);
			}

			this.availability.put(depotCode, new Boolean(available));
		}
		LOGGER.debug("Refresh cache done...");
	}
	
	private ContactAddressModel getContactAddress(DlvLocationModel model) {
		ContactAddressModel _cModel = new ContactAddressModel();
		_cModel.setFrom(model.getAddress(), model.getId(), model.getFacility(), model.getId());
		return _cModel;
	}
}
