package com.freshdirect.fdstore.standingorders.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.core.PrimaryKey;

public interface FDStandingOrdersSB extends EJBObject {
	public FDStandingOrder createStandingOrder(FDCustomerList list) throws RemoteException;
	public Collection<FDStandingOrder> loadActiveStandingOrders() throws RemoteException;
	public Collection<FDStandingOrder> loadCustomerStandingOrders(FDIdentity identity) throws RemoteException;
	public FDStandingOrder load(PrimaryKey pk) throws RemoteException;
	public void delete(FDStandingOrder so) throws RemoteException;
	public String save(FDStandingOrder so) throws RemoteException;
	public void assignStandingOrderToOrder(PrimaryKey salePK, PrimaryKey standingOrderPK) throws RemoteException;		
}
