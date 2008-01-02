/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.content.meal.MealModel;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthInfoSearchCriteria;
import com.freshdirect.fdstore.customer.FDComplaintReportCriteria;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.customer.ejb.CallCenterManagerHome;
import com.freshdirect.fdstore.customer.ejb.CallCenterManagerSB;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Singleton class for accessing functionality in ERP Services.
 *
 * @version $Revision$
 * @author $Author$
 */
public class CallCenterServices {

	private static Category LOGGER = LoggerFactory.getInstance(CallCenterServices.class);

	private static CallCenterManagerHome callCenterHome = null;


	public static Map getComplaintReasons() throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getComplaintReasons();
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	//get complaint codes
	public static Map getComplaintCodes() throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getComplaintCodes();
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getPendingComplaintOrders(String reasonCode) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getPendingComplaintOrders(reasonCode);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static List runComplaintReport(FDComplaintReportCriteria criteria) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.runComplaintReport(criteria);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}	
	}
	
	public static List locateCompanyCustomers(GenericSearchCriteria criteria) throws FDResourceException {
		if(callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.locateCompanyCustomers(criteria);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static List orderSummarySearch(GenericSearchCriteria criteria) throws FDResourceException {
		if(callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.orderSummarySearch(criteria);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static List runAuthInfoSearch(FDAuthInfoSearchCriteria criteria) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.runAuthInfoSearch(criteria);
		} catch (CreateException ce) {
			callCenterHome = null;
			LOGGER.debug("CreateException: ", ce);
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			LOGGER.debug("RemoteException: ", re);
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getSignupPromoAVSExceptions() throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getSignupPromoAVSExceptions();
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getCreditSummaryForDate(Date date) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getCreditSummaryForDate(date);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getSubjectReport(Date date1, Date date2, boolean showAutoCases) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getSubjectReport(date1,date2,showAutoCases);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static List getLateDeliveryReport(Date date) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getLateDeliveryReport(date);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static List getRouteStopReport(Date date, String wave, String route, String stop1, String stop2) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getRouteStopReport(date, wave, route, stop1, stop2);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Collection getSupervisorApprovalCodes() throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getSupervisorApprovalCodes();
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @return Collection of FDOrderAdapters
	 */
	public static Collection getFailedAuthorizationSales() throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			Collection sales = sb.getFailedAuthorizationSales();

			Collection orders = new ArrayList(sales.size());
			for (Iterator it = sales.iterator(); it.hasNext();) {
				orders.add(new FDOrderAdapter((ErpSaleModel) it.next()));
			}
			return orders;
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void resubmitOrder(String saleId,CustomerRatingI cra) throws FDResourceException, ErpTransactionException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			sb.resubmitOrder(saleId,cra);

		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void resubmitCustomer(String customerID) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			sb.resubmitCustomer(customerID);

		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void returnOrder(FDIdentity identity, String saleId, ErpReturnOrderModel returnOrder)
		throws FDResourceException, ErpTransactionException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			if (!FDCustomerManager.orderBelongsToUser(identity, saleId)) {
				throw new FDResourceException("Order not found in current user's order history.");
			}
			CallCenterManagerSB sb = callCenterHome.create();
			sb.returnOrder(saleId, returnOrder);

		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void changeRedeliveryToReturn(FDIdentity identity, String saleId)
		throws FDResourceException, ErpTransactionException, ErpSaleNotFoundException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			if (!FDCustomerManager.orderBelongsToUser(identity, saleId)) {
				throw new FDResourceException("Order not found in current user's order history.");
			}
			CallCenterManagerSB sb = callCenterHome.create();
			sb.changeRedeliveryToReturn(saleId);

		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void approveReturn(FDIdentity identity, String saleId, ErpReturnOrderModel returnOrder)
		throws FDResourceException, ErpTransactionException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			if (!FDCustomerManager.orderBelongsToUser(identity, saleId)) {
				throw new FDResourceException("Order not found in current user's order history.");
			}
			CallCenterManagerSB sb = callCenterHome.create();
			sb.approveReturn(saleId, returnOrder);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void scheduleRedelivery(FDIdentity identity, String saleId, ErpRedeliveryModel redeliveryModel)
		throws FDResourceException, ErpTransactionException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			if (!FDCustomerManager.orderBelongsToUser(identity, saleId)) {
				throw new FDResourceException("Order not found in current user's order history.");
			}
			CallCenterManagerSB sb = callCenterHome.create();
			sb.scheduleRedelivery(saleId, redeliveryModel);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating CallCenterManager session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to CallCenterManager session bean");
		}
	}
	
	public static List getOrdersByStatus(EnumSaleStatus status) throws FDResourceException {
		String[] s = {status.getStatusCode()};
		return getOrderByStatus(s);
	}
	
	public static List getOrderByStatus(String[] status) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getOrdersByStatus(status);

		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to bean");
		}
	}
	
	public static List getNSMCustomers() throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getNSMCustomers();

		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to bean");
		}
	}
	
	public static List getNSMOrders() throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getNSMOrders();
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to bean");
		}
	}

	public static EnumPaymentResponse resubmitPayment(String saleId, ErpPaymentMethodI payment, Collection charges)
		throws FDResourceException, ErpTransactionException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.resubmitPayment(saleId, payment, charges);

		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getHolidayMeals(FDIdentity identity) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getHolidayMeals(identity);

		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static MealModel saveHolidayMeal(FDIdentity identity, String agent, MealModel meal) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.saveHolidayMeal(identity, agent, meal);

		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void reverseCustomerCredit(String saleId, String complaintId)
		throws FDResourceException, ErpTransactionException, ErpComplaintException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			sb.reverseCustomerCredit(saleId, complaintId);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getCutoffTimeForDate(Date date) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getCutoffTimeForDate(date);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getCutoffTimeReport(java.util.Date day) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getCutoffTimeReport(day);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static void emailCutoffTimeReport(java.util.Date day) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			sb.emailCutoffTimeReport(day);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	protected static void lookupManagerHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			callCenterHome = (CallCenterManagerHome) ctx.lookup(FDStoreProperties.getCallCenterManagerHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}

	/**
	 * Utility method for determining whether a given order belongs to a given user.
	 * @param FDIdentity current user
	 * @param String sale id
	 * @return boolean
	 */
