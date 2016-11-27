package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpRestrictedAvailabilityModel;

public interface ErpInventoryManagerSB extends EJBObject {

	public void updateInventories(List inventories) throws RemoteException;

	public void updateRestrictedInfos(Set<ErpRestrictedAvailabilityModel> restrictedInfos, Set<String> deletedMaterials) throws RemoteException;
}
