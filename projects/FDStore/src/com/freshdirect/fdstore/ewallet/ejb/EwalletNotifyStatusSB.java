/*
 * Created on Oct 6, 2015
 *
 */
package com.freshdirect.fdstore.ewallet.ejb;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

/**
 * @author imohammed
 *
 *
 */

/**
 *@deprecated Please use the EwalletNotifyStatusController and EwalletNotifyStatusServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */ 
public interface EwalletNotifyStatusSB extends EJBObject {
	//Batch
	@Deprecated
	void postTrxnsToEwallet() throws RemoteException;
	@Deprecated
	void loadTrxnsForPostBack(int maxDays) throws RemoteException;
}