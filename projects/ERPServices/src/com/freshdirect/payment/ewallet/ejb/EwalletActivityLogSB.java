package com.freshdirect.payment.ewallet.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
/**
 *@deprecated Please use the EwalletActivityLogController and EwalletActivityLogServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */ 
public interface EwalletActivityLogSB extends EJBObject {
	@Deprecated
	public void logActivity(EwalletActivityLogModel eWalletLogModel) throws RemoteException;
}
