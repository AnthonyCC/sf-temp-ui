/* $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.routing.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.common.address.AddressI;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface RoutingGatewaySB extends EJBObject {
    	
	public void sendShippingAddress(AddressI address) throws RemoteException;	

}
