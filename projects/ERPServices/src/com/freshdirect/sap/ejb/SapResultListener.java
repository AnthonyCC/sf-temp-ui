/*
 * $Workfile:SapResultListener.java$
 *
 * $Date:7/22/2003 12:46:57 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.ejb;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDeliveryConfirmModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpInvoicedCreditModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpCreateCaseCommand;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ejb.GCGatewayHome;
import com.freshdirect.giftcard.ejb.GCGatewaySB;
import com.freshdirect.giftcard.ejb.GiftCardManagerHome;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.routing.ejb.ErpRoutingGatewayHome;
import com.freshdirect.routing.ejb.ErpRoutingGatewaySB;
import com.freshdirect.sap.command.SapCancelSalesOrder;
import com.freshdirect.sap.command.SapChangeSalesOrder;
import com.freshdirect.sap.command.SapCommandI;
import com.freshdirect.sap.command.SapCreateCustomer;
import com.freshdirect.sap.command.SapCreateSalesOrder;
import com.freshdirect.sap.command.SapOrderCommand;
import com.freshdirect.sap.command.SapPostReturnCommand;


/**
 *
 * @version $Revision:4$
 * @author $Author:Kashif Nadeem$
 */
public class SapResultListener extends MessageDrivenBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(SapResultListener.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();

	private static ServiceLocator ROUTING_LOCATOR = new ServiceLocator();
	
	static {
		try {
			Context ctx = FDStoreProperties.getRoutingInitialContext();
			if(ctx != null) {
				ROUTING_LOCATOR = new ServiceLocator(ctx);
			}
		} catch(NamingException e) {
			LOGGER.warn("Unable to load routing context using primary"+e);
		}
	}
	
	public void onMessage(Message message) {

		if (!(message instanceof ObjectMessage)) {
			LOGGER.warn("Internal error: Message " + message + " not an instance of ObjectMessage");
			return;
		}

		ObjectMessage om = (ObjectMessage) message;
		Object o;
		try {
			o = om.getObject();
		} catch (JMSException ex) {
			throw new RuntimeException("JMSException occured " + ex.getMessage());
		}

		if (!(o instanceof SapResult)) {
			LOGGER.warn("Internal error: ObjectMessage " + message + " object " + o + " not an instance of SapResultI");
			return;
		}

		if (o == null) {
			LOGGER.warn(new SapException("Internal error: ObjectMessage " + message + " object is null"));
			return;
		}

		SapResult result = (SapResult) o;

		LOGGER.debug("processResult");

		this.processResult(result);
	}
	
	private void processResult(SapResult result) {

		SapCommandI command = result.getCommand();
		if (command instanceof SapCreateCustomer) {
			this.processCreateCustomer((SapCreateCustomer) command);
		
		} else if (command instanceof SapOrderCommand) {
			this.processOrderCommand((SapOrderCommand)command, result);
		
		} else if(command instanceof SapPostReturnCommand){
			this.processPostReturnCommand((SapPostReturnCommand)command, result);
		
		}else {
			LOGGER.error("Unknown command " + command);
		}

	}

	private void processCreateCustomer(SapCreateCustomer command) {
		LOGGER.debug("SapCreateCustomer");

		String custId = command.getErpCustomerNumber();
		ErpCustomerEB custEB;
		try {
			custEB = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(custId));
			custEB.setSapId(command.getSapCustomerNumber());

		} catch (RemoteException e) {
			throw new EJBException("RemoteException occured processing customer " + custId, e);

		} catch (FinderException e) {
			throw new EJBException("RemoteException occured processing customer " + custId, e);

		}
	}
	
	private void processOrderCommand(SapOrderCommand command, SapResult result){
		LOGGER.debug("SapOrderCommand");

		String saleId = command.getWebOrderNumber();
		try {

			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));

			LOGGER.debug("Found sale " + saleId);

			if (!result.isSuccessful()) {

				StringWriter sw = new StringWriter();
				result.getException().printStackTrace(new PrintWriter(sw));
				String detailMessage = sw.toString();
				
				LOGGER.debug("Calling submitFailed " + saleId);
				saleEB.submitFailed(detailMessage);

				LOGGER.debug("Creating case for submitFailed " + saleId);
				createCase(saleEB.getCustomerPk(), saleId);

				LOGGER.debug("Sending email notification for submitFailed " + saleId);
				sendNotificationEmail("[System] Not Submitted sale " + saleId, detailMessage);
				

			} else {

				if (command instanceof SapCreateSalesOrder) {
					saleEB.createOrderComplete(((SapCreateSalesOrder) command).getSapOrderNumber());
					EnumSaleType saleType=((SapCreateSalesOrder) command).getSaleType();
					if(EnumSaleType.SUBSCRIPTION.equals(saleType) | EnumSaleType.DONATION.equals(saleType)) {
						saleEB.cutoff();
						addInvoice(saleEB,saleId,((SapCreateSalesOrder) command).getInvoiceNumber());
						ErpDeliveryConfirmModel deliveryConfirmModel = new ErpDeliveryConfirmModel();
						saleEB.addDeliveryConfirm(deliveryConfirmModel);					
					}
					if(EnumSaleType.GIFTCARD.equals(saleType)) {
						saleEB.cutoff();
						addInvoice(saleEB,saleId,((SapCreateSalesOrder) command).getInvoiceNumber());
						//Set the status in register pending.
						saleEB.setGiftCardRegPending();
						if(!FDStoreProperties.isGivexBlackHoleEnabled()){
							//Send register message to register queue only if blackhole is disabled.
							//otherwise leave it in register pending so register cron picks up
							//after blackhole is disabled.
							GCGatewaySB gateway = getGCGatewayHome().create();
							gateway.sendRegisterGiftCard(saleId, saleEB.getCurrentOrder().getSubTotal());
						}
					}

					if(EnumSaleType.REGULAR.equals(saleType)) {
						ErpRoutingGatewaySB erpRoutingGateway = getErpRoutingGatewayHome().create();
						erpRoutingGateway.sendReservationUpdateRequest(saleEB.getCurrentOrder().getDeliveryInfo().getDeliveryReservationId()
																		, saleEB.getCurrentOrder().getDeliveryInfo().getDeliveryAddress()
																		, ((SapCreateSalesOrder) command).getSapOrderNumber());
					}
				} else if (command instanceof SapCancelSalesOrder) {
					saleEB.cancelOrderComplete();

				} else if (command instanceof SapChangeSalesOrder) {
					saleEB.modifyOrderComplete();
					ErpRoutingGatewaySB erpRoutingGateway = getErpRoutingGatewayHome().create();
					erpRoutingGateway.sendReservationUpdateRequest(saleEB.getCurrentOrder().getDeliveryInfo().getDeliveryReservationId()
																	, saleEB.getCurrentOrder().getDeliveryInfo().getDeliveryAddress()
																	, saleEB.getSapOrderNumber());
				}

				/*else if (command instanceof SapCreateSubscriptionOrder) {
					saleEB.createOrderComplete(((SapCreateSubscriptionOrder) command).getSapOrderNumber());
					
					
				}*/ else {
					LOGGER.error("Unknown command " + command + " for sale " + saleId);

				}
			}

		} catch (ObjectNotFoundException e) {
			// we can't recover from this
			LOGGER.error("Can't find sale " + saleId, e);

		} catch (ErpTransactionException e) {
			// we can't recover from this
			LOGGER.error("ErpTransactionException occured for sale " + saleId, e);

		} catch (CreateException e) {
			throw new EJBException("CreateException occured processing sale " + saleId, e);

		} catch (RemoteException e) {
			throw new EJBException("RemoteException occured processing sale " + saleId, e);

		} 
		catch (FinderException e) {
			throw new EJBException("FinderException occured processing sale " + saleId, e);

		}
	}
	
	private void processPostReturnCommand(SapPostReturnCommand command, SapResult result){
		
		if (!result.isSuccessful()) {
			String subject = "[System] Posting return for invoice " + command.getInvoiceNumber() + " failed";

			StringWriter sw = new StringWriter();
			result.getException().printStackTrace(new PrintWriter(sw));

			sendNotificationEmail(subject, sw.toString());
		}
		
	}

	private void sendNotificationEmail(String subject, String body) {
		try {
			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getSapMailFrom(), 
							ErpServicesProperties.getSapMailTo(), 
							ErpServicesProperties.getSapMailCC(), 
							subject, body);
		} catch (MessagingException e) {
			throw new EJBException(e);
		}		
	}
	
	private void createCase(PrimaryKey customerPK, String saleId) {
		CrmCaseSubject subject = CrmCaseSubject.getEnum(CrmCaseSubject.CODE_NOT_SUBMITTED);
		String summary = "Failed to submit sale " + saleId + " to SAP";
		CrmSystemCaseInfo info = new CrmSystemCaseInfo(customerPK, new PrimaryKey(saleId), subject, summary);
		new ErpCreateCaseCommand(LOCATOR, info).execute();
	}

	private ErpCustomerHome getErpCustomerHome() {
		try {
			return (ErpCustomerHome) LOCATOR.getRemoteHome("freshdirect.erp.Customer");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
	
	private GiftCardManagerHome getGiftCardManagerHome() {
		try {
			return (GiftCardManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.GiftCardManager");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private GCGatewayHome getGCGatewayHome() {
		try {
			return (GCGatewayHome) LOCATOR.getRemoteHome("freshdirect.giftcard.Gateway");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private  ErpRoutingGatewayHome getErpRoutingGatewayHome() {
		try {
			return (ErpRoutingGatewayHome) ROUTING_LOCATOR.getRemoteHome("freshdirect.routing.ErpRoutingGateway");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private ErpSaleHome getErpSaleHome() {
		try {
			return (ErpSaleHome) LOCATOR.getRemoteHome("freshdirect.erp.Sale");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
	public void addInvoice(ErpSaleEB saleEB,String saleId, String invoiceNumber) throws EJBException, ErpTransactionException, RemoteException  {
		
		ErpAbstractOrderModel order = saleEB.getCurrentOrder();
		ErpInvoiceModel invoice = new ErpInvoiceModel();
		
		invoice.setAmount(order.getAmount());
		invoice.setInvoiceNumber(invoiceNumber);
		//Discount discount = order.getDiscount();
		Discount invDiscount = null;
		//if(discount != null){
			//invDiscount = new Discount("UNKNOWN", discount.getDiscountType(), discount.getAmount());
		//}
		//invoice.setDiscount(invDiscount);
		invoice.setDiscounts(order.getDiscounts());
		invoice.setSubTotal(order.getSubTotal());
		invoice.setTax(order.getTax());
		invoice.setTransactionSource(EnumTransactionSource.SYSTEM);
		invoice.setTransactionDate(new Date());
		
		List orderLines = order.getOrderLines();
		List invoiceLines = new ArrayList();
		ErpOrderLineModel orderLine = null;
		ErpInvoiceLineModel invoiceLine = null;

		for(int i = 0, size = orderLines.size(); i < size; i++){
			orderLine = (ErpOrderLineModel)orderLines.get(i);
			invoiceLine = new ErpInvoiceLineModel();
			invoiceLine.setPrice(orderLine.getPrice());
			invoiceLine.setQuantity(orderLine.getQuantity());
			invoiceLine.setWeight(orderLine.getQuantity());
			invoiceLine.setDepositValue(orderLine.getDepositValue());
			invoiceLine.setMaterialNumber(orderLine.getMaterialNumber());
			invoiceLine.setOrderLineNumber(orderLine.getOrderLineNumber());
			invoiceLine.setTaxValue(orderLine.getPrice() * orderLine.getTaxRate());
			
			invoiceLines.add(invoiceLine);
		}
		
		invoice.setInvoiceLines(invoiceLines);

		List invoicedCharges = new ArrayList();
		for(Iterator i = order.getCharges().iterator(); i.hasNext(); ){
			ErpChargeLineModel charge = (ErpChargeLineModel) i.next();			
			ErpChargeLineModel invoicedCharge = new ErpChargeLineModel();			
			invoicedCharge.setAmount(charge.getAmount());
			invoicedCharge.setDiscount(charge.getDiscount());
			invoicedCharge.setReasonCode(charge.getReasonCode());
			invoicedCharge.setTaxRate(charge.getTaxRate());
			invoicedCharge.setType(charge.getType());			
			invoicedCharges.add(invoicedCharge);
		}
		invoice.setCharges(invoicedCharges);
			
		List invoicedCredits = new ArrayList();
		int sapCreditNumber = 1;
		for(Iterator i = order.getAppliedCredits().iterator(); i.hasNext(); ){
			ErpAppliedCreditModel appliedCredit = (ErpAppliedCreditModel) i.next();
			
			ErpInvoicedCreditModel invoicedCredit = new ErpInvoicedCreditModel();
			invoicedCredit.setAffiliate(ErpAffiliate.getEnum(ErpAffiliate.CODE_FD));
			invoicedCredit.setAmount(appliedCredit.getAmount());
			invoicedCredit.setDepartment(appliedCredit.getDepartment());
			invoicedCredit.setCustomerCreditPk(appliedCredit.getCustomerCreditPk());
			invoicedCredit.setOriginalCreditId(appliedCredit.getPK().getId());
			invoicedCredit.setSapNumber("0000"+sapCreditNumber++);
			
			invoicedCredits.add(invoicedCredit);
			
		}
		invoice.setAppliedCredits(invoicedCredits);
		ErpShippingInfo sInfo=null;
		addAndReconcileInvoice(saleId,invoice,sInfo);
		
	}
	
        public void addAndReconcileInvoice(String saleId, ErpInvoiceModel invoice, ErpShippingInfo shippingInfo) throws ErpTransactionException {
            try {
                getErpCustomerManagerHome().create().addAndReconcileInvoice(saleId, invoice, shippingInfo);
            } catch (CreateException e) {
                throw new EJBException("CreateException occured processing sale " + saleId, e);
            } catch (RemoteException e) {
                throw new EJBException("RemoteException occured processing sale " + saleId, e);
            }
        }

	private ErpCustomerManagerHome getErpCustomerManagerHome() {
		try {
			return (ErpCustomerManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.CustomerManager");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	
}
