/*
 * $Workfile:ErpCustomerManagerSessionBean.java$
 *
 * $Date:8/29/2003 11:07:09 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.crm.ejb.CustomerEmailDAO;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumAlertType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumComplaintLineMethod;
import com.freshdirect.customer.EnumComplaintLineType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumFraudReason;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAdjustmentModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpCancelOrderModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpChargeInvoiceModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintInfoModel;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpCustomerAlertModel;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.customer.ErpCustomerEmailModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpInvoicedCreditModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpResubmitPaymentModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpWebOrderHistory;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.customer.adapter.CustomerAdapter;
import com.freshdirect.customer.adapter.SapOrderAdapter;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.giftcard.GiftCardApplicationStrategy;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.EnumRestrictionReason;
import com.freshdirect.payment.fraud.PaymentFraudManager;
import com.freshdirect.sap.SapCustomerI;
import com.freshdirect.sap.SapOrderLineI;
import com.freshdirect.sap.command.SapPostReturnCommand;
import com.freshdirect.sap.ejb.SapGatewayHome;
import com.freshdirect.sap.ejb.SapGatewaySB;

/**
 *
 *
 * @version $Revision:116$
 * @author $Author:Mike Rose$
 */
public class ErpCustomerManagerSessionBean extends SessionBeanSupport {

