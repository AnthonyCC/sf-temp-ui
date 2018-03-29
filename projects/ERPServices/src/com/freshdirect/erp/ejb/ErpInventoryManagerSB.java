package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpRestrictedAvailabilityModel;

/**
 * @deprecated Please use the ErpInventoryServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface ErpInventoryManagerSB extends EJBObject {
	@Deprecated
	public void updateInventories(List inventories) throws RemoteException;
	@Deprecated
	public void updateRestrictedInfos(Set<ErpRestrictedAvailabilityModel> restrictedInfos, Set<String> deletedMaterials) throws RemoteException;
}
