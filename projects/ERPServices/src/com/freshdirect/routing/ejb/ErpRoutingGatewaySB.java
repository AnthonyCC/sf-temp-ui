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

import com.freshdirect.common.address.ContactAddressModel;


/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface ErpRoutingGatewaySB extends EJBObject {
    	
	void sendReservationUpdateRequest(String  reservationId, ContactAddressModel address, String sapOrderNumber) throws RemoteException;

}
