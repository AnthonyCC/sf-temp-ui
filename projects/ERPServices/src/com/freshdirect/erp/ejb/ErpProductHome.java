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
 * ErpProduct entity home interface.
 *
 * @version    $Revision$
 * @author     $Author$
 */
public interface ErpProductHome extends EJBHome {

	/**
	 * Create from model with specified version.
	 *
	 * @param version entity version
	 * @param model ErpProductModel object
	 */
	public ErpProductEB create(int version, ModelI model) throws CreateException, RemoteException;

	public ErpProductEB findByPrimaryKey(VersionedPrimaryKey vpk) throws FinderException, RemoteException;
    
    public ErpProductEB findBySkuCode(String skuCode) throws FinderException, RemoteException;

	public ErpProductEB findBySkuCodeAndVersion(String skuCode, int version) throws FinderException, RemoteException;

}

