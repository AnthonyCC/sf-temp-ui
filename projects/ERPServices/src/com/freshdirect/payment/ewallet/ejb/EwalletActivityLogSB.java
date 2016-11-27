package com.freshdirect.payment.ewallet.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;

public interface EwalletActivityLogSB extends EJBObject {
	
	public void logActivity(EwalletActivityLogModel eWalletLogModel) throws RemoteException;
}
