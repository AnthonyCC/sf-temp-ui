package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;

public interface OrderServiceApiClientI {

	List<ErpSaleInfo> getOrderHistory(String customerId) throws FDResourceException, RemoteException;

	String getLastOrderId(String erpCustomerPK) throws RemoteException;

	String getLastOrderId(String erpCustomerPK, EnumEStoreId eStoreId) throws RemoteException;

	DlvSaleInfo getDlvSaleInfo(String orderNumber) throws RemoteException;

	List<ErpSaleInfo> getOrdersByDlvPassId(String erpCustomerPK,String dlvPassId) throws RemoteException;

	List<DlvPassUsageLine> getRecentOrdersByDlvPassId(String erpCustomerPK,String dlvPassId, int noOfDaysOld) throws RemoteException;

	Map<String, DlvPassUsageInfo> getDlvPassesUsageInfo(String customerId) throws RemoteException;

	int getValidOrderCount(String erpCustomerPK) throws RemoteException;
	
	boolean isOrderBelongsToUser(String customerId, String orderId) throws RemoteException;

	List<ErpSaleInfo> getWebOrderHistory(String customerId)
			throws FDResourceException, RemoteException;


}
