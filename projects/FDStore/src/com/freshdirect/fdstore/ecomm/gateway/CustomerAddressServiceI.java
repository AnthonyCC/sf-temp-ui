package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;

public interface CustomerAddressServiceI {

	public ErpAddressModel assumeDeliveryAddress(FDIdentity identity, String lastOrderId, String eStore)
			throws FDResourceException, RemoteException;

	public String getParentOrderAddressId(String parentOrderAddressId) throws FDResourceException;

	public ErpAddressModel getAddress(FDIdentity identity, String addressId)
			throws FDResourceException, RemoteException;

	public List<ErpAddressModel> getShippingAddresses(FDIdentity identity) throws FDResourceException, RemoteException;

	public boolean addShippingAddress(FDActionInfo info, boolean checkUniqueness, ErpAddressModel address)
			throws FDResourceException, RemoteException;

	public boolean updateShippingAddress(FDActionInfo info, boolean checkUniqueness, ErpAddressModel address)
			throws FDResourceException, RemoteException;

	public void removeShippingAddress(FDActionInfo info, PrimaryKey pk) throws FDResourceException, RemoteException;

	public String getDefaultShipToAddressPK(FDIdentity identity, String eStore) throws FDResourceException;

	public void setDefaultShippingAddressPK(FDIdentity identity, String shipToAddressPK, String eStore) throws FDResourceException,RemoteException;
	
}
