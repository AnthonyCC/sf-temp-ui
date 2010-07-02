/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpMaterialModel;

/**
 * the remote interface for the SAPLoader session bean
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface SAPLoaderSB extends EJBObject {
    
    /** performs the actual load using data assembled by the parsers and the tree builder
     * in a single batch
     *
     * @param classes the collection of classes to be updated in this batch
     * @param activeMaterials the collection of materials to be created or updated in this batch
     * @param characteristicValuePrices the collection of characteristic value prices to create or update in this batch
     * @throws RemoteException any system level problems
     * @throws LoaderException any problems encountered creating or updating objects in the system
     */    
    public void loadData(Map<String, ErpClassModel> classes, Map<ErpMaterialModel, Map<String, Object>> activeMaterials, Map<ErpCharacteristicValuePriceModel, Map<String, String>> characteristicValuePrices) throws RemoteException, LoaderException;

}

