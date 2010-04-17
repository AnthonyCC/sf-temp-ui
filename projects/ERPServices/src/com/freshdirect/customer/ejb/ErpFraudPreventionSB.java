package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

import javax.ejb.EJBObject;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.EnumFraudReason;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.framework.core.PrimaryKey;



public interface ErpFraudPreventionSB extends EJBObject {

	 public Set<EnumFraudReason> checkRegistrationFraud(ErpCustomerModel erpCustomer) throws RemoteException;

	/**
	 * @return null, or fraud reason
	 */
	 public EnumFraudReason preCheckOrderFraud(
		PrimaryKey erpCustomerPk,
		ErpAbstractOrderModel order,
		CrmAgentRole agentRole) throws RemoteException;
	
	 public void postCheckOrderFraud(PrimaryKey salePk, PrimaryKey erpCustomerPk, ErpAbstractOrderModel order, CrmAgentRole agentRole)
		throws RemoteException;

	 public boolean checkShipToAddressFraud(String erpCustomerId, ErpAddressModel address) throws RemoteException;
     
     public boolean checkBillToAddressFraud(String erpCustomerId, ContactAddressModel address) throws RemoteException;

	 public boolean checkPhoneFraud(String erpCustomerId, Collection<PhoneNumber> phones) throws RemoteException;

	 public boolean checkDuplicatePaymentMethodFraud(String erpCustomerId, ErpPaymentMethodI card) throws RemoteException;
	 
	 public EnumFraudReason preCheckGiftCardFraud(PrimaryKey erpCustomerPk, ErpAbstractOrderModel order, CrmAgentRole agentRole) throws RemoteException;
	 
	 public void postCheckGiftCardFraud(PrimaryKey salePk, PrimaryKey erpCustomerPk, ErpAbstractOrderModel order, CrmAgentRole agentRole) throws RemoteException;
	 
	 public EnumFraudReason preCheckDonationFraud(PrimaryKey erpCustomerPk, ErpAbstractOrderModel order, CrmAgentRole agentRole) throws RemoteException;
	 
	 public void postCheckDonationFraud(PrimaryKey salePk, PrimaryKey erpCustomerPk, ErpAbstractOrderModel order, CrmAgentRole agentRole) throws RemoteException;

}