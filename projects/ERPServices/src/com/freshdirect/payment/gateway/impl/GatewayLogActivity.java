package com.freshdirect.payment.gateway.impl;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.service.FDECommerceService;

public class GatewayLogActivity {
	
private static Category LOGGER = LoggerFactory.getInstance(GatewayLogActivity.class);

	
	public static void logActivity(GatewayType gatewayType,Response response) {
		try {
				FDECommerceService.getInstance().logGatewayActivity(gatewayType, response);
			
		} catch (RemoteException e) {
			throw new EJBException(e);
		} 
	}
	

}
