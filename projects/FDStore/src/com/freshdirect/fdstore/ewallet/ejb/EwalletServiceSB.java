/*
 * Created on Oct 6, 2015
 *
 */
package com.freshdirect.fdstore.ewallet.ejb;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;

/**
 * @author imohammed
 * @deprecated
 */
public interface EwalletServiceSB extends EJBObject {
	@Deprecated
	EwalletResponseData getToken(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData checkout(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData expressCheckout(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData getAllPayMethodInEwallet(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData connectComplete(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData disconnect(EwalletRequestData ewalletRequestData) throws RemoteException;
	
	//Batch
	@Deprecated
	EwalletResponseData postbackTrxns(EwalletRequestData req) throws RemoteException;
	
	//Standard checkout
	@Deprecated
	EwalletResponseData standardCheckout(EwalletRequestData ewalletRequestData) throws RemoteException;
	@Deprecated
	EwalletResponseData preStandardCheckout(EwalletRequestData ewalletRequestData) throws RemoteException;
	//PayPal
	@Deprecated
	EwalletResponseData addPayPalWallet(EwalletRequestData ewalletRequestData) throws RemoteException;
	
}