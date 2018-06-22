package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.FDResourceException;
public interface CallCenterManagerServiceI {

	public Map<String, List<ErpComplaintReason>> getComplaintReasons(boolean excludeCartonReq)
			throws FDResourceException, RemoteException;

	public void rejectMakegoodComplaint(String makegood_sale_id) throws FDResourceException, RemoteException;

	public void resubmitOrder(String saleId, CustomerRatingI cra, EnumSaleType saleType)
			throws RemoteException, FDResourceException, ErpTransactionException;

	public void resubmitCustomer(String customerID) throws FDResourceException, RemoteException;

	public void scheduleRedelivery(String saleId, ErpRedeliveryModel redeliveryModel)
			throws FDResourceException, ErpTransactionException, RemoteException;

	public void emailCutoffTimeReport(java.util.Date day) throws FDResourceException, RemoteException;

	public void saveTopFaqs(List faqIds) throws FDResourceException, RemoteException;

	public void addNewIVRCallLog(CallLogModel callLogModel) throws FDResourceException, RemoteException;

	public void voidCaptureOrder(String saleId) throws RemoteException, FDResourceException, ErpTransactionException;

}
