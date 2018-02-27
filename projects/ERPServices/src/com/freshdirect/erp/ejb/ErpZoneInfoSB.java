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
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;
import javax.ejb.ObjectNotFoundException;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpProductInfoModel;

/**
 * the remote interface for the ErpMaterialInfo session bean
 *
 *
 * @version $Revision$
 * @author $Author$
 */

/**
 *@deprecated Please use the ZoneInfoController and ZoneInfoServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface ErpZoneInfoSB extends EJBObject {
    @Deprecated
	public ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws RemoteException;
    @Deprecated
	public Collection findZoneInfoMaster(String zoneIds[]) throws RemoteException;
    @Deprecated
    public Collection loadAllZoneInfoMaster() throws RemoteException;       
    @Deprecated
    public  String findZoneId(String serviceType,String zipCode) throws RemoteException;
    @Deprecated
    public String findZoneId(String servType) throws RemoteException;
    @Deprecated
    public List<ErpZoneMasterInfo> getAllZoneInfoDetails() throws RemoteException;
    
          
}

