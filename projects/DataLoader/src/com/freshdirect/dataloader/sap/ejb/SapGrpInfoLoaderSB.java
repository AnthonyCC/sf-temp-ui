package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpRestrictedAvailabilityModel;
import com.freshdirect.dataloader.LoaderException;

public interface SapGrpInfoLoaderSB extends EJBObject {
	 /** performs the actual load using data assembled by the parsers and the tree builder
     * in a single batch
     *
     * @param classes the collection of classes to be updated in this batch
     * @param activeMaterials the collection of materials to be created or updated in this batch
     * @param characteristicValuePrices the collection of characteristic value prices to create or update in this batch
     * @throws RemoteException any system level problems
     * @throws LoaderException any problems encountered creating or updating objects in the system
     */    
    public void loadData(List grpInfoList) throws RemoteException, LoaderException;
    
    
}
