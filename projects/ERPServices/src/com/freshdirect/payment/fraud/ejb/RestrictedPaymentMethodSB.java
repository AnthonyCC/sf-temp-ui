package com.freshdirect.payment.fraud.ejb;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodCriteria;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodModel;

import java.rmi.RemoteException;
import java.util.List;

/**
 *@deprecated Please use the RestrictedPaymentMethod and RestrictedPaymentMethodServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface RestrictedPaymentMethodSB extends EJBObject {

	 @Deprecated public PrimaryKey createRestrictedPaymentMethod(RestrictedPaymentMethodModel model) throws RemoteException;
	 @Deprecated
	public RestrictedPaymentMethodModel findRestrictedPaymentMethodByPrimaryKey(PrimaryKey pk) throws RemoteException;
	 @Deprecated
	public List<RestrictedPaymentMethodModel> findRestrictedPaymentMethodByCustomerId(String customerId, EnumRestrictedPaymentMethodStatus status) throws RemoteException;
	 @Deprecated
	public RestrictedPaymentMethodModel findRestrictedPaymentMethodByPaymentMethodId(String paymentMethodId, EnumRestrictedPaymentMethodStatus status) throws RemoteException;
	 @Deprecated
	public List<RestrictedPaymentMethodModel> findRestrictedPaymentMethods(RestrictedPaymentMethodCriteria criteria) throws RemoteException;
	 @Deprecated
	public void storeRestrictedPaymentMethod(RestrictedPaymentMethodModel model) throws RemoteException;
	 @Deprecated
	public void removeRestrictedPaymentMethod(PrimaryKey pk, String lastModifyUser) throws RemoteException;
	 @Deprecated
	public boolean checkBadAccount(ErpPaymentMethodI erpPaymentMethod, boolean useBadAccountCache) throws RemoteException;
	 @Deprecated
	public List<RestrictedPaymentMethodModel> loadAllPatterns() throws RemoteException;
	 @Deprecated
	public List<RestrictedPaymentMethodModel> loadAllRestrictedPaymentMethods() throws RemoteException;
	 @Deprecated
	public List<RestrictedPaymentMethodModel> loadAllBadPaymentMethods() throws RemoteException;
	 @Deprecated
	public ErpPaymentMethodI findPaymentMethodByAccountInfo(RestrictedPaymentMethodModel m) throws RemoteException;
		
}
