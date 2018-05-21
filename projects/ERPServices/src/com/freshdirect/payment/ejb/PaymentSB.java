/*
 * PaymentSB.java
 * Date: 06/26/2002
 */
package com.freshdirect.payment.ejb;

/**
 * 
 * @author knadeem
 */

import javax.ejb.*;

import java.rmi.RemoteException;

import com.freshdirect.customer.ErpTransactionException;

/**
 * 
 * @deprecated
 *
 */
public interface PaymentSB extends EJBObject {
	
	/**
	 * capture the authorization for a given sale id
	 * 
	 * @param String saleId to capture
	 * 
	 * @throws RemoteException if there is a problem in accessing any remote resources
	 * 
	 * @deprecated
	 * 
	 */
	public void captureAuthorization(String saleId) throws ErpTransactionException, RemoteException;
	
	@Deprecated
	public void voidCaptures(String saleId) throws ErpTransactionException, RemoteException;
	@Deprecated
	public void deliveryConfirm(String saleId) throws ErpTransactionException, RemoteException;
	@Deprecated
	public void unconfirm(String saleId) throws ErpTransactionException, RemoteException;
	@Deprecated
	public void captureAuthEBTSale(String saleId) throws ErpTransactionException, RemoteException;
	
}
