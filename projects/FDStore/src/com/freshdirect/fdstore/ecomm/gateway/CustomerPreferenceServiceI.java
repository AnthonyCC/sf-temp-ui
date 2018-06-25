package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;

public interface CustomerPreferenceServiceI {

	public String loadGoGreenPreference(String customerId) throws FDResourceException, RemoteException;

	public void storeGoGreenPreferences(String customerId, String goGreen) throws FDResourceException, RemoteException;

	public void storeMobilePreferences(String customerId, String fdCustomerId, String mobileNumber, String textOffers, String textDelivery,
			String orderNotices, String orderExceptions, String offers, String partnerMessages, EnumEStoreId eStoreId)
			throws FDResourceException, RemoteException;

	public void storeSmsPrefereceFlag(String fdCustomerId, String flag, EnumEStoreId eStoreId)
			throws FDResourceException, RemoteException;

	public void storeEmailPreferenceFlag(String fdCustomerId, String flag, EnumEStoreId eStoreId)
			throws FDResourceException, RemoteException;

}
