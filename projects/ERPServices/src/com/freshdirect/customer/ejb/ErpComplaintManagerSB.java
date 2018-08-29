/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpComplaintReason;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface ErpComplaintManagerSB extends EJBObject {
	/**
	 *@deprecated This method is moved to Storefront2.0 project.
	 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
	 */
	 public Map<String, List<ErpComplaintReason>> getReasons(boolean excludeCartonReq) throws RemoteException;
	 /**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	 @Deprecated
	 public Map<String,String> getComplaintCodes() throws RemoteException;
	 //not used 
	 @Deprecated
	 public Collection<String> getPendingComplaintSaleIds() throws RemoteException;
	 //not used 
	 @Deprecated
	 public void rejectMakegoodComplaint(String makegood_sale_id) throws RemoteException;
	 /**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
     */
	 @Deprecated
	 public ErpComplaintReason getReasonByCompCode(String cCode) throws RemoteException;
}
