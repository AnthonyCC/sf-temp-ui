/*
 * $Workfile:ErpFraudPreventionSessionBean.java$
 *
 * $Date:6/11/2003 5:03:05 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumFraudReason;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @version $Revision:26$
 * @author $Author:Kashif Nadeem$
 */
public class ErpFraudPreventionSessionBean extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ErpFraudPreventionSessionBean.class);
	private final static ServiceLocator LOCATOR = new ServiceLocator();

	private FraudDAO dao = new FraudDAO();

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.customer.ejb.ErpFraudHome";
	}

	public Set checkRegistrationFraud(ErpCustomerModel erpCustomer) {

		if (!"true".equalsIgnoreCase(ErpServicesProperties.getCheckForFraud())) {
			// no check, no problem :)
			return Collections.EMPTY_SET;
		}

		LOGGER.info("Fraud check, registration. userId=" + erpCustomer.getUserId());

		//
		// Find customer delivery address
		//
		Collection addresses = erpCustomer.getShipToAddresses();
		LOGGER.debug("num addresses @ registration = " + addresses.size());

		ErpAddressModel dlvAddress = null;
		for (Iterator it = addresses.iterator(); it.hasNext();) {
			dlvAddress = (ErpAddressModel) it.next();
			LOGGER.debug("address is " + dlvAddress);
			if (EnumServiceType.CORPORATE.equals(dlvAddress.getServiceType())) {
				continue;	
			}
			break;
		}
		LOGGER.debug("address is " + dlvAddress);

		Connection conn = null;
		Set fraudReasons = new HashSet();
		try {
			conn = getConnection();
			if (dao.isDuplicateShipToAddress(conn, dlvAddress, null)) {
				fraudReasons.add(EnumFraudReason.DUP_SHIPTO_ADDRESS);
			}
			if (dao.isDuplicatePhone(conn, erpCustomer.getCustomerInfo().getHomePhone())) {
				fraudReasons.add(EnumFraudReason.DUP_PHONE);
			}

		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
		return (fraudReasons);
	}

	public boolean checkShipToAddressFraud(String erpCustomerId, ErpAddressModel address) {

		if (!"true".equals(ErpServicesProperties.getCheckForFraud())) {
			// no check, no problem :)
			return false;
		}

		LOGGER.info("Fraud check, ship-to address. customerID=" + erpCustomerId);

		Connection conn = null;
		boolean retval = false;
		try {
			conn = getConnection();

			retval = dao.isDuplicateShipToAddress(conn, address, erpCustomerId);
			if (!retval) {
				dao.isDuplicatePhone(conn, address.getPhone(), erpCustomerId);
			}

		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
		return retval;
	}

	public boolean checkBillToAddressFraud(String erpCustomerId, ContactAddressModel address) {

		if (!"true".equals(ErpServicesProperties.getCheckForFraud())) {
			// no check, no problem :)
			return false;
		}

		LOGGER.info("Fraud check, bill-to address. customerID=" + erpCustomerId);

		Connection conn = null;
		boolean retval = false;
		try {
			conn = getConnection();

			retval = dao.isDuplicateBillToAddress(conn, address, erpCustomerId);
			if (!retval) {
				dao.isDuplicatePhone(conn, address.getPhone(), erpCustomerId);
			}
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
		return retval;
	}

	public boolean checkPhoneFraud(String erpCustomerId, Collection phones) {

		if (!"true".equals(ErpServicesProperties.getCheckForFraud())) {
			// no check, no problem :)
			return false;
		}

		LOGGER.info("Fraud check, phone. customerId=" + erpCustomerId);

		Connection conn = null;
		boolean retval = false;
		try {
			conn = getConnection();

			for (Iterator it = phones.iterator();(it.hasNext() && !retval);) {
				retval = dao.isDuplicatePhone(conn, (PhoneNumber) it.next(), erpCustomerId);
			}

		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
		return retval;
	}

	public boolean checkDuplicatePaymentMethodFraud(String erpCustomerId, ErpPaymentMethodI card) {

		if (!"true".equals(ErpServicesProperties.getCheckForFraud())) {
			// no check, no problem :)
			return false;
		}

		LOGGER.info("Fraud check, payment method. customerId=" + erpCustomerId);

		Connection conn = null;
		boolean retval = false;
		try {
			conn = getConnection();
			if (card.getAbaRouteNumber() != null && !"".equals(card.getAbaRouteNumber())) {
				retval = dao.isDuplicateAbaRouteAccountNumber(conn, card.getAccountNumber(), card.getAbaRouteNumber(), erpCustomerId);				
			} else {
				retval = dao.isDuplicateAccountNumber(conn, card.getAccountNumber(), erpCustomerId);
			}
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
		return retval;

	}

	/**
	 * @return null, or fraud reason
	 */
	public EnumFraudReason preCheckOrderFraud(
		PrimaryKey erpCustomerPk,
		ErpAbstractOrderModel order,
		CrmAgentRole agentRole) {

		if (!"true".equalsIgnoreCase(ErpServicesProperties.getCheckForFraud())) {
			// no check, no problem :)
			LOGGER.info("Returning Null at start of preCheckOrderFraud");
			return null;
		}

		LOGGER.info("Fraud check, order. customerPK=" + erpCustomerPk);

		//
		// CHECK DUPLICATE ACCOUNT #
		//
		ErpPaymentMethodI paymentMethod = (ErpPaymentMethodI) order.getPaymentMethod();
		if ("true".equalsIgnoreCase(ErpServicesProperties.getCheckForPaymentMethodFraud()) && !paymentMethod.isGiftCard()) {
			boolean dupCC = this.checkDuplicatePaymentMethodFraud(erpCustomerPk.getId(), paymentMethod);

			if (dupCC) {
				return EnumFraudReason.DUP_ACCOUNT_NUMBER;
			}
		}

		//
		// ORDER TOTAL check (TOTAL > $450 OR TOTAL > $750)
		//
		if (checkOrderTotal(null,erpCustomerPk, order,false)) {
			return EnumFraudReason.MAX_ORDER_TOTAL;
		}
		
		if (preCheckMakeGoodLimits(erpCustomerPk, order, agentRole)) {
			return EnumFraudReason.MAX_MAKEGOOD;
		}
	
		return null;
	}


	private final static Set SUPERAGENT_ROLES = new HashSet(Arrays.asList(new String[] {
		CrmAgentRole.SUP_CODE,
		CrmAgentRole.ADM_CODE,
		CrmAgentRole.SYS_CODE}));

	/**
	 * @return true if order total violates make-good limit
	 */
	private boolean preCheckMakeGoodLimits(PrimaryKey erpCustomerPk, ErpAbstractOrderModel order, CrmAgentRole agentRole) {
		if (!EnumPaymentType.MAKE_GOOD.equals(order.getPaymentMethod().getPaymentType())) {
			return false;
		}

		if (agentRole!=null && SUPERAGENT_ROLES.contains(agentRole.getCode())) {
			return false;
		}
		
		if (order.getAmount() <= ErpServicesProperties.getCreditAutoApproveAmount()) {
			return false;
		}
		
		Connection conn = null;
		try {
			conn = getConnection();

			double refSaleAmount = ErpSaleInfoDAO.getOrderAmount(conn, erpCustomerPk.getId(), order.getPaymentMethod().getReferencedOrder());
			if (order.getAmount() > refSaleAmount) {
				return true;
			}
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
		
/*
		ErpSaleInfo refSale = orderHistory.getSale(order.getPaymentMethod().getReferencedOrder());
		if (refSale == null) {
			return false;
		}

		if (order.getAmount() > refSale.getAmount()) {
			return true;
		}
*/		
		return false;
	}
	
	
	private void postCheckMakeGoodLimits(
		PrimaryKey salePk,
		PrimaryKey erpCustomerPk,
		ErpAbstractOrderModel order,
		CrmAgentRole agentRole) {
		if (!EnumPaymentType.MAKE_GOOD.equals(order.getPaymentMethod().getPaymentType())) {
			return;
		}

		if (agentRole!=null && SUPERAGENT_ROLES.contains(agentRole.getCode())) {
			return;
		}


		if (order.getAmount() > ErpServicesProperties.getCreditAutoApproveAmount()) {

			NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
			String fmtAmount = currencyFormatter.format(order.getAmount());
			
			CrmCaseSubject subject = CrmCaseSubject.getEnum(CrmCaseSubject.CODE_ORDER_OVER_MAX);
			String summary = "Verify make-good order for " + fmtAmount;
			LOGGER.debug("Creating case " + summary);
			String note = "Make-good order total of " + fmtAmount + " indicates possible fraud condition. Please investigate.";

			CrmSystemCaseInfo info = new CrmSystemCaseInfo(erpCustomerPk, salePk, subject, summary);
			info.setNote(note);
			new ErpCreateCaseCommand(LOCATOR, info).execute();

		}

	}

	/**
	 *  do fraud checks that create cases, now that the order is created(or not) but case will not be rolled back
	 * @return void
	 */
	public void postCheckOrderFraud(PrimaryKey salePk, PrimaryKey erpCustomerPk, ErpAbstractOrderModel order, CrmAgentRole agentRole) {
		if (!"true".equalsIgnoreCase(ErpServicesProperties.getCheckForFraud())) {
			// no check, no problem :)
			return ;
		}
		//Check if its first order using Gift card and total > 450.

		Connection conn = null;
		try {
			conn = getConnection();
		
		int orderCount=ErpSaleInfoDAO.getPreviousOrderHistory(conn,erpCustomerPk.getId());
		if(order.getAppliedGiftCardAmount() > 0.0 && orderCount == 0){
			//Check fraud for first order using GC.
			checkFirstOrderUsingGC(salePk, erpCustomerPk, order);
		}else{
			
			//
			// ORDER TOTAL check (TOTAL > $450 OR TOTAL > $750)
			//
			checkOrderTotal(salePk,erpCustomerPk, order,true);
		}
		postCheckMakeGoodLimits(salePk, erpCustomerPk, order, agentRole);
		
			//
			// 3 ORDERS WITHIN 3 DAYS check
			//
			checkOrdersIn3Days(conn, salePk,erpCustomerPk, order);

			return ;
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
	}


	/**
	 * Checks for orders over acceptable threshhold. Creates a CRM case if order total > $450 ($750 for COS)
	 * @return true if order total > $750 and not COS/CSR
	 */
	private boolean checkOrderTotal(PrimaryKey salePk,PrimaryKey erpCustomerPk, ErpAbstractOrderModel order,boolean isOkToCreateCase) {
		boolean retval = false;
		boolean createCase = false;

		final boolean corporate = EnumDeliveryType.CORPORATE.equals(order.getDeliveryInfo().getDeliveryType());
		final boolean csr = EnumTransactionSource.CUSTOMER_REP.equals(order.getTransactionSource());

		if (order.getAmount() > 750 && !csr) {
			retval = !corporate;
			createCase = true && isOkToCreateCase;

		} else if (order.getAmount() > 450 && !corporate) {
			createCase = true & isOkToCreateCase;
		}

		if (createCase) {

			NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

			CrmCaseSubject subject = CrmCaseSubject.getEnum(CrmCaseSubject.CODE_ORDER_OVER_MAX);
			String summary = subject.getDescription() + " because order total = " + currencyFormatter.format(order.getAmount());
			LOGGER.debug("Creating case " + summary);
			String note =
				"Order total of "
					+ currencyFormatter.format(order.getAmount())
					+ " indicates possible fraud condition. Please investigate.";

			CrmSystemCaseInfo info = new CrmSystemCaseInfo(erpCustomerPk,salePk, subject, summary);
			info.setNote(note);
			new ErpCreateCaseCommand(LOCATOR, info).execute();
		}
		return retval;
	}
	
	/**
	 * Checks for orders over acceptable threshhold. Creates a CRM case if order total > $750 
	 * @return true if order total > $750 and not for CSR
	 */
	
	public void postCheckGiftCardFraud(PrimaryKey salePk, PrimaryKey erpCustomerPk, ErpAbstractOrderModel order, CrmAgentRole agentRole) {
		if (!"true".equalsIgnoreCase(ErpServicesProperties.getCheckForFraud())) {
			// no check, no problem :)
			return ;
		}
		
		checkGiftCardOrderTotal(salePk, erpCustomerPk, order, true);
	}
	/**
	 * Checks for orders over $750 and not for CSR, create a crm case.
	 */
	private boolean checkGiftCardOrderTotal(PrimaryKey salePk,PrimaryKey erpCustomerPk, ErpAbstractOrderModel order,boolean isOkToCreateCase) {
		boolean retval = false;
		boolean createCase = false;

		final boolean corporate = EnumDeliveryType.CORPORATE.equals(order.getDeliveryInfo().getDeliveryType());
		final boolean csr = EnumTransactionSource.CUSTOMER_REP.equals(order.getTransactionSource());

		if (order.getAmount() > ErpServicesProperties.getGiftCardOrderLimit() && !csr) {
			createCase = true & isOkToCreateCase;
			retval=true;
		}

		if (createCase) {

			NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

			CrmCaseSubject subject = CrmCaseSubject.getEnum(CrmCaseSubject.CODE_GC_ORDER_OVER_MAX);
			String summary = " because Gift Card order total = " + currencyFormatter.format(order.getAmount());
			LOGGER.debug("Creating case " + summary);
			String note =
				"Gift Card Order total of "
					+ currencyFormatter.format(order.getAmount())
					+ " indicates possible fraud condition. Please investigate.";

			CrmSystemCaseInfo info = new CrmSystemCaseInfo(erpCustomerPk,salePk, subject, summary);
			info.setNote(note);
			new ErpCreateCaseCommand(LOCATOR, info).execute();
		}
		return retval;
	}

	

	/**
	 * Checks for 3 orders placed within three days of current order
	 */
	private void checkOrdersIn3Days(Connection conn, PrimaryKey salePk,PrimaryKey erpCustomerPk, ErpAbstractOrderModel order) throws SQLException {
		/*
		Collection orderHistory = ErpSaleInfoDAO.getOrderHistoryInfo(conn, erpCustomerPk.getId());
		LOGGER.debug("checking order history (" + orderHistory.size() + " orders) for 3 orders in last 3 days");

		Date threeDaysAgo = DateUtil.addDays(DateUtil.truncate(new Date()), -3);

		int ordersInThreeDays = order instanceof ErpModifyOrderModel ? 0 : 1;

		for (Iterator it = orderHistory.iterator(); it.hasNext() && ordersInThreeDays < 3;) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) it.next();
			if (saleInfo.getCreateDate().after(threeDaysAgo) && (salePk==null || !saleInfo.getSaleId().equals(salePk.getId()) )) {
				ordersInThreeDays++;
				LOGGER.debug(saleInfo.getSaleId() + " is within last 3 days");
			}
		}
		LOGGER.debug("Found " + ordersInThreeDays + " orders");
		*/
		Date threeDaysAgo = DateUtil.addDays(DateUtil.truncate(new Date()), -3);
		int ordersInThreeDays = ErpSaleInfoDAO.getOrderCountPast(conn, erpCustomerPk.getId(), threeDaysAgo,EnumSaleType.REGULAR);
		LOGGER.debug("Found " + ordersInThreeDays + " orders in past 3 days");
		if (ordersInThreeDays >= 3) {
			CrmSystemCaseInfo info =
				new CrmSystemCaseInfo(
					erpCustomerPk,
					CrmCaseSubject.getEnum(CrmCaseSubject.CODE_OVER_3_PLUS_DAYS),
					"Found " + ordersInThreeDays + " orders placed within 3 consecutive days.");
			new ErpCreateCaseCommand(LOCATOR, info).execute();

		}

	}
	
	
	
	public EnumFraudReason preCheckGiftCardFraud(PrimaryKey erpCustomerPk, ErpAbstractOrderModel order, CrmAgentRole agentRole){
		

		if (!"true".equalsIgnoreCase(ErpServicesProperties.getCheckForFraud())) {
			// no check, no problem :)
			LOGGER.info("Returning Null at start of preCheckOrderFraud");
			return null;
		}

		LOGGER.info("Fraud check, order. customerPK=" + erpCustomerPk);

		//
		// CHECK DUPLICATE ACCOUNT #
		// 
		if ("true".equalsIgnoreCase(ErpServicesProperties.getCheckForPaymentMethodFraud())) {

			ErpPaymentMethodI paymentMethod = (ErpPaymentMethodI) order.getPaymentMethod();
			boolean dupCC = this.checkDuplicatePaymentMethodFraud(erpCustomerPk.getId(), paymentMethod);

			if (dupCC) {
				return EnumFraudReason.DUP_ACCOUNT_NUMBER;
			}
		}

		final boolean csr = EnumTransactionSource.CUSTOMER_REP.equals(order.getTransactionSource());
		//
		// ORDER TOTAL check (TOTAL > $5000)
		//		
		if (order.getAmount() > ErpServicesProperties.getGiftCardStrictOrderLimit() && !csr) {
			return EnumFraudReason.MAX_GC_ORDER_TOTAL;
		}

		try{
		Date threeDaysAgo = DateUtil.addDays(DateUtil.truncate(new Date()), -1);
		int ordersInOneDays = ErpSaleInfoDAO.getOrderCountPast(getConnection(), erpCustomerPk.getId(), threeDaysAgo,EnumSaleType.GIFTCARD);
		LOGGER.debug("Found " + ordersInOneDays + " orders in past 24 hrs");
		if(ordersInOneDays > ErpServicesProperties.getGiftCardOrderCountLimit() && !csr){
			return EnumFraudReason.MAX_ORDER_COUNT_LIMIT;
		}
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());
		}
		return null;
		
	}

	private void checkFirstOrderUsingGC(PrimaryKey salePk,PrimaryKey erpCustomerPk, ErpAbstractOrderModel order) throws SQLException {
		//final boolean csr = EnumTransactionSource.CUSTOMER_REP.equals(order.getTransactionSource());
		if (order.getDeliveryInfo().getDeliveryType().equals(EnumDeliveryType.PICKUP) ) {
			LOGGER.debug("First Order placed using Gift Card scheduled for Pick up. Order Number : "+salePk);
			CrmSystemCaseInfo info =
				new CrmSystemCaseInfo(
					erpCustomerPk,
					CrmCaseSubject.getEnum(CrmCaseSubject.CODE_FIRST_ORDER_FOR_PICK_UP_USED_GC),
					"First Order placed using Gift Card scheduled for Pick up. Order Number : "+salePk );
			new ErpCreateCaseCommand(LOCATOR, info).execute();

		}
		if (order.getAmount() > 450 ) {
			LOGGER.debug("First Order placed using Gift Card and Order Total over $450. Order Number : "+salePk);
			CrmSystemCaseInfo info =
				new CrmSystemCaseInfo(
					erpCustomerPk,
					CrmCaseSubject.getEnum(CrmCaseSubject.CODE_FIRST_ORDER_OVER_MAX_USED_GC),
					"First Order placed using Gift Card and Order Total over $450. Order Number : "+salePk );
			new ErpCreateCaseCommand(LOCATOR, info).execute();

		}

	}
	
	
}
