/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer.ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.*;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.*;
import com.freshdirect.crm.CrmClick2CallModel;
import com.freshdirect.customer.*;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.RestrictionI;

import com.freshdirect.fdstore.content.meal.*;
import com.freshdirect.framework.util.GenericSearchCriteria;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface CallCenterManagerSB extends EJBObject {

	public Map getComplaintReasons(boolean excludeCartonReq) throws FDResourceException, RemoteException;
	
	public Map getComplaintCodes() throws FDResourceException, RemoteException;

	public void rejectMakegoodComplaint(String makegood_sale_id) throws FDResourceException, RemoteException; 
	
	public List getPendingComplaintOrders(String reasonCode) throws FDResourceException, RemoteException;
    
    public List getSignupPromoAVSExceptions() throws FDResourceException, RemoteException;

	public Collection getSupervisorApprovalCodes() throws FDResourceException, RemoteException;

	public Collection getFailedAuthorizationSales() throws FDResourceException, RemoteException;

	public void returnOrder(String saleId, ErpReturnOrderModel returnOrder) throws FDResourceException, ErpTransactionException, RemoteException;

	public EnumPaymentResponse resubmitPayment(String saleId, ErpPaymentMethodI payment, Collection charges) throws FDResourceException, ErpTransactionException, RemoteException;

	public void resubmitOrder(String saleId,CustomerRatingI cra,EnumSaleType saleType) throws RemoteException, FDResourceException, ErpTransactionException;
	
	public void resubmitCustomer(String customerID) throws FDResourceException, RemoteException;
	
	public List getNSMCustomers() throws FDResourceException, RemoteException;
	
	public List getNSMOrders(String date, String cutOff) throws FDResourceException, RemoteException; 
	
	public List getOrdersByStatus(String[] status) throws FDResourceException, RemoteException;
		
	public void approveReturn(String saleId, ErpReturnOrderModel returnOrder) throws FDResourceException, ErpTransactionException, RemoteException;
	
	public void scheduleRedelivery(String saleId, ErpRedeliveryModel redeliveryModel) throws FDResourceException, ErpTransactionException, RemoteException;
    
    public void changeRedeliveryToReturn(String saleId) throws FDResourceException, ErpTransactionException, ErpSaleNotFoundException, RemoteException;
    
    public List getHolidayMeals(FDIdentity identity) throws FDResourceException, RemoteException;
    
    public List locateCompanyCustomers(GenericSearchCriteria criteria) throws FDResourceException, RemoteException;
    
    public List orderSummarySearch(GenericSearchCriteria criteria) throws FDResourceException, RemoteException;
    
    public MealModel saveHolidayMeal(FDIdentity identity, String agent, MealModel meal) throws FDResourceException, RemoteException;
    
    public List getCreditSummaryForDate(Date date) throws FDResourceException, RemoteException;
  
	public void reverseCustomerCredit(String saleId, String complaintId) throws FDResourceException, ErpTransactionException, ErpComplaintException , RemoteException;
	
	public List getCutoffTimeForDate(Date date) throws FDResourceException, RemoteException;
	
	public List getCutoffTimeReport(java.util.Date day) throws FDResourceException, RemoteException;
	
	public void emailCutoffTimeReport(java.util.Date day) throws FDResourceException, RemoteException;

	public List getSubjectReport(java.util.Date day1,java.util.Date day2,boolean showAutoCases) throws FDResourceException, RemoteException;

	public List getLateDeliveryReport(java.util.Date date) throws FDResourceException, RemoteException;
	
	public List getRouteStopReport(java.util.Date date, String wave, String route, String stop1, String stop2) throws FDResourceException, RemoteException;
	
	public List getOrderStatusReport(String[] statusCodes) throws FDResourceException, RemoteException;
	
	public List getSettlementProblemReport(String[] statusCodes, String[] transactionTypes, Date failureStartDate, Date failureEndDate) throws FDResourceException, RemoteException;

	public List runComplaintReport(FDComplaintReportCriteria criteria) throws FDResourceException, RemoteException;
	
	public List runAuthInfoSearch(FDAuthInfoSearchCriteria criteria) throws FDResourceException, RemoteException; 
	
	public List getMakeGoodOrder(Date date) throws FDResourceException, RemoteException;
	
	public List doGenericSearch(GenericSearchCriteria criteria) throws FDResourceException, RemoteException;
	
	public int cancelReservations(GenericSearchCriteria resvCriteria, String initiator, String notes) throws FDResourceException, RemoteException;
	
	public int fixBrokenAccounts() throws FDResourceException, RemoteException;
	
	public Map returnOrders(FDActionInfo info, List returnOrders) throws FDResourceException, RemoteException;
	
	public int fixSettlemnentBatch(String batch_id) throws FDResourceException, RemoteException;	
	
	public void saveTopFaqs(List faqIds) throws FDResourceException, RemoteException;
	
	public void saveClick2CallInfo(CrmClick2CallModel click2CallModel) throws FDResourceException, RemoteException;
	
	public void saveClick2CallStatus(String id, String userId, boolean status) throws FDResourceException, RemoteException;
	
	public void createSnapShotForModifyOrders(GenericSearchCriteria criteria) throws FDResourceException, RemoteException;
	
	public void updateOrderModifiedStatus(String saleId, String status, String errorDesc) throws FDResourceException, RemoteException;
	
//	public CrmClick2CallModel getClick2CallInfo() throws FDResourceException, RemoteException;
	
}
