package com.freshdirect.customer;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ejb.ActivityLogHome;
import com.freshdirect.customer.ejb.ActivityLogSB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.ServiceLocator;

public class ActivityLog {

	private static ActivityLog INSTANCE = null;

	private final ServiceLocator serviceLocator;

	private ActivityLog() throws NamingException {
		this.serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
	}

	public static ActivityLog getInstance() throws FDResourceException {
		if (INSTANCE == null) {
			try {
				INSTANCE = new ActivityLog();
			} catch (NamingException e) {
				throw new FDResourceException(e);
			}
		}
		return INSTANCE;
	}

	public Collection findActivityByTemplate(ErpActivityRecord template) throws FDResourceException {
		try {
			return this.getActivityLogSB().findActivityByTemplate(template);
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to ActivityLogSB");
		}
	}

	private ActivityLogSB getActivityLogSB() throws FDResourceException {
		try {
			ActivityLogHome home = (ActivityLogHome) serviceLocator.getRemoteHome("freshdirect.customer.ActivityLog",
				ActivityLogHome.class);
			return home.create();
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} catch (CreateException e) {
			throw new FDResourceException(e, "Cannot create CrmManagerSB");
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}

}