package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.dataloader.LoaderException;

/**
 *@deprecated Please use the SAPProductFamilyController and SAPProductFamilyLoaderServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface SAPProductFamilyLoaderSB extends EJBObject{
	/** performs the actual load using data assembled by the parsers and the tree builder
     * in a single batch
     *
     * @param classes the collection of classes to be updated in this batch
     * @param activeMaterials the collection of materials to be created or updated in this batch
     * @throws RemoteException any system level problems
     * @throws LoaderException any problems encountered creating or updating objects in the system
     */    
	@Deprecated
	 public void loadData(List productFamilyList) throws RemoteException, LoaderException;
	@Deprecated
	public void updateCacheWithProdFly(List<String> familyIds)throws RemoteException, LoaderException;
}
