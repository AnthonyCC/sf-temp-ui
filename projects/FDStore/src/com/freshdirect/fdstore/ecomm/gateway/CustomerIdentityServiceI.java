package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;

public interface CustomerIdentityServiceI {

	public FDIdentity login(String userId, String password)
			throws FDAuthenticationException, FDResourceException, RemoteException;

	public FDUser recognize(FDIdentity identity) throws FDAuthenticationException, FDResourceException, RemoteException;

	public FDUser recognize(String cookie, EnumEStoreId eStoreId)
			throws FDAuthenticationException, FDResourceException, RemoteException;

	public FDUser recognize(FDIdentity identity, EnumEStoreId eStoreId, final boolean lazy,
			boolean populateDeliveryPlantInfo) throws FDAuthenticationException, FDResourceException, RemoteException;

	public FDCustomerModel getFDCustomer(String fdCustomerId, String erpCustomerId) throws FDResourceException;

	public ErpCustomerModel getErpCustomer(String erpCustomerId) throws FDResourceException;

}
