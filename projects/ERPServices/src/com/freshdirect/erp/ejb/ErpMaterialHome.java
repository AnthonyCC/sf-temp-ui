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
 * ErpMaterial entity home interface.
 *
 * @version    $Revision$
 * @author     $Author$
 */
public interface ErpMaterialHome extends EJBHome {

	/**
	 * Create from model with specified version.
	 *
	 * @param version entity version
	 * @param model ErpMaterialModel object
	 */
	public ErpMaterialEB create(int version, ModelI model) throws CreateException, RemoteException;

	public ErpMaterialEB findByPrimaryKey(VersionedPrimaryKey vpk) throws FinderException, RemoteException;
    
    public ErpMaterialEB findBySapId(String sapId) throws FinderException, RemoteException;

}

