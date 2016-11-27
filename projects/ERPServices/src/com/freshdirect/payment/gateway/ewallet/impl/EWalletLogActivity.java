package com.freshdirect.payment.gateway.ewallet.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ewallet.ejb.EwalletActivityLogHome;
import com.freshdirect.payment.ewallet.ejb.EwalletActivityLogSB;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EWalletLogActivity {

	
private static ServiceLocator serviceLocator;
private static Category LOGGER = LoggerFactory.getInstance(EWalletLogActivity.class);

	static {
		try {
			Context ctx = ErpServicesProperties.getInitialContext();
			if(ctx != null) {
				serviceLocator = new ServiceLocator(ctx);
			}
		} catch(NamingException e) {
			LOGGER.warn("Unable to load context using primary"+e);
		}
	}
	
	public static void logActivity(EwalletActivityLogModel eWalletLogModel) {
		try {
			EwalletActivityLogSB logSB = getActivityLogHome().create();
			logSB.logActivity(eWalletLogModel);
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (CreateException e) {
			throw new EJBException(e);
		}
	}
	
	private static EwalletActivityLogHome getActivityLogHome() {
		try {
			if (serviceLocator == null) {
				serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
			}
			return (EwalletActivityLogHome) serviceLocator.getRemoteHome("freshdirect.payment.EwalletActivityLog");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
}
