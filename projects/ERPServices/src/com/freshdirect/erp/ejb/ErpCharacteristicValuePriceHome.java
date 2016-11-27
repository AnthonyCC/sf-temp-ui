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

import java.util.Collection;

import com.freshdirect.framework.core.*;

/**
 * ErpCharacteristicValuePrice entity home interface.
 *
 * @version    $Revision$
 * @author     $Author$
 */
public interface ErpCharacteristicValuePriceHome extends EJBHome {

	/**
	 * Create from model with specified version.
	 */
	public ErpCharacteristicValuePriceEB create(int version, ModelI model) throws CreateException, RemoteException;

	public ErpCharacteristicValuePriceEB findByPrimaryKey(VersionedPrimaryKey vpk) throws FinderException, RemoteException;
	
	public Collection findByMaterial(VersionedPrimaryKey materialPK) throws FinderException, RemoteException;
    
    public ErpCharacteristicValuePriceEB findByMaterialAndCharValue(VersionedPrimaryKey materialPK, VersionedPrimaryKey charValPK) throws FinderException, RemoteException;

}

