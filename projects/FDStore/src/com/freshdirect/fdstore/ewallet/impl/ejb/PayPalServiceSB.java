package com.freshdirect.fdstore.ewallet.impl.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;

public interface PayPalServiceSB extends EJBObject {
	EwalletResponseData getToken(EwalletRequestData ewalletRequestData) throws RemoteException;
	EwalletResponseData addPayPalWallet(EwalletRequestData ewalletRequestData) throws RemoteException;
	EwalletResponseData disconnect(EwalletRequestData ewalletRequestData) throws RemoteException;
}
