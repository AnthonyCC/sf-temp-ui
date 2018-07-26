package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.UnsettledOrdersInfo;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;

public interface CustomerOrderServiceI {
	
	public FDOrderI getOrder(FDIdentity identity, String saleId) throws FDResourceException, RemoteException;

    public FDOrderI getOrder(String saleId) throws FDResourceException, RemoteException;
    
    public CustomerAvgOrderSize getHistoricOrderSize(String customerId) throws FDResourceException, RemoteException;
    
    public void updateOrderInModifyState(ErpSaleModel sale)	throws FDResourceException, RemoteException;
    
    public void clearModifyCartlines(String currentOrderId) throws FDResourceException, RemoteException;
    
    public List<UnsettledOrdersInfo> getUnsettledOrders() throws FDResourceException, RemoteException;
    
    public List<FDCartLineI> getModifiedCartlines(String orderId, UserContext userContext) throws FDResourceException, RemoteException;

	public void saveModifiedCartline(PrimaryKey  userpk, StoreContext storeContext, FDCartLineI newLine, String orderId, boolean isModifiedCartLine) throws FDResourceException, RemoteException;
	
	public void removeModifiedCartline(FDCartLineI cartLine) throws FDResourceException, RemoteException;

	public void updateModifiedCartlineQuantity(FDCartLineI cartLine) throws FDResourceException, RemoteException;
    
}
