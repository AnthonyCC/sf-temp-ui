/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import javax.ejb.*;

import com.freshdirect.framework.core.*;

/**
 * ErpInventory entity home interface.
 *
 * @version    $Revision$
 * @author     $Author$
 */
public interface ErpInventoryHome extends EJBHome {

	public ErpInventoryEB create(ModelI model) throws CreateException, RemoteException;

	public ErpInventoryEB findByPrimaryKey(PrimaryKey pk) throws FinderException, RemoteException;

}

