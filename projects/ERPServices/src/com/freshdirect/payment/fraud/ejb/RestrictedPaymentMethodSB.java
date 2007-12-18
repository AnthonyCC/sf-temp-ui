package com.freshdirect.payment.fraud.ejb;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodCriteria;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodModel;

import java.rmi.RemoteException;
import java.util.List;

public interface RestrictedPaymentMethodSB extends EJBObject {

	public PrimaryKey createRestrictedPaymentMethod(RestrictedPaymentMethodModel model) throws RemoteException;

	public RestrictedPaymentMethodModel findRestrictedPaymentMethodByPrimaryKey(PrimaryKey pk) throws RemoteException;

	public List findRestrictedPaymentMethodByCustomerId(String customerId, EnumRestrictedPaymentMethodStatus status) throws RemoteException;
	
	public RestrictedPaymentMethodModel findRestrictedPaymentMethodByPaymentMethodId(String paymentMethodId, EnumRestrictedPaymentMethodStatus status) throws RemoteException;
	
	public List findRestrictedPaymentMethods(RestrictedPaymentMethodCriteria criteria) throws RemoteException;

	public void storeRestrictedPaymentMethod(RestrictedPaymentMethodModel model) throws RemoteException;

	public void removeRestrictedPaymentMethod(PrimaryKey pk, String lastModifyUser) throws RemoteException;
	
	public boolean checkBadAccount(ErpPaymentMethodI erpPaymentMethod, boolean useBadAccountCache) throws RemoteException;

	public List loadAllPatterns() throws RemoteException;

	public List loadAllRestrictedPaymentMethods() throws RemoteException;

	public List loadAllBadPaymentMethods() throws RemoteException;

	public ErpPaymentMethodI findPaymentMethodByAccountInfo(RestrictedPaymentMethodModel m) throws RemoteException;
		
}
