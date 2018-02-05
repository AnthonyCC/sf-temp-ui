package com.freshdirect.customer;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ejb.ActivityLogHome;
import com.freshdirect.customer.ejb.ActivityLogSB;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.payment.service.FDECommerceService;

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

	public Collection<ErpActivityRecord> findActivityByTemplate(ErpActivityRecord template) throws FDResourceException {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ActivityLogSB)){
				return FDECommerceService.getInstance().findActivityByTemplate(template);
			}
			else{
				return this.getActivityLogSB().findActivityByTemplate(template);
			}
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to ActivityLogSB");
		}
	}
	
	public Collection<ErpActivityRecord> getCCActivitiesByTemplate(ErpActivityRecord template) throws FDResourceException {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("customer.ejb.ActivityLogSB")){
				return FDECommerceService.getInstance().getCCActivitiesByTemplate(template);
			}
			else{
				return this.getActivityLogSB().getCCActivitiesByTemplate(template);
			}
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to ActivityLogSB");
		}
	}
	
	public void logActivity(ErpActivityRecord rec) throws FDResourceException {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ActivityLogSB)){
				FDECommerceService.getInstance().logActivity(rec);
			}
			else{
				this.getActivityLogSB().logActivity( rec );
			}
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to ActivityLogSB");
		}		
	}

	private ActivityLogSB getActivityLogSB() throws FDResourceException {
		try {
			ActivityLogHome home = (ActivityLogHome) serviceLocator.getRemoteHome("freshdirect.customer.ActivityLog");
			return home.create();
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} catch (CreateException e) {
			throw new FDResourceException(e, "Cannot create CrmManagerSB");
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to CrmManagerSB");
		}
	}
	
	public Map<String, List> getFilterLists(ErpActivityRecord template) throws FDResourceException {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ActivityLogSB)){
				return FDECommerceService.getInstance().getFilterLists(template);
			}
			else{
				return this.getActivityLogSB().getFilterLists(template);
			}
		} catch (RemoteException e) {
			throw new FDResourceException(e, "Cannot talk to ActivityLogSB");
		}
	
	}

}
