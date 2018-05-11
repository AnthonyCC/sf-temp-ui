/*
 * Created on Sept 26, 2015
 *
 */
package com.freshdirect.fdstore.ewallet.impl.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;

/**
 * @author imohammed
 * @deprecated
 *
 */
public interface MasterpassServiceSB extends EJBObject {
	@Deprecated
	EwalletResponseData getToken(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData checkout(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData expressCheckout(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData connect(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData getAllPayMethodInEwallet(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData connectComplete(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData disconnect(EwalletRequestData ewalletRequestData) throws RemoteException;
	
	//Batch
	@Deprecated
	EwalletResponseData postback(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData standardCheckout(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData preStandardCheckout(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData expressCheckoutWithoutPrecheckout(EwalletRequestData ewalletRequestData) throws RemoteException; 
 
}
