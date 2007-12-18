/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;

import com.freshdirect.sap.SapCustomerI;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.command.SapPostReturnCommand;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface SapGatewaySB extends EJBObject {
    
	public SapOrderI checkAvailability(SapOrderI order, long timeout) throws RemoteException;

	public void sendCreateSalesOrder(SapOrderI order) throws RemoteException;

	public void sendCreateCustomer(String erpCustomerNumber, SapCustomerI customer) throws RemoteException;
	
	public void sendCancelSalesOrder(String webOrderNumber, String sapOrderNumber) throws RemoteException;

	public void sendChangeSalesOrder(String webOrderNumber, String sapOrderNumber, SapOrderI order) throws RemoteException;
	
	public void sendReturnInvoice(SapPostReturnCommand command) throws RemoteException;

}