	private final static Category LOGGER = LoggerFactory.getInstance(ErpCustomerManagerSessionBean.class);
	private final static ServiceLocator LOCATOR = new ServiceLocator();


	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.customer.ejb.ErpCustomerManagerHome";
	}

	
	public PrimaryKey createCustomer(ErpCustomerModel erpCustomer) throws ErpDuplicateUserIdException {
		return createCustomer(erpCustomer, false);
	}
	
	/**
	 * Create an ErpCustomer and enqueue request in SAP.
	 *
	 * @param erpCustomer ErpCustomerModel
	 *
	 * @return primary key assigned to ErpCustomer
	 */
	public PrimaryKey createCustomer(ErpCustomerModel erpCustomer, boolean isGiftCardBuyer) throws ErpDuplicateUserIdException {
		try {

			LOGGER.info("Creating customer - start. userId=" + erpCustomer.getUserId());

			ErpCustomerEB customerEB = this.getErpCustomerHome().create(erpCustomer);
			PrimaryKey customerPK = customerEB.getPK();

			this.enqueueCustomer(erpCustomer, customerPK, isGiftCardBuyer);

			//this.doEmail( ErpEmailFactory.createSignupConfirmEmail(erpCustomer) );

			LOGGER.info("Creating customer - success. userId=" + erpCustomer.getUserId() + " customerPK=" + customerPK);
			return customerPK;

		} catch (DuplicateKeyException de) {
			this.getSessionContext().setRollbackOnly();
			throw new ErpDuplicateUserIdException("User id already exists");

		} catch (CreateException ex) {
			throw new EJBException(ex.getMessage());

		} catch (RemoteException ex) {
			throw new EJBException(ex);
		}

	}
	
	private void enqueueCustomer(ErpCustomerModel erpCustomer, PrimaryKey customerPK) {
		enqueueCustomer(erpCustomer, customerPK, false);
	}

	private void enqueueCustomer(ErpCustomerModel erpCustomer, PrimaryKey customerPK, boolean isGiftCardBuyer) {
		try {
			// transform ErpCustomerModel into CustomerI
			// send the ship-to addr as the bill-to
			SapCustomerI customer = null;
			if(isGiftCardBuyer) {
				customer = new CustomerAdapter(false, erpCustomer, null, erpCustomer.getSapBillToAddress());
			} else {
				customer = new CustomerAdapter(false, erpCustomer, null, (ErpAddressModel) erpCustomer.getShipToAddresses().get(0));
			}

			SapGatewaySB sapSB = this.getSapGatewayHome().create();
			sapSB.sendCreateCustomer(customerPK.getId(), customer);

		} catch (CreateException ex) {
			throw new EJBException(ex.getMessage());

		} catch (RemoteException ex) {
			throw new EJBException(ex);
		}
	} 

	/**
	 * Creates sale object and enqueues order in SAP.
	 *
	 * @param erpCustomerPk primary key of ErpCustomer
	 * @param order the create order transaction
	 *
	 * @return erp sale PK
	 */
	public PrimaryKey placeOrder(
		PrimaryKey erpCustomerPk,
		ErpCreateOrderModel order,
		Set usedPromotionCodes,
		CustomerRatingI rating,
		CrmAgentRole agentRole,
		String dlvPassId,
		EnumSaleType saleType) throws ErpFraudException {

		
		LOGGER.info("Placing order - start. CustomerPK=" + erpCustomerPk);

		try {
			//
			// Prevent deactivated customers from placing orders
			//
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(erpCustomerPk);
			if (!customerEB.isActive()) {
				SessionContext ctx = this.getSessionContext();
				ctx.setRollbackOnly();
				throw new ErpFraudException(EnumFraudReason.DEACTIVATED_ACCOUNT);
			}
			// comment out this unnecessary call
			//ErpOrderHistory orderHistory = getOrderHistoryInfo(erpCustomerPk);
						
			//
			// Do fraud check
			//
			
			if(saleType==EnumSaleType.GIFTCARD){
				preCheckGiftCardOrderFraud(erpCustomerPk, order, agentRole);
			}else if(saleType==EnumSaleType.DONATION){
				preCheckDonationFraud(erpCustomerPk, order, agentRole);
			}else
			   preCheckOrderFraud(erpCustomerPk, order, agentRole);
			
			//
			// store order in ERPS and send message to SAP (via JMS)
			//

			LOGGER.info("Placing order - store in ERPS. CustomerPK=" + erpCustomerPk);
			ErpSaleEB saleEB = this.getErpSaleHome().create(erpCustomerPk, order, usedPromotionCodes, dlvPassId,saleType);
			PrimaryKey salePK = saleEB.getPK();
		
			ErpAbstractOrderModel orderModel = saleEB.getCurrentOrder();
			if(saleType==EnumSaleType.GIFTCARD){
				postCheckGiftCardFraud(salePK,erpCustomerPk, orderModel, agentRole);	
			}else if(saleType==EnumSaleType.DONATION){
				postCheckDonationFraud(salePK,erpCustomerPk, orderModel, agentRole);
			}
			else {
				postCheckOrderFraud(salePK,erpCustomerPk, orderModel, agentRole);// perform				
			}
			
			if(EnumSaleType.REGULAR.equals(saleType)) {
				SapOrderAdapter sapOrder = this.adaptOrder(erpCustomerPk, orderModel, rating);
				SapGatewaySB sapSB = this.getSapGatewayHome().create();
				sapOrder.setWebOrderNumber(salePK.getId());
				sapSB.sendCreateSalesOrder(sapOrder,saleType);
				
			}
			this.reconcileCustomerCredits(erpCustomerPk, order);
			LOGGER.info("Placing order - successful. CustomerPK=" + erpCustomerPk);

			return salePK;
		} catch (CreateException ce) {
			throw new EJBException(ce);
		} catch (FinderException fe) {
			throw new EJBException(fe);
		} catch (RemoteException re) {
			throw new EJBException(re);
		}
	}
	
	public void createCase(CrmSystemCaseInfo caseInfo, boolean requiresNewTx) {
		ErpCreateCaseCommand cmd = new ErpCreateCaseCommand(LOCATOR, caseInfo);
		cmd.setRequiresNewTx(requiresNewTx);
		cmd.execute();
	}

	/**
	 * Resubmit a failed ("not submitted") order to SAP.
	 * If there's an SAP ID assigned, it calls change order, otherwise it calls create.
	 *
	 * @param saleId
	 * @throws ErpTransactionException if the order is not in the NOT_SUBMITTED, NEW or MODIFIED state
	 */
	public void resubmitOrder(String saleId,CustomerRatingI cra,EnumSaleType saleType) throws ErpTransactionException {
		try {
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));

			// check state
			EnumSaleStatus status = saleEB.getStatus();
			if (!(EnumSaleStatus.NOT_SUBMITTED.equals(status)
				|| EnumSaleStatus.NEW.equals(status)
				|| EnumSaleStatus.MODIFIED.equals(status)
				|| EnumSaleStatus.MODIFIED_CANCELED.equals(status))) {
				this.getSessionContext().setRollbackOnly();
				throw new ErpTransactionException("Sale not in NSM, NEW, MOD or MOC state");
			}

			LOGGER.info("Resubmitting order. saleID=" + saleId + " status=" + status.getName());
			String sapOrderNumber = saleEB.getSapOrderNumber();

			SapGatewaySB sapSB = this.getSapGatewayHome().create();

			if (saleEB.getStatus().isCanceled()) {
				sapSB.sendCancelSalesOrder(saleId, sapOrderNumber);
				return;
			}

			ErpAbstractOrderModel order = saleEB.getCurrentOrder();

			PrimaryKey customerPk = saleEB.getCustomerPk();
			SapOrderAdapter sapOrder = this.adaptOrder(customerPk, order, cra);
			sapOrder.setWebOrderNumber(saleId);

			if (sapOrderNumber == null) {
				// it's not in SAP yet, create it
				sapSB.sendCreateSalesOrder(sapOrder,saleType);
			} else {
				// it's already in SAP, so call change
				sapSB.sendChangeSalesOrder(saleId, sapOrderNumber, sapOrder);
			}

		} catch (CreateException ce) {
			throw new EJBException(ce);
		} catch (FinderException fe) {
			throw new EJBException(fe);
		} catch (RemoteException re) {
			throw new EJBException(re);
		}
	}

	/**
	 * Cancels a sale, enqueues request in SAP.
	 *
	 * @param saleId
	 * @return String reservation id of the canceled order so it can be releases
	 */
	public String cancelOrder(String saleId, EnumTransactionSource source,String initiator) throws ErpTransactionException {
		String reservationId = "";
		try {

			LOGGER.info("Cancel order - start. SaleID=" + saleId);

			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			PrimaryKey customerPk = saleEB.getCustomerPk();
			// !!! do something with potential creditcard authorization ?

			//
			// Locate and reverse any potential applied credits
			//
			HashMap creditsMap = new HashMap();

			ErpAbstractOrderModel order = saleEB.getCurrentOrder();
			

			for (Iterator i = order.getAppliedCredits().iterator(); i.hasNext();) {
				ErpAppliedCreditModel ac = (ErpAppliedCreditModel) i.next();
				creditsMap.put(ac.getCustomerCreditPk().getId(), new Double(ac.getAmount()));
			}
            
			
			

			ErpCancelOrderModel cancelOrder = new ErpCancelOrderModel();
			cancelOrder.setTransactionSource(source);
			cancelOrder.setTransactionInitiator(initiator);
			saleEB.cancelOrder(cancelOrder);
			ErpSaleModel sale=(ErpSaleModel)saleEB.getModel();
			if(EnumSaleType.REGULAR.equals(sale.getType())) {

				reservationId = order.getDeliveryInfo().getDeliveryReservationId();
				reverseAppliedCredits(customerPk, creditsMap);
				SapGatewaySB sapSB = this.getSapGatewayHome().create();
				sapSB.sendCancelSalesOrder(saleId, saleEB.getSapOrderNumber());
				return reservationId;
			} else {
				saleEB.cancelOrderComplete();
				return "";
			}

		} catch (CreateException ce) {
			throw new EJBException(ce);
		} catch (FinderException fe) {
			throw new EJBException(fe);
		} catch (RemoteException re) {
			throw new EJBException(re);
		}
	}

	public void modifyOrder(
			String saleId,
			ErpModifyOrderModel order,
			Set usedPromotionCodes,
			CustomerRatingI cra,
			CrmAgentRole agentRole,
			boolean sendToSap) throws ErpFraudException, ErpTransactionException {

		try {

			LOGGER.info("Modify order - start. saleId=" + saleId);

			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			
			ErpSaleModel sale = (ErpSaleModel) saleEB.getModel();
			PrimaryKey erpCustomerPk = sale.getCustomerPk();

			//comment out this unnecessary call
			//ErpOrderHistory orderHistory = getOrderHistoryInfo(erpCustomerPk);
			preCheckOrderFraud(erpCustomerPk, order, agentRole);

			// reverse credits applied to the last order Transaction
			ErpAbstractOrderModel originalOrder = saleEB.getCurrentOrder();
			List credits = originalOrder.getAppliedCredits();
			HashMap creditsMap = new HashMap();
			for (Iterator i = credits.iterator(); i.hasNext();) {
				ErpAppliedCreditModel ac = (ErpAppliedCreditModel) i.next();
				creditsMap.put(ac.getCustomerCreditPk().getId(), new Double(ac.getAmount()));
			}
			reverseAppliedCredits(sale.getCustomerPk(), creditsMap);

			//add the transaction
			saleEB.modifyOrder(order, usedPromotionCodes);

			ErpAbstractOrderModel orderModel = saleEB.getCurrentOrder();
			postCheckOrderFraud(saleEB.getPK(),erpCustomerPk, orderModel, agentRole); 

			if (sendToSap) { 
				SapOrderAdapter sapOrder = this.adaptOrder(erpCustomerPk, orderModel, cra);
				sapOrder.setWebOrderNumber(saleId);
				// !!! log activity

				SapGatewaySB sapSB = this.getSapGatewayHome().create();

				sapSB.sendChangeSalesOrder(saleId, sale.getSapOrderNumber(), sapOrder);
			}
			
			reconcileCustomerCredits(erpCustomerPk, order);

			LOGGER.info("Modify order - successful. saleId=" + saleId);
		} catch (CreateException ce) {
			throw new EJBException(ce);
		} catch (FinderException ce) {
			throw new EJBException(ce);
		} catch (RemoteException re) {
			throw new EJBException(re);
		}

	}
	

	private void preCheckGiftCardOrderFraud(
			PrimaryKey erpCustomerPk,
			ErpAbstractOrderModel order,
			CrmAgentRole agentRole) throws CreateException, RemoteException, ErpFraudException {
			
			ErpFraudPreventionSB fraudSB = getErpFraudHome().create();
			EnumFraudReason fraud = fraudSB.preCheckGiftCardFraud(erpCustomerPk, order, agentRole);
			if (fraud != null) {
				SessionContext ctx = this.getSessionContext();
				ctx.setRollbackOnly();
				throw new ErpFraudException(fraud);
			}			
		}

	
	private void preCheckDonationFraud(
			PrimaryKey erpCustomerPk,
			ErpAbstractOrderModel order,
			CrmAgentRole agentRole) throws CreateException, RemoteException, ErpFraudException {
			
			ErpFraudPreventionSB fraudSB = getErpFraudHome().create();
			EnumFraudReason fraud = fraudSB.preCheckDonationFraud(erpCustomerPk, order, agentRole);
			if (fraud != null) {
				SessionContext ctx = this.getSessionContext();
				ctx.setRollbackOnly();
				throw new ErpFraudException(fraud);
			}			
		}
	
		private void preCheckOrderFraud(
		PrimaryKey erpCustomerPk,
		ErpAbstractOrderModel order,
		CrmAgentRole agentRole) throws CreateException, RemoteException, ErpFraudException {
		
		ErpFraudPreventionSB fraudSB = getErpFraudHome().create();
		EnumFraudReason fraud = fraudSB.preCheckOrderFraud(erpCustomerPk, order, agentRole);
		if (fraud != null) {
			SessionContext ctx = this.getSessionContext();
			ctx.setRollbackOnly();
			throw new ErpFraudException(fraud);
		}
		
	}

	/**
	 * Perform fraud checks that create cases, since they may have been lost.
	 *
	 * @param saleId
	 */
	private void postCheckOrderFraud(
		PrimaryKey salePk,
		PrimaryKey erpCustomerPk,
		ErpAbstractOrderModel order,
		CrmAgentRole agentRole) throws CreateException, RemoteException {
		
		ErpFraudPreventionSB fraudSB = getErpFraudHome().create();
		fraudSB.postCheckOrderFraud(salePk, erpCustomerPk, order, agentRole);
	}

	private void postCheckGiftCardFraud(
			PrimaryKey salePk,
			PrimaryKey erpCustomerPk,
			ErpAbstractOrderModel order,
			CrmAgentRole agentRole) throws CreateException, RemoteException {
			
			ErpFraudPreventionSB fraudSB = getErpFraudHome().create();
			fraudSB.postCheckGiftCardFraud(salePk, erpCustomerPk, order, agentRole);
	}
	
	private void postCheckDonationFraud(
			PrimaryKey salePk,
			PrimaryKey erpCustomerPk,
			ErpAbstractOrderModel order,
			CrmAgentRole agentRole) throws CreateException, RemoteException {
			
			ErpFraudPreventionSB fraudSB = getErpFraudHome().create();
			fraudSB.postCheckDonationFraud(salePk, erpCustomerPk, order, agentRole);
	}
	
	public EnumPaymentResponse resubmitPayment(String saleId, ErpPaymentMethodI paymentMethod, Collection charges)
		throws ErpTransactionException {
		try {

			LOGGER.info("Resubmit payment - start. saleId=" + saleId);

			ErpSaleEB sale = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpInvoiceModel invoice = sale.getInvoice();
			ErpResubmitPaymentModel resubmitModel = new ErpResubmitPaymentModel();
			resubmitModel.setPaymentMethod(paymentMethod);
			resubmitModel.setInvoiceLines(invoice.getInvoiceLines());
			List chargesList = invoice.getCharges();
			chargesList.addAll(charges);
			resubmitModel.setCharges(chargesList);

		} catch (FinderException fe) {
			throw new EJBException(fe);
		} catch (RemoteException re) {
			throw new EJBException(re);
		}

		LOGGER.info("Resubmit payment - done. saleId=" + saleId);

		return null;
	}

	private void reverseAppliedCredits(PrimaryKey customerPk, HashMap creditsMap) {
		try {
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(customerPk);
			ErpCustomerModel customerModel = (ErpCustomerModel) customerEB.getModel();
			Collection credits = customerModel.getCustomerCredits();
			for (Iterator it = credits.iterator(); it.hasNext();) {
				ErpCustomerCreditModel credit = (ErpCustomerCreditModel) it.next();
				String thisCreditId = credit.getPK().getId();
				if (creditsMap.containsKey(thisCreditId)) {
					Double delta = (Double) creditsMap.get(thisCreditId);
					LOGGER.debug(
						"Reversing credit (#"
							+ thisCreditId
							+ "): was $"
							+ credit.getAmount()
							+ ", now is $"
							+ (credit.getAmount() + delta.doubleValue()));
					//credit.setAmount( credit.getAmount() + delta.doubleValue() );
					customerEB.updateCustomerCredit(thisCreditId, delta.doubleValue());
				}
			}
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}

	} // method reverseAppliedCredits

	private void reconcileCustomerCredits(PrimaryKey erpCustomerPk, ErpAbstractOrderModel order) {

		try {
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(erpCustomerPk);
			ErpCustomerModel customerModel = (ErpCustomerModel) customerEB.getModel();
			List customerCredits = customerModel.getCustomerCredits();

			for (Iterator acIterator = order.getAppliedCredits().iterator(); acIterator.hasNext();) {
				//creditsApplied = true;
				ErpAppliedCreditModel appliedCredit = (ErpAppliedCreditModel) acIterator.next();
				for (Iterator ccIterator = customerCredits.iterator(); ccIterator.hasNext();) {
					ErpCustomerCreditModel customerCredit = (ErpCustomerCreditModel) ccIterator.next();
					if (appliedCredit.getCustomerCreditPk().getId().equals(customerCredit.getPK().getId())) {
						//customerCredit.setRemainingAmount( customerCredit.getRemainingAmount() - appliedCredit.getAmount() );
						LOGGER.debug(
							"setting remaining credit on CUSTCREDIT "
								+ customerCredit.getPK().getId()
								+ " to "
								+ (customerCredit.getRemainingAmount() - appliedCredit.getAmount()));
						this.updateCustomerCredit(erpCustomerPk, customerCredit.getPK().getId(), -appliedCredit.getAmount());
						break;
					}
				}
			}

		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}

	} // method reconcileCustomerCredits

	/**
	 * @see ErpCustomerManagerSB#checkAvailability(PrimaryKey, ErpCreateOrderModel, long)
	 */
	public Map checkAvailability(PrimaryKey erpCustomerPk, ErpCreateOrderModel order, long timeout) {
		SapOrderAdapter sapOrder = this.adaptOrder(erpCustomerPk, order, null);

		try {
			SapGatewaySB sapSB = this.getSapGatewayHome().create();
			sapOrder.setWebOrderNumber("1");
			sapOrder = (SapOrderAdapter) sapSB.checkAvailability(sapOrder, timeout);
		} catch (CreateException ex) {
			LOGGER.warn("Error creating session bean", ex);
			throw new EJBException("Error creating session bean " + ex.getMessage());
		} catch (RemoteException ex) {
			LOGGER.warn("Error talking to session bean", ex);
			throw new EJBException("Error talking to session bean " + ex.getMessage());
		}

		Map inventoryMap = new HashMap();
		for (int i = 0; i < sapOrder.numberOfOrderLines(); i++) {
			SapOrderLineI orderLine = sapOrder.getOrderLine(i);
			inventoryMap.put(orderLine.getLineNumber(), orderLine.getInventories());
		}

		return inventoryMap;
	}

	/**
	 * Get a specific sale.
	 */
	public ErpSaleModel getOrder(PrimaryKey erpSalePk) {
		try {
			ErpSaleEB saleEB = getErpSaleHome().findByPrimaryKey(erpSalePk);

			return (ErpSaleModel) saleEB.getModel();

		} catch (FinderException ex) {
			LOGGER.warn("FinderException: ", ex);
			throw new EJBException(ex);
		} catch (RemoteException ex) {
			LOGGER.warn("RemoteException: ", ex);
			throw new EJBException(ex);
		}
	}

	/**
	 * Get lightweight info about a customer's orders.
	 *
	 * @param erpCustomerPk primary key of ErpCustomer
	 *
	 * @return collection of ErpSaleInfo objects
	 */
	public ErpOrderHistory getOrderHistoryInfo(PrimaryKey erpCustomerPk) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			Collection history = ErpSaleInfoDAO.getOrderHistoryInfo(conn, erpCustomerPk.getId());

