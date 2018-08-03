package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;

public interface CustomerDeliveryPassServiceI {
	public FDUserDlvPassInfo getDeliveryPassInfo(FDIdentity FDUserDlvPassInfo, EnumEStoreId estore)
			throws FDResourceException, RemoteException;

	public String hasAutoRenewDP(String customerPK) throws FDResourceException, RemoteException;
}
