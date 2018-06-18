package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.VSReasonCodes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.meal.MealModel;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.GenericSearchCriteria;

public interface CallCenterManagerServiceI {

	public Map<String, List<ErpComplaintReason>> getComplaintReasons(boolean excludeCartonReq)
			throws FDResourceException, RemoteException;

	public Map<String, String> getComplaintCodes() throws FDResourceException, RemoteException;

	public void rejectMakegoodComplaint(String makegood_sale_id) throws FDResourceException, RemoteException;

	public EnumPaymentResponse resubmitPayment(String saleId, ErpPaymentMethodI payment, Collection charges)
			throws FDResourceException, ErpTransactionException, RemoteException;

	public void resubmitOrder(String saleId, CustomerRatingI cra, EnumSaleType saleType)
			throws RemoteException, FDResourceException, ErpTransactionException;

	public void resubmitCustomer(String customerID) throws FDResourceException, RemoteException;

	public void scheduleRedelivery(String saleId, ErpRedeliveryModel redeliveryModel)
			throws FDResourceException, ErpTransactionException, RemoteException;

	public void changeRedeliveryToReturn(String saleId)
			throws FDResourceException, ErpTransactionException, ErpSaleNotFoundException, RemoteException;

	public <E> List locateCompanyCustomers(GenericSearchCriteria criteria) throws FDResourceException, RemoteException;

	public List orderSummarySearch(GenericSearchCriteria criteria) throws FDResourceException, RemoteException;

	public MealModel saveHolidayMeal(FDIdentity identity, String agent, MealModel meal)
			throws FDResourceException, RemoteException;

	public List getCutoffTimeForDate(Date date) throws FDResourceException, RemoteException;

	public void emailCutoffTimeReport(java.util.Date day) throws FDResourceException, RemoteException;

	public <E> List doGenericSearch(GenericSearchCriteria criteria) throws FDResourceException, RemoteException;

	public void saveTopFaqs(List faqIds) throws FDResourceException, RemoteException;

	public void addNewIVRCallLog(CallLogModel callLogModel) throws FDResourceException, RemoteException;

	public void voidCaptureOrder(String saleId) throws RemoteException, FDResourceException, ErpTransactionException;

}