/*	
    This method is obsolete. Replaced by FDCustomerManager.orderBelongsToUser(FDIdentity identity, String saleId)
    
    private static boolean orderBelongsToUser(FDIdentity identity, String saleId) throws FDResourceException {
		Collection orders = FDCustomerManager.getOrderHistoryInfo(identity).getFDOrderInfos();
		for (Iterator it = orders.iterator(); it.hasNext();) {
			FDOrderInfoI orderInfo = (FDOrderInfoI) it.next();
			if (orderInfo.getErpSalesId().equals(saleId)) {
				LOGGER.debug("verified order belongs to user");
				return true;
			}
		}
		return false;
	}*/
	
	public static List getOrderStatusReport(String[] statusCodes) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getOrderStatusReport(statusCodes);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static List getSettlementProblemReport(String[] statusCodes, String [] transactionTypes, Date failureStartDate, Date failureEndDate) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getSettlementProblemReport(statusCodes, transactionTypes, failureStartDate, failureEndDate);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List getMakeGoodOrder(Date date) throws FDResourceException { 
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.getMakeGoodOrder(date);
		} catch (CreateException ce) {
			callCenterHome = null;
			LOGGER.debug("CreateException: ", ce);
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			LOGGER.debug("RemoteException: ", re);
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List doGenericSearch(GenericSearchCriteria criteria) throws FDResourceException {
		if(callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.doGenericSearch(criteria);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static int cancelReservations(GenericSearchCriteria resvCriteria, String initiator, String notes) throws FDResourceException {
		if(callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.cancelReservations(resvCriteria, initiator, notes);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static int fixBrokenAccounts() throws FDResourceException {
		if(callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.fixBrokenAccounts();
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static Map returnOrders(FDActionInfo info, List returnOrders) throws FDResourceException {
		if (callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.returnOrders(info, returnOrders);
	
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	public static int fixSettlemnentBatch(String batch_id) throws FDResourceException {
		if(callCenterHome == null) {
			lookupManagerHome();
		}
		try {
			CallCenterManagerSB sb = callCenterHome.create();
			return sb.fixSettlemnentBatch(batch_id);
		} catch (CreateException ce) {
			callCenterHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			callCenterHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


} // class CallCenterServices