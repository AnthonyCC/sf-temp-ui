/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface ErpComplaintManagerSB extends EJBObject {

	 public Map getReasons() throws RemoteException;
	 
	 public Map getComplaintCodes() throws RemoteException;

	 public Collection getPendingComplaintSaleIds() throws RemoteException;

	 public void rejectMakegoodComplaint(String makegood_sale_id) throws RemoteException;
}
