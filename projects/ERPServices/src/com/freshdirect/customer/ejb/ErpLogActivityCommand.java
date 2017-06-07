package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.payment.service.FDECommerceService;

public class ErpLogActivityCommand {
	
	private final ServiceLocator locator;
	private final ErpActivityRecord record;
	private final boolean newTx;
	
	public ErpLogActivityCommand(ServiceLocator locator, ErpActivityRecord record) {
		this(locator, record, false);
	}
	
	public ErpLogActivityCommand(ServiceLocator locator, ErpActivityRecord record, boolean newTx) {
		this.locator = locator;
		this.record = record;
		this.newTx = newTx;
	}
	
	public ErpLogActivityCommand(ErpActivityRecord record) {
		this.locator = new ServiceLocator();
		this.record = record;
		this.newTx = false;
	}
	
	public void execute() {
		try {
			ActivityLogSB logSB = getActivityLogHome().create();
			
			if (newTx) {
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("customer.ejb.ActivityLogSB")){
					FDECommerceService.getInstance().logActivityNewTX(record);
				}
				else{
					logSB.logActivityNewTX(record);
				}
			} else {
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("customer.ejb.ActivityLogSB")){
					FDECommerceService.getInstance().logActivity(record);
				}
				else{
					logSB.logActivity(record);
				}
			}
			
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (CreateException e) {
			throw new EJBException(e);
		}
	}
	
	private ActivityLogHome getActivityLogHome() {
		try {
			return (ActivityLogHome) locator.getRemoteHome("freshdirect.customer.ActivityLog");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

}
