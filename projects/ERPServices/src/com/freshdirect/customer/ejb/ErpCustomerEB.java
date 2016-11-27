package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.util.List;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerAlertModel;
import com.freshdirect.customer.ErpCustomerI;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.framework.core.EntityBeanRemoteI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.giftcard.ErpGiftCardModel;


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
	public List<ErpAddressModel> getShipToAddresses() throws RemoteException;

	public String getUserId() throws RemoteException;

	public void updateUserId(String userId) throws ErpDuplicateUserIdException, RemoteException;

	public void setPasswordHash(String passwordHash) throws RemoteException;

	public String getSapId() throws RemoteException;

	public void setSapId(String sapId) throws RemoteException;

	public boolean isActive() throws RemoteException;

	public void setActive(boolean active) throws RemoteException;

	public List<ErpPaymentMethodI> getPaymentMethods() throws RemoteException;

	public void updateCustomerCredit(String customerCreditId, double delta) throws RemoteException;
	
	public void removeCustomerCreditByComplaintId(String complaintId) throws ErpTransactionException, RemoteException; 

	public void updateCustomerAlert(ErpCustomerAlertModel element) throws RemoteException;

	public boolean removeCustomerAlert(PrimaryKey pk) throws RemoteException;

	public List<ErpCustomerAlertModel> getCustomerAlerts() throws RemoteException;

	public void addCustomerAlert(ErpCustomerAlertModel element) throws RemoteException;
	
	public boolean isOnAlert() throws RemoteException;
	
	public List<ErpGiftCardModel> getGiftCards() throws RemoteException;
	
	public void updatePaymentMethodNewTx(ErpPaymentMethodI payment) throws RemoteException;
	
}

