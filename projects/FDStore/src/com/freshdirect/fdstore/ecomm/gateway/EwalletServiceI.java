package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;

public interface EwalletServiceI {
	

	
	EwalletResponseData getToken(EwalletRequestData ewalletRequestData) throws RemoteException;
	EwalletResponseData checkout(EwalletRequestData ewalletRequestData) throws RemoteException;
	EwalletResponseData expressCheckout(EwalletRequestData ewalletRequestData) throws RemoteException;
	EwalletResponseData connect(EwalletRequestData ewalletRequestData) throws RemoteException;
	EwalletResponseData getAllPayMethodInEwallet(EwalletRequestData ewalletRequestData) throws RemoteException;
	EwalletResponseData connectComplete(EwalletRequestData ewalletRequestData) throws RemoteException;
	EwalletResponseData disconnect(EwalletRequestData ewalletRequestData) throws RemoteException;
	
	//Batch
	EwalletResponseData postbackTrxns(EwalletRequestData req) throws RemoteException;
	
	//Standard checkout
	EwalletResponseData standardCheckout(EwalletRequestData ewalletRequestData) throws RemoteException;
	EwalletResponseData preStandardCheckout(EwalletRequestData ewalletRequestData) throws RemoteException;
	//PayPal
	EwalletResponseData addPayPalWallet(EwalletRequestData ewalletRequestData) throws RemoteException;
	

}
