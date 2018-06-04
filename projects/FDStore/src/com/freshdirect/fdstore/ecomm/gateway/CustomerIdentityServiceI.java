package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;

public interface CustomerIdentityServiceI {

	public FDIdentity login(String userId, String password)
			throws FDAuthenticationException, FDResourceException, RemoteException;

	public FDUser recognize(FDIdentity identity, EnumEStoreId eStoreId, final boolean lazy,
			boolean populateDeliveryPlantInfo) throws FDAuthenticationException, FDResourceException, RemoteException;

}
