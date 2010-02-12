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
public interface ErpZoneInfoSB extends EJBObject {
    
	public ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws RemoteException;
    
	public Collection findZoneInfoMaster(String zoneIds[]) throws RemoteException;
	
    public Collection loadAllZoneInfoMaster() throws RemoteException;       
    
    public  String findZoneId(String serviceType,String zipCode) throws RemoteException;
    
    public String findZoneId(String servType) throws RemoteException;
    
          
}

