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
import com.freshdirect.fdstore.EnumEStoreId;


/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface ErpRoutingGatewaySB extends EJBObject {
    	
	void sendReservationUpdateRequest(String  reservationId, ContactAddressModel address, String sapOrderNumber) throws RemoteException;
	
	void sendSubmitOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String orderMobileNumber) throws RemoteException;
	
	void sendCancelOrderRequest(String saleId) throws RemoteException;
	
	void sendModifyOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String orderMobileNumber) throws RemoteException;
	

}
