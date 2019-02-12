package com.freshdirect.payment.gateway.ewallet.impl;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EWalletLogActivity {

	private static Category LOGGER = LoggerFactory.getInstance(EWalletLogActivity.class);

	public static void logActivity(EwalletActivityLogModel eWalletLogModel) {
		try {
			IECommerceService commerceService = FDECommerceService.getInstance();
			commerceService.logActivity(eWalletLogModel);

		} catch (RemoteException e) {
			throw new EJBException(e);
		}
	}

}
