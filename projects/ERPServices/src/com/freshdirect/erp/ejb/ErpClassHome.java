/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp.ejb;

import java.util.Collection;

import java.rmi.RemoteException;
import javax.ejb.*;

import com.freshdirect.framework.core.*;

/**
 * ErpClass entity home interface.
 *
 * @version    $Revision$
 * @author     $Author$
 */
public interface ErpClassHome extends EJBHome {

	/**
	 * Create from model with specified version.
	 *
	 * @param version entity version
	 * @param model ErpClassModel object
	 */
	public ErpClassEB create(int version, ModelI model) throws CreateException, RemoteException;

	public ErpClassEB findByPrimaryKey(VersionedPrimaryKey vpk) throws FinderException, RemoteException;
    
    public ErpClassEB findBySapId(String sapId) throws FinderException, RemoteException;
    
    public Collection findAllClasses() throws FinderException, RemoteException;

}

