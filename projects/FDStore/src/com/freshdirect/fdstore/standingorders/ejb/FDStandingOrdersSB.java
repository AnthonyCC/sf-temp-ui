package com.freshdirect.fdstore.standingorders.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrderFilterCriteria;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList;
import com.freshdirect.framework.core.PrimaryKey;

public interface FDStandingOrdersSB extends EJBObject {
	public FDStandingOrder createStandingOrder(FDCustomerList list) throws RemoteException;
	public Collection<FDStandingOrder> loadActiveStandingOrders() throws RemoteException;
	public Collection<FDStandingOrder> loadCustomerStandingOrders(FDIdentity identity) throws RemoteException;
	public FDStandingOrder load(PrimaryKey pk) throws RemoteException;
	public void delete(FDActionInfo info, FDStandingOrder so) throws RemoteException;
	public String save(FDActionInfo info, FDStandingOrder so, String saleId) throws RemoteException;
	public void assignStandingOrderToOrder(PrimaryKey salePK, PrimaryKey standingOrderPK) throws RemoteException;
	public void markSaleAltDeliveryDateMovement(PrimaryKey salePK) throws RemoteException;
	public void logActivity(ErpActivityRecord record) throws RemoteException;
	public FDStandingOrderInfoList getActiveStandingOrdersCustInfo(FDStandingOrderFilterCriteria filter)throws RemoteException;
	public void clearStandingOrderErrors(String[] soIDs,String agentId)throws RemoteException;
	public FDStandingOrderInfoList getFailedStandingOrdersCustInfo()throws RemoteException;
	public FDStandingOrderInfoList getMechanicalFailedStandingOrdersCustInfo()throws RemoteException;
	public Map<Date,Date> getStandingOrdersAlternateDeliveryDates() throws RemoteException;
	public List<FDStandingOrderAltDeliveryDate> getStandingOrderAltDeliveryDates() throws RemoteException;
	public void addStandingOrderAltDeliveryDate(FDStandingOrderAltDeliveryDate altDeliveryDate) throws RemoteException;	
	public void updateStandingOrderAltDeliveryDate(FDStandingOrderAltDeliveryDate altDeliveryDate) throws RemoteException;
	public void deleteStandingOrderAltDeliveryDate(FDStandingOrderAltDeliveryDate altDeliveryDate) throws RemoteException;
}
