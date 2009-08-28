/* $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.routing.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.common.address.ContactAddressModel;


/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface RoutingGatewaySB extends EJBObject {
    	
	public void sendShippingAddress(AddressI address) throws RemoteException;
	
	public void sendDateRangeAndZoneForTimeslots(List timeSlots, ContactAddressModel address) throws RemoteException;
	
	public void sendReservationRequest(Object  reservation, ContactAddressModel address)throws RemoteException;

}
