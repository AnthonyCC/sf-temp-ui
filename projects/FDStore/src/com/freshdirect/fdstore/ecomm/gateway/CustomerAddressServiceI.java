package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;

public interface CustomerAddressServiceI {

	public ErpAddressModel assumeDeliveryAddress(FDIdentity identity, String lastOrderId)
			throws FDResourceException, RemoteException;

	public String getParentOrderAddressId(String parentOrderAddressId) throws FDResourceException;
}
