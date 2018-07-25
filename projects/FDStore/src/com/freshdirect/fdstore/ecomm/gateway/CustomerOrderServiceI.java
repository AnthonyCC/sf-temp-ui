package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;

public interface CustomerOrderServiceI {
	
	public FDOrderI getOrder(FDIdentity identity, String saleId) throws FDResourceException, RemoteException;

    public FDOrderI getOrder(String saleId) throws FDResourceException, RemoteException;
}
