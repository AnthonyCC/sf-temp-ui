package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.customer.ErpTransactionException;

public interface PaymentServiceI {
	/**
	 * capture the authorization for a given sale id
	 * 
	 * @param String saleId to capture
	 * 
	 * @throws RemoteException if there is a problem in accessing any remote resources
	 */
	public void captureAuthorization(String saleId) throws ErpTransactionException, RemoteException;
	
	public void voidCaptures(String saleId) throws ErpTransactionException, RemoteException;
	
	public void deliveryConfirm(String saleId) throws ErpTransactionException, RemoteException;
	
	public void unconfirm(String saleId) throws ErpTransactionException, RemoteException;
	
	public void captureAuthEBTSale(String saleId) throws ErpTransactionException, RemoteException;}
