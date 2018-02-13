package com.freshdirect.payment.gateway.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.GatewayActivityLogHome;
import com.freshdirect.payment.ejb.GatewayActivityLogSB;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.service.FDECommerceService;

public class GatewayLogActivity {
	
private static ServiceLocator serviceLocator = new ServiceLocator();
private static Category LOGGER = LoggerFactory.getInstance(GatewayLogActivity.class);

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
	
	public static void logActivity(GatewayType gatewayType,Response response) {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.GatewayActivityLog)){
				FDECommerceService.getInstance().logGatewayActivity(gatewayType, response);
			}
			else {
				GatewayActivityLogSB logSB = getActivityLogHome().create();
				logSB.logActivity(gatewayType, response);
			}
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (CreateException e) {
			throw new EJBException(e);
		}
	}
	
	private static GatewayActivityLogHome getActivityLogHome() {
		try {
			return (GatewayActivityLogHome) serviceLocator.getRemoteHome("freshdirect.payment.GatewayActivityLog");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

}
