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
import java.util.Collection;
import java.util.Date;

import com.freshdirect.framework.core.EntityBeanRemoteI;

/**
 * ErpInventory remote interface.
 *
 * @version    $Revision$
 * @author     $Author$
 */
/**
 * @deprecated Please use the ErpInventoryDaoI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface ErpInventoryEB extends EntityBeanRemoteI {

	/**
	 * Set inventory entries. Overwrites existing collection only if
	 * the specified dateCreated is newer than what's already stored.
	 *
	 * @param dateCreated date the entries were queried on
	 * @param collection collection of ErpInventoryEntryModel objects
	 *
	 * @return true if the entires were stored
	 */
	@Deprecated
	public boolean setEntries(Date dateCreated, Collection collection) throws RemoteException;
	
}

