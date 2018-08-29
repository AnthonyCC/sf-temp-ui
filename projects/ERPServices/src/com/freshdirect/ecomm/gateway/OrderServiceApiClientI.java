package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;

public interface OrderServiceApiClientI {
	
	public ErpSaleModel getOrder(String id) throws RemoteException;

	public ErpDeliveryInfoModel getDeliveryInfo(String saleId) throws ErpSaleNotFoundException, RemoteException;
	
	public List<ErpSaleInfo> getOrderHistory(String customerId) throws FDResourceException, RemoteException;

	public String getLastOrderId(String erpCustomerPK) throws RemoteException;

	public String getLastOrderId(String erpCustomerPK, EnumEStoreId eStoreId) throws RemoteException;

	public DlvSaleInfo getDlvSaleInfo(String orderNumber) throws RemoteException;

	public List<ErpSaleInfo> getOrdersByDlvPassId(String erpCustomerPK,String dlvPassId) throws RemoteException;

	public List<DlvPassUsageLine> getRecentOrdersByDlvPassId(String erpCustomerPK,String dlvPassId, int noOfDaysOld) throws RemoteException;

	public Map<String, DlvPassUsageInfo> getDlvPassesUsageInfo(String customerId) throws RemoteException;

	public int getValidOrderCount(String erpCustomerPK) throws RemoteException;
	
	public boolean isOrderBelongsToUser(String customerId, String orderId) throws RemoteException;

	public List<ErpSaleInfo> getWebOrderHistory(String customerId)
			throws FDResourceException, RemoteException;

	public List<DlvSaleInfo> getOrdersByTruck(String truckNumber, Date dlvDate) throws RemoteException;


}