/*			This block has been commented out as loading of Promotion History is seperated
 * 			from Order History. Jira Task PERF - 22.
 * 			Map promoParticipation = ErpPromotionDAO.loadPromotionParticipation(conn, erpCustomerPk);
			for (Iterator i = history.iterator(); i.hasNext();) {
				ErpSaleInfo saleInfo = (ErpSaleInfo) i.next();
				Set promoCodes = (Set) promoParticipation.get(saleInfo.getSaleId());
				if (promoCodes != null) {
					saleInfo.setUsedPromotionCodes(promoCodes);
				}
			}
*/
			return new ErpOrderHistory(history);

		} catch (SQLException ex) {
			throw new EJBException(ex);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					LOGGER.warn("Unable to close connection", ex);
				}
			}
		}
	}
	
	 public ErpPromotionHistory getPromoHistoryInfo(PrimaryKey erpCustomerPk) {
			Connection conn = null;
			try {
				conn = this.getConnection();
				Map promoParticipation = ErpPromotionDAO.loadPromotionParticipation(conn, erpCustomerPk);
				return new ErpPromotionHistory(promoParticipation);

			} catch (SQLException ex) {
				throw new EJBException(ex);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException ex) {
						LOGGER.warn("Unable to close connection", ex);
					}
				}
			}
	 }

	/**
	 * Add an invoice to ErpSale
	 *
	 * @parameter ErpInvoiceModel invoice to be added
	 * @return void
	 * @throws EJBException if there are any problems in finding, talking or adding to the ErpSale
	 */
	public void addInvoice(ErpInvoiceModel invoice, String saleId, ErpShippingInfo shippingInfo) throws ErpTransactionException {
		try {

			LOGGER.info("Add invoice - start. saleId=" + saleId);

			ErpSaleEB eb = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpAbstractOrderModel order = eb.getCurrentOrder();
			if(EnumPaymentType.MAKE_GOOD.equals(order.getPaymentMethod().getPaymentType())){
				invoice.setAppliedCredits(Collections.EMPTY_LIST);
				//Applied gift cards should also be set to empty list.
				invoice.setAppliedGiftCards(Collections.EMPTY_LIST);
			}else{
				List appliedCredits = order.getAppliedCredits();
				List invoicedCredits = invoice.getAppliedCredits();
				this.reconcileInvoicedCredits(eb.getCustomerPk(), invoicedCredits, appliedCredits);
				List appliedGiftCards = order.getAppliedGiftcards();
				if(appliedGiftCards != null && appliedGiftCards.size() > 0) {
					//Set the selected gift cards from the original order.
					//ErpCustomerEB erpCustomerEB = this.getErpCustomerHome().findByPrimaryKey(eb.getCustomerPk());
					List selectedGiftCards = ErpGiftCardUtil.getGiftcardPaymentMethods(appliedGiftCards);
					order.setSelectedGiftCards(selectedGiftCards);
					//Re Generate Applied gift cards info.
					GiftCardApplicationStrategy strategy = new GiftCardApplicationStrategy(order, invoice);
					strategy.generateAppliedGiftCardsInfo();
					invoice.setAppliedGiftCards(strategy.getAppGiftCardInfo());
				}
			}
			eb.addInvoice(invoice);
			eb.updateShippingInfo(shippingInfo);

			LOGGER.info("Add invoice - done. saleId=" + saleId);

		} catch (FinderException fe) {
			LOGGER.warn(fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn(re);
			throw new EJBException(re);
		}
	}

	private void reconcileInvoicedCredits(PrimaryKey customerPK, List invoicedCredits, List appliedCredits)
		throws RemoteException, FinderException {
		ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(customerPK);
		ErpCustomerModel customerModel = (ErpCustomerModel) customerEB.getModel();
		List customerCredits = customerModel.getCustomerCredits();

		for (Iterator i = invoicedCredits.iterator(); i.hasNext();) {
			ErpInvoicedCreditModel invoicedCredit = (ErpInvoicedCreditModel) i.next();
			for (Iterator j = appliedCredits.iterator(); j.hasNext();) {
				ErpAppliedCreditModel appliedCredit = (ErpAppliedCreditModel) j.next();
				if (appliedCredit.getPK().getId().equals(invoicedCredit.getOriginalCreditId())) {
					invoicedCredit.setCustomerCreditPk(appliedCredit.getCustomerCreditPk());
					invoicedCredit.setDepartment(appliedCredit.getDepartment());
					invoicedCredit.setAffiliate(appliedCredit.getAffiliate());
					int invoicedAmount = (int) Math.round(invoicedCredit.getAmount() * 100);
					int originalAmount = (int) Math.round(appliedCredit.getAmount() * 100);

					if (invoicedAmount < originalAmount) {
						for (Iterator ccIterator = customerCredits.iterator(); ccIterator.hasNext();) {
							ErpCustomerCreditModel customerCredit = (ErpCustomerCreditModel) ccIterator.next();
							if (invoicedCredit.getCustomerCreditPk().getId().equals(customerCredit.getPK().getId())) {
								LOGGER.debug(
									"setting remaining credit on CUSTCREDIT "
										+ customerCredit.getPK().getId()
										+ " to "
										+ (customerCredit.getRemainingAmount() + appliedCredit.getAmount()));
								this.updateCustomerCredit(
									customerPK,
									customerCredit.getPK().getId(),
									(appliedCredit.getAmount() - invoicedCredit.getAmount()));
								//customerEB.updateCustomerCredit(customerCredit.getPK().getId(), (appliedCredit.getAmount() - invoicedCredit.getAmount()));
								break;
							}
						}
					}

					break;
				}
			}
		}
	}

	public void reconcileSale(String saleId) throws ErpTransactionException {
		try {
			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			List cases = eb.reconcileSale();
			new ErpCreateCaseCommand(LOCATOR, cases).execute();

		} catch (FinderException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		}
	}

	public void cutoff(String saleId) {
		try {

			LOGGER.info("Cutoff - start. saleId=" + saleId);

			ErpSaleEB eb = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			eb.cutoff();

			LOGGER.info("Cutoff - done. saleId=" + saleId);

		} catch (FinderException fe) {
			LOGGER.warn(fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn(re);
			throw new EJBException(re);
		} catch (ErpTransactionException te) {
			LOGGER.warn(te);
			throw new EJBException(te);
		}
	}

	public String addSettlement(ErpSettlementModel model, String saleId, String authId) {
		try {

			LOGGER.info("Add settlement - start. saleId=" + saleId + " authId=" + authId);

			ErpSaleEB eb = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpInvoiceModel invoice = eb.getInvoice();
			String invoiceNumber = invoice.getInvoiceNumber();
			/* 
			//just a sanity check that this settlement is for an authorization we actually did
			//for this sale
			List authorizations = eb.getApprovedAuthorizations();
			boolean valid = true;
			 
			 for(Iterator i = authorizations.iterator(); i.hasNext(); ){
				ErpAuthorizationModel auth = (ErpAuthorizationModel) i.next();
				if(auth.getApprovalCode().equals(authId)){
					valid = true;
				}
			}
			if(!valid){
				throw new EJBException("This settlement is not for this sale");
			}
			*/

			eb.addSettlement(model);

			LOGGER.info("Add settlement - done. saleId=" + saleId + " authId=" + authId);

			return invoiceNumber;
		} catch (FinderException fe) {
			LOGGER.debug(fe);
			throw new EJBException("Cannot find Sale for id: " + saleId);
		} catch (RemoteException re) {
			LOGGER.debug(re);
			throw new EJBException("Error talking to sale entity bean");
		} catch (ErpTransactionException te) {
			LOGGER.debug(te);
			throw new EJBException("Error while performing this transaction ", te);
		}

	}

	public void addAdjustment(ErpAdjustmentModel adjustmentModel) throws ErpTransactionException {
		try {
			String saleId = adjustmentModel.getReferenceNumber();

			LOGGER.info("Add adjustment - start. saleId=" + saleId);

			ErpSaleEB eb = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			eb.addAdjustment(adjustmentModel);
			String customerId = eb.getCustomerPk().getId();

			CrmSystemCaseInfo info =
				new CrmSystemCaseInfo(
					new PrimaryKey(customerId),
					new PrimaryKey(saleId),
					CrmCaseSubject.getEnum(CrmCaseSubject.CODE_PAYMENT_ERROR),
					"Following sale failed to Settle " + saleId);

			new ErpCreateCaseCommand(LOCATOR, info).execute();

			LOGGER.info("Add adjustment - done. saleId=" + saleId);

		} catch (FinderException e) {
			LOGGER.debug(e);
			throw new EJBException("No sale found for the given saleId", e);
		} catch (RemoteException e) {
			LOGGER.debug(e);
			throw new EJBException("Cannot talk to the beans", e);
		}
	}

	public void markAsReturn(String saleId, boolean fullReturn, boolean alcoholOnly)
		throws ErpTransactionException, ErpSaleNotFoundException {
		try {
			ErpSaleEB eb = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			eb.markAsReturn();

			String details = null;
			if (fullReturn) {
				details = "Customer Refused whole Order";
			}
			if (alcoholOnly) {
				details = "Nobody over 21 yrs was home to take delivery of alcoholic items";
			}
			this.createCaseForSale(saleId, "refused_order", details);
		} catch (FinderException fe) {
			LOGGER.warn("Cannot find sale for sale", fe);
			throw new EJBException("No sale found for the given saleId: " + saleId, fe);
		} catch (RemoteException re) {
			LOGGER.warn("RemoteException: ", re);
			throw new EJBException("Exception while talking to Sale", re);
		}
	}

	public void markAsRedelivery(String saleId) throws ErpTransactionException, ErpSaleNotFoundException {
		try {
			ErpSaleEB eb = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			eb.markAsRedelivery();
			this.createCaseForSale(saleId, "redelivery", "Nobody was home to take delivery");
		} catch (FinderException fe) {
			LOGGER.warn("Cannot find sale for saleId", fe);
			throw new EJBException("No sale found for given saleId: " + saleId, fe);
		} catch (RemoteException re) {
			LOGGER.warn("RemoteException: ", re);
			throw new EJBException("Exception while talking to Sale", re);
		}
	}

	public List getRedeliveries(java.util.Date date) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return ErpSaleInfoDAO.getRedeliveries(conn, date);
		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
	}

	public ErpDeliveryInfoModel getDeliveryInfo(String saleId) throws ErpSaleNotFoundException {
		try {
			ErpSaleEB eb = (ErpSaleEB) getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpAbstractOrderModel order = eb.getCurrentOrder();

			return order.getDeliveryInfo();

		} catch (FinderException fe) {
			LOGGER.warn(fe);
			throw new ErpSaleNotFoundException(fe);
		} catch (RemoteException re) {
			LOGGER.warn(re);
			throw new EJBException(re);
		}

	}

	public void createCaseForSale(String saleId, String reason) throws ErpSaleNotFoundException {
		String details = null;

		if ("redelivery".equalsIgnoreCase(reason)) {
			details = "Nobody was home to take delivery";
		} else if ("refused_order".equalsIgnoreCase(reason)) {
			details = "Customer Refused Order";
		} else {
			throw new EJBException("Unknown case type " + reason);
		}

		this.createCaseForSale(saleId, reason, details);

	}

	private void createCaseForSale(String saleId, String reason, String details) throws ErpSaleNotFoundException {
		try {
			ErpSaleEB eb = (ErpSaleEB) getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			PrimaryKey custPK = eb.getCustomerPk();

			CrmCaseSubject subject;
			if ("refused_order".equalsIgnoreCase(reason)) {
				subject = CrmCaseSubject.getEnum(CrmCaseSubject.CODE_RETURN_REFUSAL);
			} else {
				throw new EJBException("Unknown case type");
			}
			CrmSystemCaseInfo info = new CrmSystemCaseInfo(custPK, new PrimaryKey(saleId), subject, details + " " + saleId);
			new ErpCreateCaseCommand(LOCATOR, info).execute();

		} catch (FinderException fe) {
			LOGGER.warn(fe);
			throw new ErpSaleNotFoundException(fe);
		} catch (RemoteException re) {
			LOGGER.warn(re);
			throw new EJBException(re);
		}
	}

	public List getTruckNumbersForDate(java.util.Date deliveryDate) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return ErpSaleInfoDAO.getTruckNumbersForDate(conn, deliveryDate);

		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning: ", se);
			}
		}
	}

	public DlvSaleInfo getDlvSaleInfo(String orderNumber) throws ErpSaleNotFoundException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			DlvSaleInfo dlvSaleInfo = ErpSaleInfoDAO.getDlvSaleInfo(conn, orderNumber);
			if (dlvSaleInfo == null) {
				throw new ErpSaleNotFoundException("Cannot Find sale for #: " + orderNumber);
			}
			return dlvSaleInfo;
		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning: ", se);
			}
		}
	}

	public List getOrdersForDateAndAddress(java.util.Date date, String address, String zipcode) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return ErpSaleInfoDAO.getOrdersForDateAndAddress(conn, date, address, zipcode);
		} catch (SQLException e) {
			LOGGER.debug("SQLException: ", e);
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while cleaning: ", e);
			}
		}
	}

	public List getOrdersByTruckNumber(String truckNumber, java.util.Date deliveryDate) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return ErpSaleInfoDAO.getOrdersByTruckNumber(conn, truckNumber, deliveryDate);

		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up: ", se);
			}
		}
	}

	public String approveComplaint(String complaintId, boolean isApproved, String csrId) throws ErpComplaintException {
		try {
			ErpSaleEB eb = getErpSaleHome().findByComplaintId(complaintId);
			ErpSaleModel saleModel = (ErpSaleModel) eb.getModel();
			ErpComplaintModel pendingComplaint = saleModel.getComplaint(complaintId);
			if (pendingComplaint == null) {
				this.getSessionContext().setRollbackOnly();
				throw new ErpComplaintException("This complaint doesn't belong to this sale.");
			}
			//
			// Approve or reject the credit
			//
			if (isApproved) {
				
				validateComplaintPayment(eb.getCurrentOrder().getPaymentMethod().getPaymentType(), pendingComplaint);
				
				pendingComplaint.setStatus(EnumComplaintStatus.APPROVED);
				pendingComplaint.setApprovedBy(csrId);
				pendingComplaint.setApprovedDate(new Date());
				//
				// complaints containing refunds can only be approved after the order has settled
				//
				if ((pendingComplaint.getComplaintMethod() != ErpComplaintModel.STORE_CREDIT)
					&& !(EnumSaleStatus.SETTLED.equals(saleModel.getStatus())
						|| EnumSaleStatus.SETTLED_RETURNED.equals(saleModel.getStatus()))) {
					this.getSessionContext().setRollbackOnly();
					throw new ErpComplaintException("Complaints containing refunds can only be approved once the sale is in the SETTLED state.");
				}
				//
				// complaints containing only store credits can be approved once the order is delivered
				//
				if ((pendingComplaint.getComplaintMethod() == ErpComplaintModel.STORE_CREDIT)
					&& !(EnumSaleStatus.SETTLED.equals(saleModel.getStatus())
						|| EnumSaleStatus.PAYMENT_PENDING.equals(saleModel.getStatus())
						|| EnumSaleStatus.CAPTURE_PENDING.equals(saleModel.getStatus()))) {
					this.getSessionContext().setRollbackOnly();
					throw new ErpComplaintException("Store credit can only be approved once the has been delivered to the customer.");
				}
				//Cannot issue cashback that is more than the amount we actually charged for.
				double invoiceAmount = ((int) Math.round(saleModel.getLastInvoice().getAmount() * 100)) / 100.0;
				List invoicedGiftCards = saleModel.getLastInvoice().getAppliedGiftCards();
				if(invoicedGiftCards != null && invoicedGiftCards.size() > 0){
					//Gift cards applied on the order. substract applied gift card amount from invoice amount.
					double appliedInvGCamount = ErpGiftCardUtil.getTotalAppliedAmount(invoicedGiftCards);
					invoiceAmount -= appliedInvGCamount;
				}
				
				double cashBackAmount = ((int) Math.round(pendingComplaint.getCashBackAmount() * 100)) / 100.0;
				
				if (cashBackAmount > invoiceAmount) {
					this.getSessionContext().setRollbackOnly();
					throw new ErpComplaintException("Cashback amount cannot be more than the invoice amount.");
				}
			
				if (cashBackAmount == 0.0 && pendingComplaint.getComplaintLines().isEmpty()) {
					this.getSessionContext().setRollbackOnly();
					throw new ErpComplaintException("Can't issue cashback for zero amount.");
				}
				// due to the way this whole complaint thing is split up we need
				// this updateComplaint call here and in the else block it will be called
				// once anyway. (for the genius who thinks that it can be made once at the
				// end of conditional block)
				
				eb.updateComplaint(pendingComplaint);
				processComplaint(pendingComplaint, eb.getPK().getId());
			} else {
				pendingComplaint.setStatus(EnumComplaintStatus.REJECTED);
				pendingComplaint.setApprovedBy(csrId);
				pendingComplaint.setApprovedDate(new java.util.Date());
				//
				eb.updateComplaint(pendingComplaint);
			}
			//
			// Persist the change in complaint status
			//
			return eb.getPK().getId();

		} catch (FinderException ex) {
			LOGGER.debug("Could not find requested sale for complaintId: " + complaintId, ex);
			throw new EJBException(ex);
		} catch (RemoteException ex) {
			LOGGER.debug("RemoteException while working with requested sale for complaintId: " + complaintId, ex);
			throw new EJBException(ex);
		} catch (ErpTransactionException ex) {
			LOGGER.debug("ErpTransactionException while working with requested sale for complaintId: " + complaintId, ex);
			throw new EJBException(ex);
		}
	}

	private void validateComplaintPayment(EnumPaymentType paymentType, ErpComplaintModel pendingComplaint) throws ErpComplaintException {
		if (EnumPaymentType.MAKE_GOOD.equals(paymentType) && pendingComplaint.getMakegood_sale_id() == null) {
			this.getSessionContext().setRollbackOnly();
			throw new ErpComplaintException("Cannot issue credit for make-good orders.");
		}
		if (EnumPaymentType.ON_FD_ACCOUNT.equals(paymentType)) {
			
			int complaintType = pendingComplaint.getComplaintMethod();
			if (ErpComplaintModel.CASH_BACK == complaintType || ErpComplaintModel.MIXED == complaintType) {
				this.getSessionContext().setRollbackOnly();
				throw new ErpComplaintException("This order is ON FD ACCOUNT and no cashback can be issued.");
			}
		}
	}

	/**
	 * Adds a complaint to the user's list of complaints and begins the associated credit issuing process
	 *
	 * @param ErpComplaintModel represents the complaint
	 * @param String the PK of the sale to which the complaint is to be added
	 * @throws ErpComplaintException if order was not in proper state to accept complaints
	 */
	public PrimaryKey addComplaint(ErpComplaintModel complaint, String saleId) throws ErpComplaintException {
		Connection conn = null;
		try {
			ErpSaleEB eb = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			boolean autoApprove = complaint.canBeAutoApproved(eb.getStatus());


			//
			// Auto-approve the complaint if it doesn't require approval
			//
			if (autoApprove) {
				complaint.setStatus(EnumComplaintStatus.APPROVED);
				complaint.setApprovedDate(new java.util.Date());
				complaint.setApprovedBy("AUTO_APPROVED");
			}
		
			validateComplaintPayment(eb.getCurrentOrder().getPaymentMethod().getPaymentType(), complaint);

			//
			// add complaint to the order
			//
			try {
				conn = this.getConnection();
				if (complaint.getCustomerEmail()!=null) {
					CustomerEmailDAO ceDAO = new CustomerEmailDAO();
					PrimaryKey pk = ceDAO.create(conn,complaint.getCustomerEmail());
					complaint.setCustomerEmail((ErpCustomerEmailModel) ceDAO.load(conn,pk));
				}
				eb.addComplaint(complaint);
			} catch (ErpTransactionException e) {
				throw new ErpComplaintException(e,e.getMessage());
			}
			
			//
			// and get back the new sale model with the complaint
			//
			ErpSaleModel saleModel = (ErpSaleModel) eb.getModel();
			Collection complaints = saleModel.getComplaints();
			ErpComplaintModel lastComplaint = this.getLastComplaint(complaints);
			PrimaryKey complaintPk = lastComplaint.getPK();
			
			//
			// auto-approve the complaint if appropriate if its non-zero
			//
			if (autoApprove && 0 != Math.round(complaint.getAmount() * 100.0)) {
				this.approveComplaint(lastComplaint.getPK().getId(), true, "AUTO_APPROVED");
			}
			return complaintPk;
			
		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} catch (RemoteException re) {
			throw new EJBException(re);
		} catch (FinderException fe) {
			throw new EJBException(fe);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					LOGGER.warn("Unable to close connection", ex);
				}
			}
		}
			
	} // method addComplaint

	public void processAllComplaints(String saleId) throws ErpComplaintException {
		try {
			ErpSaleEB eb = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			if (!EnumSaleStatus.SETTLED.equals(eb.getStatus())) {
				LOGGER.warn("Cannot process complaints for sale that has not been settled.");
				throw new ErpComplaintException("Cannot process complaints for sale that has not been settled.");
			}
			ErpSaleModel saleModel = (ErpSaleModel) eb.getModel();

			Collection complaints = saleModel.getComplaints();
			//
			// Process APPROVED complaints
			//
			for (Iterator it = complaints.iterator(); it.hasNext();) {
				ErpComplaintModel c = (ErpComplaintModel) it.next();
				if (EnumComplaintStatus.APPROVED.equals(c.getStatus())) {
					this.processComplaint(c, saleId);
				}
			}

		} catch (FinderException ex) {
			LOGGER.debug("Could not find requested sale: " + saleId, ex);
			throw new EJBException(ex);
		} catch (RemoteException ex) {
			LOGGER.debug("RemoteException while working with requested sale: " + saleId, ex);
			throw new EJBException(ex);
		} catch (ErpTransactionException ex) {
			LOGGER.debug("ErpTransactionException while working with requested sale: " + saleId, ex);
			throw new EJBException(ex);
		}
	} // method processAllComplaints

	private void processComplaint(ErpComplaintModel complaint, String saleId) throws ErpTransactionException {
		//
		// Add Cash Back / Issue Credit
		//
		if (complaint.getComplaintMethod() == ErpComplaintModel.CASH_BACK
			|| complaint.getComplaintMethod() == ErpComplaintModel.MIXED) {
				
			this.addCashback(complaint.getCashBackComplaintLines(), saleId);
		}
		if (complaint.getComplaintMethod() == ErpComplaintModel.STORE_CREDIT
			|| complaint.getComplaintMethod() == ErpComplaintModel.MIXED) {
			this.issueCredit(complaint, saleId);
		}
	}

	public ErpComplaintInfoModel getComplaintInfo(String saleId, String complaintId) {
		try {
			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpComplaintModel complaint = null;
			ErpComplaintModel tempComplaint = null;
			for (Iterator i = eb.getComplaints().iterator(); i.hasNext();) {
				tempComplaint = (ErpComplaintModel) i.next();
				if (tempComplaint.getPK().getId().equals(complaintId)) {
					complaint = tempComplaint;
					break;
				}
			}
			if (complaint == null) {
				throw new EJBException("Cannot find complaint for complaintId: " + complaintId);
			}
			ErpComplaintInfoModel infoModel = new ErpComplaintInfoModel(eb.getCustomerPk().getId(), complaint);
			return infoModel;
		} catch (FinderException fe) {
			LOGGER.warn("Cannot find sale for id: " + saleId, fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn("Cannot talk to SaleEntityBean", re);
			throw new EJBException(re);
		}

	}

	private void addCashback(List lines, String saleId) throws ErpTransactionException {
		try {
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpAbstractOrderModel order = saleEB.getCurrentOrder();
			ErpPaymentMethodI paymentMethod = order.getPaymentMethod();
			double fdAmount = 0.0;
			double bcAmount = 0.0;
			double usqAmount = 0.0;
			final ErpAffiliate FD = ErpAffiliate.getPrimaryAffiliate();
			final ErpAffiliate BC = ErpAffiliate.getEnum(ErpAffiliate.CODE_BC);
			final ErpAffiliate USQ = ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ);
			for(Iterator i = lines.iterator(); i.hasNext(); ) {
				ErpComplaintLineModel line = (ErpComplaintLineModel) i.next();
				if(EnumComplaintLineType.ORDER_LINE.equals(line.getType())) {
					ErpOrderLineModel orderline = order.getOrderLineByPK(line.getOrderLineId());
					if(USQ.equals(orderline.getAffiliate())) {
						usqAmount += line.getAmount();
					}else if(BC.equals(orderline.getAffiliate())) {
						bcAmount += line.getAmount();
					} else {
						fdAmount += line.getAmount();
					}
				} else {
					fdAmount += line.getAmount();
				}
			}
			
			PaymentManager paymentManager = new PaymentManager();
			if(fdAmount > 0) {
				ErpCashbackModel cashback = paymentManager.returnCashback(saleId, paymentMethod, fdAmount, 0.0, FD);
				saleEB.addCashback(cashback);
			}
			
			if(bcAmount > 0) {
				ErpCashbackModel cashback = paymentManager.returnCashback(saleId, paymentMethod, bcAmount, 0.0, BC);
				saleEB.addCashback(cashback);
			}
			if(usqAmount > 0) {
				ErpCashbackModel cashback = paymentManager.returnCashback(saleId, paymentMethod, usqAmount, 0.0, USQ);
				saleEB.addCashback(cashback);
			}
			
		} catch (FinderException e) {
			throw new EJBException("No Sale found for saleId: " + saleId);
		} catch (RemoteException e) {
			throw new EJBException("Error talking to beans", e);
		}
	} // method addCashback

	private ErpComplaintModel getLastComplaint(Collection col) {

		ErpComplaintModel lastComplaint = null;

		for (Iterator it = col.iterator(); it.hasNext();) {
			java.util.Date lastDate = null;
			ErpComplaintModel cm = (ErpComplaintModel) it.next();
			if (lastDate == null || cm.getCreateDate().after(lastDate)) {
				lastDate = cm.getCreateDate();
				lastComplaint = cm;
			}
		}
		return lastComplaint;
	}

	private void issueCredit(ErpComplaintModel complaint, String saleId) {
		try {
			//
			// Get sale model and associated customer bean
			//
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpSaleModel saleModel = (ErpSaleModel) saleEB.getModel();

			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(saleModel.getCustomerPk());
			//
			// Could be multiple customer credits per complaint,
			// so we need to iterate through them and add them to the customer bean
			//
			Collection deptCredits = this.getDeptCredits(complaint, saleModel.getRecentOrderTransaction());
			for (Iterator it = deptCredits.iterator(); it.hasNext();) {
				DeptCredit dc = (DeptCredit) it.next();
				ErpCustomerCreditModel custCreditModel =
					new ErpCustomerCreditModel(
						complaint.getPK(),
						dc.getDepartment(),
						dc.getAmount(),
						dc.getAffiliate());
				//Bug fix MNT-172. BEGIN
				custCreditModel.setCreateDate(new Date());
				//END.				
				customerEB.addCustomerCredit(custCreditModel);
			}
		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}
	}

	private Collection getDeptCredits(ErpComplaintModel complaint, ErpAbstractOrderModel order) {

		HashMap deptCredits = new HashMap();

		for (Iterator i = complaint.getComplaintLines().iterator(); i.hasNext();) {
			ErpComplaintLineModel complaintLine = (ErpComplaintLineModel) i.next();
			if (EnumComplaintLineMethod.STORE_CREDIT.equals(complaintLine.getMethod()) && (complaintLine.getAmount() > 0.0)) {
				ErpAffiliate affiliate = null;
				if (EnumComplaintLineType.ORDER_LINE.equals(complaintLine.getType())) {
					ErpOrderLineModel ol = order.getOrderLineByPK(complaintLine.getOrderLineId());
					affiliate = ol.getAffiliate();
				} else {
					//!!! Any credit other than EnumComplaintLineType.ORDER_LINE is considered FD credit
					affiliate = ErpAffiliate.getEnum(ErpAffiliate.CODE_FD);
				}
				DeptAffiliateKey key = new DeptAffiliateKey(affiliate, complaintLine.getDepartmentCode());
				DeptCredit dc = (DeptCredit) deptCredits.get(key);
				if (dc == null) {
					dc = new DeptCredit(key, complaintLine.getAmount());
					deptCredits.put(key, dc);
				} else {
					dc.addAmount(complaintLine.getAmount());
				}
			}
		}

		return deptCredits.values();
	}

	private static class DeptAffiliateKey {
		private final ErpAffiliate affiliate;
		private final String department;

		public DeptAffiliateKey(ErpAffiliate affiliate, String department) {
			this.affiliate = affiliate;
			this.department = department;
		}

		public ErpAffiliate getAffiliate() {
			return this.affiliate;
		}

		public String getDepartment() {
			return this.department;
		}

		public int hashCode() {
			return this.department.hashCode() ^ this.affiliate.hashCode();
		}

		public boolean equals(Object o) {
			if (o instanceof DeptAffiliateKey) {
				DeptAffiliateKey da = (DeptAffiliateKey) o;
				return this.department.equals(da.department) && this.affiliate.equals(da.affiliate);
			}
			return false;
		}
	}

	private static class DeptCredit {
		private final DeptAffiliateKey key;
		private double amount = 0;

		public DeptCredit(DeptAffiliateKey key, double amount) {
			this.key = key;
			this.amount = amount;
		}

		public double getAmount() {
			return this.amount;
		}

		public void addAmount(double amount) {
			this.amount += amount;
		}

		public ErpAffiliate getAffiliate() {
			return this.key.getAffiliate();
		}

		public String getDepartment() {
			return this.key.getDepartment();
		}

		public String toString() {
			return "[DeptCredit Affiliate: "
				+ this.getAffiliate()
				+ " Department: "
				+ this.getDepartment()
				+ " Amount: "
				+ this.amount
				+ "]";
		}
	}

	public Collection getFailedAuthorizationSales() {
		try {
			//
			// Get sale model and associated customer bean
			//
			Collection col = this.getErpSaleHome().findByStatus(EnumSaleStatus.AUTHORIZATION_FAILED);
			List models = new ArrayList();
			for (Iterator i = col.iterator(); i.hasNext();) {
				ErpSaleEB saleEB = (ErpSaleEB) i.next();
				models.add(saleEB.getModel());
			}
			return models;

		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}
	} // method getFailedAuthorizationSales

	public void updateCustomerCredit(PrimaryKey pk, String customerCreditId, double delta) {
		try {
			//
			// Find relevant customer & update the remaining credit amount
			//
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(pk);
			customerEB.updateCustomerCredit(customerCreditId, delta);

		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}
	} // method updateCustomerCredit

	/**
	 * Set this customer as active/deactivated.
	 *
	 * @param PrimaryKey customer indentifier
	 * @param boolean indicates active/deactivated status
	 */
	public void setActive(PrimaryKey pk, boolean b) {
		try {
			// find relevant customer
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(pk);
			customerEB.setActive(b);

		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}
	}

	/**
	 * Set this customer as alert on/alert off.
	 *
	 * @param PrimaryKey customer indentifier
	 * @param boolean indicates active/deactivated status
	 */
	public boolean setAlert(PrimaryKey pk, ErpCustomerAlertModel customerAlert,  boolean isOnAlert) {
		try {
			// find relevant customer
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(pk);

			if (isOnAlert) {
				if (!isOnAlert(pk, customerAlert.getAlertType())) {
					customerEB.addCustomerAlert(customerAlert);
					return true;
				}
			} else {
				customerEB.removeCustomerAlert(customerAlert.getPK());
				return true;
			}
			return false;
		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}
	}
	
	public List getAlerts(PrimaryKey pk) {
		try {
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(pk);
			return customerEB.getCustomerAlerts();
		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}
	}

	public boolean isOnAlert(PrimaryKey pk) {
		return isOnAlert(pk, null);
	}
	
	public boolean isOnAlert(PrimaryKey pk, String alertType) {
		try {
			// find relevant customer
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(pk);
			List alerts = customerEB.getCustomerAlerts();
			
			if (alertType == null) {  // if there are any alerts
				return alerts.size() > 0;
			}
			
			for (Iterator iter = alerts.iterator(); iter.hasNext();) {
				ErpCustomerAlertModel alert = (ErpCustomerAlertModel) iter.next();
				if (alertType.equalsIgnoreCase(alert.getAlertType())) {
					return true;
				}
			}
			return false;
		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}
	}
	
	
	
	
	public boolean isCustomerActive(PrimaryKey pk) {
		try {
			// find relevant customer
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(pk);
			return customerEB.isActive();						
		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}
	}
	
	/**
	 * Adapt the order to an SAP order.
	 */
	private SapOrderAdapter adaptOrder(PrimaryKey erpCustomerPk, ErpAbstractOrderModel order, CustomerRatingI rating) {
		try {
			// find relevant customer
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(erpCustomerPk);
			ErpCustomerModel erpCustomer = (ErpCustomerModel) customerEB.getModel();

			SapOrderAdapter adapter = new SapOrderAdapter(order, erpCustomer, rating);
			return adapter;
			
		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (FDResourceException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}
	}

	public void reverseCustomerCredit(String saleId, String complaintId) throws ErpTransactionException {
		try {
			ErpSaleEB saleEB = null;
			try {
				saleEB = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			} catch (FinderException fe) {
				this.getSessionContext().setRollbackOnly();
				throw new ErpTransactionException(fe, "cannot find sale with pk: " + saleId);
			}
			ErpComplaintModel complaint = null;
			for (Iterator i = saleEB.getComplaints().iterator(); i.hasNext();) {
				ErpComplaintModel model = (ErpComplaintModel) i.next();
				if (model.getPK().getId().equals(complaintId)) {
					complaint = model;
					break;
				}
			}
			if (complaint == null) {
				this.getSessionContext().setRollbackOnly();
				throw new ErpTransactionException("Cannot find complaint with if " + complaintId);
			}
			int complaintMethod = complaint.getComplaintMethod();
			if (complaintMethod == ErpComplaintModel.CASH_BACK || complaintMethod == ErpComplaintModel.MIXED) {
				this.getSessionContext().setRollbackOnly();
				throw new ErpTransactionException("complaints contains a CASHBACK cannot reverse it");
			}
			PrimaryKey customerPK = saleEB.getCustomerPk();
			ErpCustomerEB customerEB = null;
			try {
				customerEB = getErpCustomerHome().findByPrimaryKey(customerPK);
			} catch (FinderException fe) {
				this.getSessionContext().setRollbackOnly();
				throw new ErpTransactionException(fe, "cannot find customer with pk: " + customerPK + " to remove credit");
			}
			customerEB.removeCustomerCreditByComplaintId(complaintId);
			complaint.setStatus(EnumComplaintStatus.REJECTED);
			saleEB.updateComplaint(complaint);

		} catch (RemoteException re) {
			throw new ErpTransactionException(re, "cannot talk to Beans");
		}
	}

	public void processSaleReturn(String saleId, ErpReturnOrderModel returnOrder) throws ErpTransactionException {
		try {
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpAbstractOrderModel order = saleEB.getCurrentOrder();
			ErpInvoiceModel invoice = saleEB.getInvoice();

			returnOrder.setTransactionSource(EnumTransactionSource.CUSTOMER_REP);
			returnOrder.setTransactionDate(new java.util.Date());
			ErpGenerateInvoiceCommand command = new ErpGenerateInvoiceCommand();
			ErpInvoiceModel newInvoice = command.generateNewInvoice(order, invoice, returnOrder);
			
			returnOrder.setSubTotal(invoice.getActualSubTotal() - newInvoice.getActualSubTotal());
			returnOrder.setTax(invoice.getTax() - newInvoice.getTax());
			returnOrder.setAmount(invoice.getAmount() - newInvoice.getAmount());
			saleEB.addReturn(returnOrder);

		} catch (RemoteException ex) {
			LOGGER.warn("RemoteException while talking to Session Bean", ex);
			throw new EJBException(ex);
		} catch (FinderException ex) {
			LOGGER.warn("FinderException while locate to Sale Entity Bean", ex);
			throw new EJBException(ex);
		}
	}

	public void approveReturn(String saleId, ErpReturnOrderModel returnOrder) throws ErpTransactionException {
		try {

			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpAbstractOrderModel order = saleEB.getCurrentOrder();
			ErpInvoiceModel invoice = saleEB.getInvoice();
			ErpGenerateInvoiceCommand invoiceCommand = new ErpGenerateInvoiceCommand();
			ErpInvoiceModel newInvoice = invoiceCommand.generateNewInvoice(order, invoice, returnOrder);			
			this.reconcileInvoicedCredits(saleEB.getCustomerPk(), newInvoice.getAppliedCredits(), invoice.getAppliedCredits());
			List appliedGiftCards = order.getAppliedGiftcards();
			if(appliedGiftCards != null && appliedGiftCards.size() > 0) {
				//Set the selected gift cards from the original order.
				//ErpCustomerEB erpCustomerEB = this.getErpCustomerHome().findByPrimaryKey(saleEB.getCustomerPk());
				List selectedGiftCards = ErpGiftCardUtil.getGiftcardPaymentMethods(appliedGiftCards);
				order.setSelectedGiftCards(selectedGiftCards);
				//Re Generate Applied gift cards info.
				GiftCardApplicationStrategy strategy = new GiftCardApplicationStrategy(order, newInvoice);
				strategy.generateAppliedGiftCardsInfo();
				newInvoice.setAppliedGiftCards(strategy.getAppGiftCardInfo());
			}
			//System.out.println("newInvoice.getSubTotal():"+newInvoice.getSubTotal());
			//System.out.println("newInvoice.getActualSubTotal():"+newInvoice.getActualSubTotal());
			saleEB.addInvoice(newInvoice);
			SapPostReturnCommand sapCommand = createSapReturnCommand(order, newInvoice);
			postReturnToSap(sapCommand);

		} catch (FinderException fe) {
			LOGGER.warn("FinderExceptin while trying to locate Sale Entity Bean", fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn("RemoteException while trying to talk to Sale Entity Bean", re);
			throw new EJBException(re);
		}
	}

	private static class ReturnAccumulator {
		double subtotal = 0;
		double tax = 0;
		double deposit = 0;

		public void process(ErpInvoiceLineModel invoiceLine) {
			subtotal += invoiceLine.getPrice();
			tax += invoiceLine.getTaxValue();
			deposit += invoiceLine.getDepositValue();
		}

		public String toString() {
			return "[ReturnAccumulator subtotal: " + subtotal + " tax: " + tax + " deposit: " + deposit + "]";
		}

	}

	private SapPostReturnCommand createSapReturnCommand(ErpAbstractOrderModel order, ErpInvoiceModel invoice) {

		Map accs = new HashMap();
		for (Iterator i = ErpAffiliate.getEnumList().iterator(); i.hasNext();) {
			accs.put(i.next(), new ReturnAccumulator());
		}

		for (Iterator i = order.getOrderLines().iterator(); i.hasNext();) {
			ErpOrderLineModel orderLine = (ErpOrderLineModel) i.next();
			ErpInvoiceLineModel invoiceLine = invoice.getInvoiceLine(orderLine.getOrderLineNumber());
			ReturnAccumulator acc = (ReturnAccumulator) accs.get(orderLine.getAffiliate());
			acc.process(invoiceLine);
		}

		SapPostReturnCommand command = new SapPostReturnCommand(invoice.getInvoiceNumber());

		boolean makeGood = EnumPaymentType.MAKE_GOOD.equals(order.getPaymentMethod().getPaymentType());

		if (makeGood) {
			// send compensating credit amount (all goes to primary affiliate, FD)
			double totalCredit = invoice.getSubTotal()
				+ invoice.getTax()
				+ invoice.getDepositValue()
				+ invoice.getDeliverySurcharge()
				+ invoice.getPhoneCharge()
				- invoice.getDiscountAmount();
			
			for (Iterator i = accs.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Entry) i.next();
				ErpAffiliate affiliate = (ErpAffiliate) e.getKey();
				ReturnAccumulator acc = (ReturnAccumulator) e.getValue();
				command.addCharges(
					affiliate,
					acc.subtotal,
					acc.tax,
					acc.deposit,
					0,
					affiliate.isPrimary() ? totalCredit : 0);
			}
			
		} else {
			for (Iterator i = accs.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Entry) i.next();
				ErpAffiliate affiliate = (ErpAffiliate) e.getKey();
				ReturnAccumulator acc = (ReturnAccumulator) e.getValue();
				command.addCharges(
					affiliate,
					acc.subtotal,
					acc.tax,
					acc.deposit,
					invoice.getRestockingFee(affiliate),
					invoice.getAppliedCreditAmount(affiliate));
			}
		}

		double deliveryAmount = 0;
		ErpChargeLineModel delivery = invoice.getCharge(EnumChargeType.DELIVERY);
		if (delivery != null) {
			deliveryAmount += delivery.getTotalAmount();
			deliveryAmount += delivery.getTotalAmount() * delivery.getTaxRate();
		}
		ErpChargeLineModel misc = invoice.getCharge(EnumChargeType.MISCELLANEOUS);
		if (misc != null) {
			deliveryAmount += misc.getTotalAmount();
			deliveryAmount += misc.getTotalAmount() * misc.getTaxRate();
		}
		command.setDeliveryCharge(deliveryAmount);
		
		command.setPhoneCharge(invoice.getPhoneCharge());
		command.setPromotionAmount(invoice.getDiscountAmount());

		return command;
	}

	private void postReturnToSap(SapPostReturnCommand command) {
		try {
			SapGatewaySB sb = this.getSapGatewayHome().create();
			sb.sendReturnInvoice(command);
		} catch (CreateException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		}
	}

	public List getEveryItemEverOrdered(PrimaryKey erpCustomerPK) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return ErpSaleInfoDAO.getEveryItemEverOrdered(conn, erpCustomerPK.getId());
		} catch (SQLException ex) {
			throw new EJBException(ex);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					LOGGER.warn("Unable to close connection", ex);
				}
			}
		}
	}

	public void scheduleRedelivery(String saleId, ErpRedeliveryModel redeliveryModel) throws ErpTransactionException {
		try {
			ErpSaleEB eb = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			eb.addRedelivery(redeliveryModel);
		} catch (FinderException fe) {
			LOGGER.warn("FinderException cannot find sale for id: " + saleId, fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn("RemoteException while trying to talk to ErpSaleEntityBean", re);
			throw new EJBException(re);
		}
	}

	private SapGatewayHome getSapGatewayHome() {
		try {
			return (SapGatewayHome) LOCATOR.getRemoteHome("java:comp/env/ejb/SapGateway", SapGatewayHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private ErpSaleHome getErpSaleHome() {
		try {
			return (ErpSaleHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpSale", ErpSaleHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private ErpCustomerHome getErpCustomerHome() {
		try {
			return (ErpCustomerHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpCustomer", ErpCustomerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private ErpFraudPreventionHome getErpFraudHome() {
		try {
			return (ErpFraudPreventionHome) LOCATOR.getRemoteHome("freshdirect.fraud.prevention", ErpFraudPreventionHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	public void resubmitCustomer(PrimaryKey customerPk) {
		try {
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(customerPk);
			ErpCustomerModel erpCustomer = (ErpCustomerModel) customerEB.getModel();

			if (erpCustomer.getSapId() != null) {
				throw new IllegalStateException("Customer " + customerPk + " SAP ID already assigned");
			}
			this.enqueueCustomer(erpCustomer, customerPk);

		} catch (FinderException fe) {
			throw new EJBException(fe);
		} catch (RemoteException re) {
			throw new EJBException(re);
		}
	}

	public void updateEmailSentFlag(ErpCustomerEmailModel customerEmail) {
		CustomerEmailDAO ced = new CustomerEmailDAO();
		Connection conn = null;
		try {
			conn = this.getConnection();
			ced.updateMailSentFlag(conn,customerEmail);
		} catch (SQLException sqle){
			throw new EJBException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					LOGGER.warn("Unable to close connection", ex);
				}
			}
		}
				
	}
	
	/**
	 * Update ShippingInfo to ErpSale
	 *
	 * @parameter ErpShippingInfo to be updated
	 * @return void
	 * @throws EJBException if there are any problems in finding, talking or adding to the ErpSale
	 */
	public void updateWaveInfo(String saleId, ErpShippingInfo sapShippingInfo) throws RemoteException, FinderException {
		try {

			LOGGER.info("Updating shipping info - start. saleId=" + saleId);

			ErpSaleEB saleEB = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpSaleModel sale = (ErpSaleModel) saleEB.getModel();
			ErpShippingInfo shippingInfo = sale.getShippingInfo();
			if (shippingInfo == null) {
				shippingInfo = new ErpShippingInfo(sapShippingInfo.getWaveNumber(), sapShippingInfo.getTruckNumber(), sapShippingInfo.getStopSequence(), 0, 0, 0);
			} else {
				shippingInfo.setWaveNumber(sapShippingInfo.getWaveNumber());
				shippingInfo.setTruckNumber(sapShippingInfo.getTruckNumber());
				shippingInfo.setStopSequence(sapShippingInfo.getStopSequence());
			}
			saleEB.updateShippingInfo(shippingInfo);

			LOGGER.info("Update wave info - done. saleId=" + saleId);

		} catch (FinderException fe) {
			LOGGER.warn(fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn(re);
			throw new EJBException(re);
		}
	}

    public void updateCartonInfo(String saleId, List cartonList) throws RemoteException, FinderException {
		try {

			LOGGER.info("Updating carton info - start. saleId=" + saleId);

			ErpSaleEB saleEB = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));

			saleEB.updateCartonInfo(cartonList);

			LOGGER.info("Update carton info - done. saleId=" + saleId);

		} catch (FinderException fe) {
			LOGGER.warn(fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn(re);
			throw new EJBException(re);
		} catch (ErpTransactionException te) {
			LOGGER.warn(te);
			throw new EJBException(te);
		}
    }
    
    public void addChargeInvoice(String saleId, double charge) {
		try {
			ErpGenerateInvoiceCommand invoiceCommand =  new ErpGenerateInvoiceCommand();
			ErpChargeInvoiceModel chargeInvoice = invoiceCommand.makeChargeInvoice(saleId, charge);
			ErpSaleEB saleEB = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			saleEB.addChargeInvoice(chargeInvoice);
		} catch (FinderException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (RemoteException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		} catch (ErpTransactionException ex) {
			LOGGER.warn(ex);
			throw new EJBException(ex);
		}
	}

	public void updateBadCustomerPaymentMethod(String saleId, EnumPaymentMethodType paymentMethodType, EnumPaymentResponse paymentResponse, String accountNumber) {

			EnumRestrictionReason restrictionReason = PaymentFraudManager.translateRestrictionReason(paymentResponse);
			String note = paymentResponse.getName()+ "-" + paymentResponse.getDescription();		
			ErpPaymentMethodI paymentMethod = getPaymentMethod(saleId, paymentMethodType, accountNumber);
			if (paymentMethod != null) { 
				PaymentFraudManager.addRestrictedPaymentMethod(paymentMethod, restrictionReason, note);
				updateECheckAlertForSale(saleId);
			}
			
	}
	
	public void removeBadCustomerPaymentMethod(String saleId, EnumPaymentMethodType paymentMethodType, String accountNumber) {
		ErpPaymentMethodI paymentMethod = getPaymentMethod(saleId, paymentMethodType, accountNumber);
		if (paymentMethod != null) { 
			PaymentFraudManager.removeRestrictedPaymentMethod(paymentMethod, true);
		}
		updateECheckAlertForSale(saleId);
	}
	
	private ErpPaymentMethodI getPaymentMethod(String saleId, EnumPaymentMethodType paymentMethodType, String accountNumber) {
		
		try{
			ErpSaleEB erpSaleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			PrimaryKey pk = erpSaleEB.getCustomerPk();
			ErpCustomerEB erpCustomerEB = this.getErpCustomerHome().findByPrimaryKey(pk);
			List paymentMethodList = erpCustomerEB.getPaymentMethods();
			if (paymentMethodList != null && paymentMethodList.size() > 0) {
				for (Iterator iter = paymentMethodList.iterator(); iter.hasNext();) {
					ErpPaymentMethodI pm = (ErpPaymentMethodI) iter.next();
					if (paymentMethodType != null && paymentMethodType.equals(pm.getPaymentMethodType()) &&
							accountNumber != null && accountNumber.equals(pm.getAccountNumber()) ) {
						return pm;
					}
				}
			}
			return null;
		}catch(FinderException e){
			LOGGER.warn("Cannot find sale: "+saleId, e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}	
	}

	private void updateECheckAlertForSale(String saleId) {		
		try{
			LOGGER.debug("adding customer alert for sale# "+saleId);
			ErpSaleEB erpSaleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			PrimaryKey pk = erpSaleEB.getCustomerPk();
			
			if (pk != null && pk.getId() != null) {
				List restrictedPaymentMethods = PaymentFraudManager.getRestrictedPaymentMethodsByCustomerId(pk.getId(), EnumRestrictedPaymentMethodStatus.BAD);
				// place cutomer on alert if there are any outstanding restricted payment methods
				boolean setOnAlert = (restrictedPaymentMethods != null && !restrictedPaymentMethods.isEmpty());
				boolean isAlreadyOnAlert = isOnAlert(pk, EnumAlertType.ECHECK.getName());
				// update only if need to
				if ((!setOnAlert && isAlreadyOnAlert) || (setOnAlert && !isAlreadyOnAlert)) {
					EnumAccountActivityType activityType = null;
					String note = null;
					ErpCustomerAlertModel customerAlert = new ErpCustomerAlertModel();
					if (setOnAlert) {
						activityType = EnumAccountActivityType.PLACE_ALERT;
						note = "System placed an ECheck alert due to a restricted payment method possibly caused by a settlement failure.";							
						customerAlert.setCustomerId(pk.getId());
						customerAlert.setAlertType(EnumAlertType.ECHECK.getName());
						customerAlert.setCreateDate(new Date());
						customerAlert.setCreateUserId(EnumTransactionSource.SYSTEM.getCode());						
						customerAlert.setNote(note);						
					} else if ((!setOnAlert && (customerAlert = getECheckAlertSetBySystem(pk)) != null)) {
						activityType = EnumAccountActivityType.REMOVE_ALERT;
						note = "System removed the ECheck alert due to no outstanding restricted payment method." ;							
					}
					if (customerAlert != null) {
						setAlert(pk, customerAlert, setOnAlert);					
						logAlertActivity(pk, activityType, note, EnumTransactionSource.SYSTEM);
					}
				}
			}
		}catch(FinderException e){
			LOGGER.warn("Cannot find sale: "+saleId, e);
			throw new EJBException(e);
		}catch(FDResourceException e){
			LOGGER.warn("Cannot create session bean for setCustomerAlertForSale : "+saleId, e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}	
	}

	private ErpCustomerAlertModel getECheckAlertSetBySystem(PrimaryKey pk) {
		try {
			
			// find relevant customer
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(pk);
			List alerts = customerEB.getCustomerAlerts();
			for (Iterator iter = alerts.iterator(); iter.hasNext();) {
				ErpCustomerAlertModel alert = (ErpCustomerAlertModel) iter.next();
				if (EnumAlertType.ECHECK.getName().equalsIgnoreCase(alert.getAlertType()) && EnumTransactionSource.SYSTEM.getCode().equalsIgnoreCase(alert.getCreateUserId())) {
					return alert;
				}
			}
			return null;
		}catch(FinderException e){
			LOGGER.warn("Cannot find customer bean for customer : "+ pk.getId(), e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}	
	}
	
	private void logAlertActivity(PrimaryKey pk, EnumAccountActivityType activityType, String note, EnumTransactionSource source) {
		ErpActivityRecord erpActivityRecord = new ErpActivityRecord();
		erpActivityRecord.setCustomerId(pk.getId());
		erpActivityRecord.setActivityType(activityType);
		erpActivityRecord.setCustomerId(pk.getId());
		erpActivityRecord.setDate(new Date());
		erpActivityRecord.setInitiator(source.getCode());
		erpActivityRecord.setSource(source);
		erpActivityRecord.setNote(note);
		new ErpLogActivityCommand(LOCATOR, erpActivityRecord, true).execute();
	}
	
	/**
	 * Get a list of orders that used the specific delivery pass on a customer account.
	 */
	public ErpOrderHistory getOrdersByDlvPassId(String customerPk, String dlvPassId) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			Collection history = ErpSaleInfoDAO.getOrdersByDlvPassId(conn, customerPk, dlvPassId);

			return new ErpOrderHistory(history);

		} catch (SQLException ex) {
			throw new EJBException(ex);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					LOGGER.warn("Unable to close connection", ex);
				}
			}
		}
	}

	/**
	 * Get a list of orders that used the specific delivery pass on a customer account.
	 */
	public List getRecentOrdersByDlvPassId(String customerPk, String dlvPassId,int noOfDaysOld) {
		Connection conn = null;
		List recentOrders = null;
		try {
			conn = this.getConnection();
			recentOrders = (List) ErpSaleInfoDAO.getRecentOrdersByDlvPassId(conn, customerPk, dlvPassId, noOfDaysOld);

		} catch (SQLException ex) {
			throw new EJBException(ex);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					LOGGER.warn("Unable to close connection", ex);
				}
			}
		}
		return recentOrders;
	}
	
	/**
	 * Get a list of orders that used delivery passes on a customer account.
	 */
	public Map getDlvPassesUsageInfo(String customerPk) {
		Connection conn = null;
		Map mapInfo = new HashMap();
		try {
			conn = this.getConnection();
			Collection usageList = ErpSaleInfoDAO.getOrdersUsingDlvPass(conn, customerPk);
			Iterator iter = usageList.iterator();
			while(iter.hasNext()) {
				DlvPassUsageLine usageLine = (DlvPassUsageLine) iter.next();
				String dlvPassId = usageLine.getDlvPassIdUsed();
				DlvPassUsageInfo usageInfo = null;
				if(mapInfo.containsKey(dlvPassId)) {
					usageInfo = (DlvPassUsageInfo) mapInfo.get(dlvPassId);

				}else{
					//Create a usageInfo Object and add it to the Map.
					usageInfo = new DlvPassUsageInfo();
				}
				usageInfo.addUsageLine(usageLine);
				mapInfo.put(dlvPassId, usageInfo);
			}
		} catch (SQLException ex) {
			throw new EJBException(ex);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					LOGGER.warn("Unable to close connection", ex);
				}
			}
		}
		return mapInfo;
	}
	public void updateDlvPassIdToSale(String saleId, String dlvPassId){
		try{
			ErpSaleEB erpSaleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			erpSaleEB.updateDeliveryPassId(dlvPassId);
			LOGGER.info("Update delivery pass Id - done. saleId=" + saleId);
		}catch(FinderException e){
			LOGGER.warn("Cannot find sale: "+saleId, e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}		
	}
	
	public int getValidOrderCount(PrimaryKey erpCustomerPk) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return ErpSaleInfoDAO.getValidOrderCount(conn, erpCustomerPk.getId());
		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
	}

	public String getLastOrderID(PrimaryKey erpCustomerPk) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return ErpSaleInfoDAO.getLastOrderID(conn, erpCustomerPk.getId());
		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
	}

	public boolean isOrderBelongsToUser(PrimaryKey erpCustomerPk, String saleId) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return ErpSaleInfoDAO.isOrderBelongsToUser(conn, erpCustomerPk.getId(), saleId);
		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
	}
	 public OrderHistoryI getWebOrderHistoryInfo(PrimaryKey erpCustomerPk) {
			Connection conn = null;
			try {
				conn = this.getConnection();
				Collection history = ErpSaleInfoDAO.getWebOrderHistoryInfo(conn, erpCustomerPk.getId());
				return new ErpWebOrderHistory(history);

			} catch (SQLException ex) {
				throw new EJBException(ex);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException ex) {
						LOGGER.warn("Unable to close connection", ex);
					}
				}
			}		 
	 }
		/**
		 * Get a specific sale.
		 */
		public ErpSaleModel getLastNonCOSOrder(String customerID, EnumSaleType saleType, EnumSaleStatus saleStatus, EnumPaymentMethodType paymentMethodType) throws ErpSaleNotFoundException {
			try {
				ErpSaleEB saleEB = getErpSaleHome().findByCriteria(customerID,saleType, saleStatus,paymentMethodType);

				return (ErpSaleModel) saleEB.getModel();

			} catch (FinderException fe) {
				LOGGER.warn(fe);
				throw new ErpSaleNotFoundException(fe);
			} catch (RemoteException re) {
				LOGGER.warn(re);
				throw new EJBException(re);
			}
		}
		
		public void cutOffSale(String saleId) throws ErpSaleNotFoundException {
			try {
				ErpSaleEB saleEB = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
				ErpSaleModel saleModel=(ErpSaleModel) saleEB.getModel();
				saleModel.cutoff();
			} catch (RemoteException re) {
				LOGGER.warn(re);
				throw new EJBException(re);
			} catch (FinderException fe) {
				LOGGER.warn(fe);
				throw new ErpSaleNotFoundException(fe);
			}
			catch (ErpTransactionException e) {
				LOGGER.warn(e);
				throw new EJBException(e);
			}
		}
		
		public void sendCreateOrderToSAP(String erpCustomerID, String saleID, EnumSaleType saleType, CustomerRatingI rating) throws ErpSaleNotFoundException {
			
			try {
				
				PrimaryKey erpCustomerPk=new PrimaryKey(erpCustomerID);
				PrimaryKey salePK=new PrimaryKey(saleID);
				ErpSaleEB saleEB = getErpSaleHome().findByPrimaryKey(salePK);
				ErpAbstractOrderModel orderModel=saleEB.getCurrentOrder();
				SapOrderAdapter sapOrder = this.adaptOrder(erpCustomerPk, orderModel, rating);
				SapGatewaySB sapSB = this.getSapGatewayHome().create();
				sapOrder.setWebOrderNumber(salePK.getId());
				sapSB.sendCreateSalesOrder(sapOrder,saleType);
				
			} catch (RemoteException re) {
				LOGGER.warn(re);
				throw new EJBException(re);
			} catch (FinderException fe) {
				LOGGER.warn(fe);
				throw new ErpSaleNotFoundException(fe);
			} catch (CreateException ce) {
				LOGGER.warn(ce);
				throw new EJBException(ce);
			}
		}


	public void assignAutoCaseToComplaint(ErpComplaintModel complaint, PrimaryKey autoCasePK) {
		Connection conn = null;
		try {
			conn = this.getConnection();

			PreparedStatement ps = conn.prepareStatement("UPDATE CUST.COMPLAINT SET AUTO_CASE_ID=? WHERE ID=?");
			ps.setString(1, autoCasePK.getId());
			ps.setString(2, complaint.getPK().getId());
			
			try {
				if (ps.executeUpdate() != 1) {
					throw new SQLException("Row not updated (missing complaint)");
				}
			} catch (SQLException sqle) {
				throw sqle;
			} finally {
				ps.close();
			}

		} catch(SQLException exc) {
			throw new EJBException(exc);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
	}

		public double getOutStandingBalance(ErpAbstractOrderModel order){
			//Generate Applied gift cards info.
			GiftCardApplicationStrategy strategy = new GiftCardApplicationStrategy(order, null);
			strategy.generateAppliedGiftCardsInfo();	
			return strategy.getRemainingBalance();
		}
		
		public double getPerishableBufferAmount(ErpAbstractOrderModel order){
			//Generate Applied gift cards info.
			GiftCardApplicationStrategy strategy = new GiftCardApplicationStrategy(order, null);
			strategy.generateAppliedGiftCardsInfo();	
			return strategy.getPerishableBufferAmount();
		}
}
