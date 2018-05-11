package com.freshdirect.fdstore.ewallet.impl.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;

/**
 * 
 * @deprecated
 *
 */
public interface PayPalServiceSB extends EJBObject {
	@Deprecated
	EwalletResponseData getToken(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData addPayPalWallet(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData disconnect(EwalletRequestData ewalletRequestData) throws RemoteException;
}
