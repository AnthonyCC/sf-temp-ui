/* $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.routing.ejb;
/**
 *@deprecated Please do the changes in RoutingGatewayService and RoutingGatewayServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
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
    @Deprecated
	void sendReservationUpdateRequest(String  reservationId, ContactAddressModel address, String sapOrderNumber) throws RemoteException;
    @Deprecated
	void sendSubmitOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType, 
			String unattendedInstr,String orderMobileNumber,String erpOrderId,boolean containsAlcohol) throws RemoteException;
    @Deprecated
	void sendCancelOrderRequest(String saleId) throws RemoteException;
    @Deprecated
	void sendModifyOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType, 
			String unattendedInstr,String orderMobileNumber,String erpOrderId,boolean containsAlcohol) throws RemoteException;
	

}
