/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer.ejb;

import com.freshdirect.framework.core.*;
import com.freshdirect.giftcard.ErpGiftCardI;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.customer.*;

import java.util.List;
import java.rmi.RemoteException;


/**
 * ErpCustomer remote interface.
 *
 * @version
 * @author     $Author$
 */
public interface ErpCustomerEB extends EntityBeanRemoteI, ErpCustomerI {
	/**
	 * Get ShipToAddresses.
	 *
	 * @return collection of ShipToAddress model objects*/
	public List getShipToAddresses() throws RemoteException;

	public String getUserId() throws RemoteException;

	public void updateUserId(String userId) throws ErpDuplicateUserIdException, RemoteException;

	public void setPasswordHash(String passwordHash) throws RemoteException;

	public String getSapId() throws RemoteException;

	public void setSapId(String sapId) throws RemoteException;

	public boolean isActive() throws RemoteException;

	public void setActive(boolean active) throws RemoteException;

	public List getPaymentMethods() throws RemoteException;

	public void updateCustomerCredit(String customerCreditId, double delta) throws RemoteException;
	
	public void removeCustomerCreditByComplaintId(String complaintId) throws ErpTransactionException, RemoteException; 

	public void updateCustomerAlert(ErpCustomerAlertModel element) throws RemoteException;

	public boolean removeCustomerAlert(PrimaryKey pk) throws RemoteException;

	public List getCustomerAlerts() throws RemoteException;

	public void addCustomerAlert(ErpCustomerAlertModel element) throws RemoteException;
	
	public boolean isOnAlert() throws RemoteException;
	
	public List getGiftCards() throws RemoteException;
	
}

