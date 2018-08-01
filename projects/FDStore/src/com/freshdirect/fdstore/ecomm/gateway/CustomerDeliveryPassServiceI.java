package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Date;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;

public interface CustomerDeliveryPassServiceI {
	public FDUserDlvPassInfo getDeliveryPassInfo(FDIdentity FDUserDlvPassInfo, EnumEStoreId estore)
			throws FDResourceException, RemoteException;

	public String hasAutoRenewDP(String customerPK) throws FDResourceException, RemoteException;

	public void setDpFreeTrialOptin(boolean dpFreeTrialOptin, String custId, FDActionInfo info)
			throws FDResourceException, RemoteException;

	public Date getDpFreeTrialOptinDate(String custId) throws FDResourceException, RemoteException;

	public void updateDpOptinDetails(boolean isAutoRenewDp, String custId, String dpType, FDActionInfo info,
			EnumEStoreId eStore) throws FDResourceException, RemoteException;

}
