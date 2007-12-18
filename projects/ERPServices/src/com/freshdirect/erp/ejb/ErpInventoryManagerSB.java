package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface ErpInventoryManagerSB extends EJBObject {

	public void updateInventories(List inventories) throws RemoteException;

}
