package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDIdentity;

public interface CustomerIdentityServiceI {

	public FDIdentity login(String userId, String password) throws FDAuthenticationException, FDResourceException, RemoteException;

}
